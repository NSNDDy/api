package org.example.demojwt.common.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.demojwt.common.service.JwtService;
import org.example.demojwt.info.entity.Message;
import org.example.demojwt.info.entity.User;
import org.example.demojwt.info.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class SocketModule {

    private final SocketIOServer server;
    private final SocketService socketService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public SocketModule(SocketIOServer server, SocketService socketService, JwtService jwtService, UserRepository userRepository) {
        this.server = server;
        this.socketService = socketService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;

        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("join_room", JoinRoomRequest.class, onJoinRoom());
        server.addEventListener("send_message", MessageRequest.class, onSendMessage());
    }

    @jakarta.annotation.PostConstruct
    private void autoStartup() {
        server.start();
    }

    @jakarta.annotation.PreDestroy
    private void autoStop() {
        server.stop();
    }

    private ConnectListener onConnected() {
        return (client) -> {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            if (token == null) {
                token = client.getHandshakeData().getHttpHeaders().get("Authorization");
            }

            if (token != null) {
                String normalized = token.replace("%20", " ").replace("+", " ").trim();
                if (normalized.regionMatches(true, 0, "Bearer ", 0, 7)) {
                    token = normalized.substring(7).trim();
                } else {
                    token = normalized;
                }
            }

            if (token != null && jwtService.validateToken(token)) {
                String username = jwtService.extractUsername(token);
                Optional<User> user = userRepository.findByUsername(username);
                if (user.isPresent()) {
                    client.set("user_id", user.get().getId());
                    client.set("username", username);
                    // Join personal room for private notifications
                    client.joinRoom("user_" + user.get().getId());
                    log.info("User {} connected with socket {}", username, client.getSessionId());
                } else {
                    client.disconnect();
                }
            } else {
                log.warn("Invalid token for socket connection");
                client.disconnect();
            }
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("Client[{}] - Disconnected from socket", client.getSessionId());
        };
    }

    private DataListener<JoinRoomRequest> onJoinRoom() {
        return (client, data, ackSender) -> {
            log.info("Client[{}] - Joined room {}", client.getSessionId(), data.getRoomId());
            client.joinRoom(data.getRoomId());
            
            // Send history messages (Optional - not in spec but good for chat)
            // List<Message> messages = socketService.getMessages(data.getRoomId());
            // ... send messages to client
        };
    }

    private DataListener<MessageRequest> onSendMessage() {
        return (client, data, ackSender) -> {
            Long userId = client.get("user_id");
            if (userId == null) {
                sendError(client, "FORBIDDEN", "You are not allowed to chat.");
                return;
            }

            log.info("Client[{}] - Sending message to room {}", client.getSessionId(), data.getRoomId());
            
            // Save to DB
            Message message = socketService.saveMessage(userId, data.getRoomId(), data.getContent(), data.getType());
            
            // Broadcast to room
            Map<String, Object> response = new HashMap<>();
            response.put("id", message.getId()); // or "msg_" + message.getId()
            response.put("roomId", message.getRoomId());
            response.put("content", message.getContent());
            response.put("timestamp", message.getCreatedAt().toString());
            
            Map<String, Object> sender = new HashMap<>();
            sender.put("id", message.getSender().getId());
            sender.put("username", message.getSender().getUsername());
            sender.put("avatar", message.getSender().getAvatar());
            response.put("sender", sender);
            
            // Broadcast to the room (for people currently in the chat window)
            server.getRoomOperations(data.getRoomId()).sendEvent("receive_message", response);

            // If it's a private room, also notify the other user (even if they're not in the room)
            if (data.getRoomId().startsWith("private_")) {
                String[] parts = data.getRoomId().split("_");
                if (parts.length == 3) {
                    try {
                        Long id1 = Long.parseLong(parts[1]);
                        Long id2 = Long.parseLong(parts[2]);
                        Long otherUserId = userId.equals(id1) ? id2 : id1;
                        
                        // Send specifically to the other user's personal room
                        server.getRoomOperations("user_" + otherUserId).sendEvent("private_notification", response);
                    } catch (NumberFormatException e) {
                        log.error("Error parsing room ID for private notification", e);
                    }
                }
            }
        };
    }
    
    private void sendError(SocketIOClient client, String code, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("code", code);
        error.put("message", message);
        client.sendEvent("error", error);
    }

    @Data
    public static class JoinRoomRequest {
        private String roomId;
    }

    @Data
    public static class MessageRequest {
        private String roomId;
        private String content;
        private String type;
    }
}
