package org.example.demojwt.common.socket;

import org.example.demojwt.info.entity.Message;
import org.example.demojwt.info.entity.MessageType;
import org.example.demojwt.info.entity.User;
import org.example.demojwt.info.repository.MessageRepository;
import org.example.demojwt.info.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocketService {

    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserRepository userRepository;

    public Message saveMessage(Long senderId, String roomId, String content, String type) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Message message = Message.builder()
                .sender(sender)
                .roomId(roomId)
                .content(content)
                .type(MessageType.valueOf(type))
                .build();

        return messageRepository.save(message);
    }
    
    public List<Message> getMessages(String roomId) {
        return messageRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
    }
}
