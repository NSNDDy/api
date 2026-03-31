package org.example.demojwt.info.service;

import lombok.RequiredArgsConstructor;
import org.example.demojwt.common.dto.ApiResponse;
import org.example.demojwt.info.entity.Transaction;
import org.example.demojwt.info.entity.User;
import org.example.demojwt.info.repository.TransactionRepository;
import org.example.demojwt.info.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.demojwt.info.entity.SavingsGoal;
import org.example.demojwt.info.repository.SavingsGoalRepository;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final TransactionRepository transactionRepository;
    private final SavingsGoalRepository savingsGoalRepository;
    private final UserRepository userRepository;

    // ... existing transaction methods ...

    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getGoals() {
        User me = currentUser();
        List<SavingsGoal> goals = savingsGoalRepository.findByUser(me);
        List<Map<String, Object>> items = goals.stream().map(this::goalToDto).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Goals", items));
    }

    public ResponseEntity<ApiResponse<Map<String, Object>>> createGoal(Map<String, Object> body) {
        User me = currentUser();
        SavingsGoal goal = SavingsGoal.builder()
                .user(me)
                .name((String) body.get("name"))
                .targetAmount(new BigDecimal(String.valueOf(body.get("targetAmount"))))
                .currentAmount(new BigDecimal(String.valueOf(body.get("currentAmount"))))
                .type((String) body.get("type"))
                .build();
        SavingsGoal saved = savingsGoalRepository.save(goal);
        return ResponseEntity.ok(ApiResponse.success("Created", goalToDto(saved)));
    }

    public ResponseEntity<ApiResponse<Map<String, Object>>> updateGoal(Long id, Map<String, Object> body) {
        User me = currentUser();
        SavingsGoal goal = savingsGoalRepository.findById(id)
                .filter(g -> g.getUser().getId().equals(me.getId()))
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        if (body.containsKey("name")) goal.setName((String) body.get("name"));
        if (body.containsKey("targetAmount")) goal.setTargetAmount(new BigDecimal(String.valueOf(body.get("targetAmount"))));
        if (body.containsKey("currentAmount")) goal.setCurrentAmount(new BigDecimal(String.valueOf(body.get("currentAmount"))));
        if (body.containsKey("type")) goal.setType((String) body.get("type"));

        SavingsGoal saved = savingsGoalRepository.save(goal);
        return ResponseEntity.ok(ApiResponse.success("Updated", goalToDto(saved)));
    }

    public ResponseEntity<ApiResponse<String>> deleteGoal(Long id) {
        User me = currentUser();
        SavingsGoal goal = savingsGoalRepository.findById(id)
                .filter(g -> g.getUser().getId().equals(me.getId()))
                .orElseThrow(() -> new RuntimeException("Goal not found"));
        savingsGoalRepository.delete(goal);
        return ResponseEntity.ok(ApiResponse.success("Deleted", "Goal deleted successfully"));
    }

    private Map<String, Object> goalToDto(SavingsGoal g) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", g.getId());
        map.put("name", g.getName());
        map.put("targetAmount", g.getTargetAmount());
        map.put("currentAmount", g.getCurrentAmount());
        map.put("type", g.getType());
        return map;
    }

    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTransactions(String fromIso, String toIso) {
        User me = currentUser();
        List<Transaction> transactions;

        if (fromIso != null && toIso != null && !fromIso.isEmpty() && !toIso.isEmpty()) {
            LocalDate from = LocalDate.parse(fromIso);
            LocalDate to = LocalDate.parse(toIso);
            transactions = transactionRepository.findByUserAndDateBetweenOrderByDateDescCreatedAtDesc(me, from, to);
        } else {
            transactions = transactionRepository.findByUserOrderByDateDescCreatedAtDesc(me);
        }

        List<Map<String, Object>> items = transactions.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Transactions", items));
    }

    public ResponseEntity<ApiResponse<Map<String, Object>>> createTransaction(Map<String, Object> body) {
        User me = currentUser();
        
        String type = (String) body.get("type"); // INCOME | EXPENSE
        String category = (String) body.get("category");
        BigDecimal amount = new BigDecimal(String.valueOf(body.get("amount")));
        String description = (String) body.get("description");
        String dateIso = (String) body.get("date");

        if (type == null || category == null || amount == null || dateIso == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Missing required fields"));
        }

        Transaction transaction = Transaction.builder()
                .user(me)
                .type(type)
                .category(category)
                .amount(amount)
                .description(description)
                .date(LocalDate.parse(dateIso))
                .build();

        Transaction saved = transactionRepository.save(transaction);
        return ResponseEntity.ok(ApiResponse.success("Created", toDto(saved)));
    }

    public ResponseEntity<ApiResponse<Map<String, Object>>> updateTransaction(Long id, Map<String, Object> body) {
        User me = currentUser();
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> t.getUser().getId().equals(me.getId()))
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (body.containsKey("type")) transaction.setType((String) body.get("type"));
        if (body.containsKey("category")) transaction.setCategory((String) body.get("category"));
        if (body.containsKey("amount")) transaction.setAmount(new BigDecimal(String.valueOf(body.get("amount"))));
        if (body.containsKey("description")) transaction.setDescription((String) body.get("description"));
        if (body.containsKey("date")) transaction.setDate(LocalDate.parse((String) body.get("date")));

        Transaction saved = transactionRepository.save(transaction);
        return ResponseEntity.ok(ApiResponse.success("Updated", toDto(saved)));
    }

    public ResponseEntity<ApiResponse<String>> deleteTransaction(Long id) {
        User me = currentUser();
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> t.getUser().getId().equals(me.getId()))
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transactionRepository.delete(transaction);
        return ResponseEntity.ok(ApiResponse.success("Deleted", "Transaction deleted successfully"));
    }

    private Map<String, Object> toDto(Transaction t) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", t.getId());
        map.put("type", t.getType());
        map.put("category", t.getCategory());
        map.put("amount", t.getAmount());
        map.put("description", t.getDescription());
        map.put("date", t.getDate().toString());
        map.put("createdAt", t.getCreatedAt());
        return map;
    }

    private User currentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
