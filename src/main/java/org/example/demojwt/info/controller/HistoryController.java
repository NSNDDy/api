package org.example.demojwt.info.controller;

import lombok.RequiredArgsConstructor;
import org.example.demojwt.common.dto.ApiResponse;
import org.example.demojwt.info.entity.Message;
import org.example.demojwt.info.repository.HistoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryRepository historyRepository;

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<Message>>> getHistory(@RequestParam String roomId) {

        List<Message> messageList = historyRepository.findAllbyMessage(roomId);

        if (messageList.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Message is empty !"));
        }

        return ResponseEntity.ok(ApiResponse.success("Success!",
                                                     messageList));
    }
}
