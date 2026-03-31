package org.example.demojwt.info.controller;

import lombok.RequiredArgsConstructor;
import org.example.demojwt.common.dto.ApiResponse;
import org.example.demojwt.info.service.FinanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTransactions(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        return financeService.getTransactions(from, to);
    }

    @PostMapping("/transactions")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createTransaction(@RequestBody Map<String, Object> body) {
        return financeService.createTransaction(body);
    }

    @PutMapping("/transactions/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateTransaction(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return financeService.updateTransaction(id, body);
    }

    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTransaction(@PathVariable Long id) {
        return financeService.deleteTransaction(id);
    }

    // Goals Endpoints
    @GetMapping("/goals")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getGoals() {
        return financeService.getGoals();
    }

    @PostMapping("/goals")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createGoal(@RequestBody Map<String, Object> body) {
        return financeService.createGoal(body);
    }

    @PutMapping("/goals/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateGoal(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return financeService.updateGoal(id, body);
    }

    @DeleteMapping("/goals/{id}")
    public ResponseEntity<ApiResponse<String>> deleteGoal(@PathVariable Long id) {
        return financeService.deleteGoal(id);
    }
}
