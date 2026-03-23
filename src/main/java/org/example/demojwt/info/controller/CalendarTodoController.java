package org.example.demojwt.info.controller;

import org.example.demojwt.common.dto.ApiResponse;
import org.example.demojwt.info.entity.CalendarTodo;
import org.example.demojwt.info.entity.User;
import org.example.demojwt.info.repository.CalendarTodoRepository;
import org.example.demojwt.info.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/todos")
public class CalendarTodoController {

    @Autowired
    private CalendarTodoRepository calendarTodoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAll() {
        User me = currentUser();
        List<Map<String, Object>> items = calendarTodoRepository
                .findByUser_IdOrderByDateAscCreatedAtAsc(me.getId())
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Todos", items));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> list(
            @RequestParam(value = "date", required = false) String dateIso,
            @RequestParam(value = "from", required = false) String fromIso,
            @RequestParam(value = "to", required = false) String toIso
    ) {
        User me = currentUser();
        List<CalendarTodo> todos;

        if (fromIso != null && toIso != null && !fromIso.isEmpty() && !toIso.isEmpty()) {
            LocalDate from = LocalDate.parse(fromIso);
            LocalDate to = LocalDate.parse(toIso);
            todos = calendarTodoRepository.findByUser_IdAndDateBetweenOrderByDateAscCreatedAtAsc(me.getId(), from, to);
        } else if (dateIso != null && !dateIso.isEmpty()) {
            LocalDate date = LocalDate.parse(dateIso);
            todos = calendarTodoRepository.findByUser_IdAndDateOrderByCreatedAtAsc(me.getId(), date);
        } else {
            todos = calendarTodoRepository.findByUser_IdOrderByDateAscCreatedAtAsc(me.getId());
        }

        List<Map<String, Object>> items = todos.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Todos", items));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> create(@RequestBody Map<String, Object> body) {
        User me = currentUser();
        String dateIso = String.valueOf(body.getOrDefault("date",
                                                          ""));
        String text = String.valueOf(body.getOrDefault("text",
                                                       ""))
                .trim();
        String project = body.get("project") != null ? String.valueOf(body.get("project")).trim() : null;
        String priority = body.get("priority") != null ? String.valueOf(body.get("priority")).trim() : "medium";
        String status = body.get("status") != null ? String.valueOf(body.get("status")).trim() : "open";
        if (text.isEmpty() || dateIso.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("date and text are required"));
        }
        LocalDate date = LocalDate.parse(dateIso);
        boolean done = "done".equalsIgnoreCase(status);
        CalendarTodo entity = CalendarTodo.builder()
                .user(me)
                .date(date)
                .text(text)
                .project(project)
                .priority(priority)
                .status(status)
                .done(done)
                .build();
        CalendarTodo saved = calendarTodoRepository.save(entity);
        return ResponseEntity.ok(ApiResponse.success("Created",
                                                     toDto(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> update(@PathVariable Long id,
                                                                   @RequestBody Map<String, Object> body) {
        User me = currentUser();
        CalendarTodo todo = calendarTodoRepository.findByIdAndUser_Id(id,
                                                                      me.getId())
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        if (body.containsKey("text")) {
            String text = String.valueOf(body.get("text")).trim();
            if (!text.isEmpty())
                todo.setText(text);
        }
        if (body.containsKey("project")) {
            String project = body.get("project") != null ? String.valueOf(body.get("project")).trim() : null;
            todo.setProject(project);
        }
        if (body.containsKey("priority")) {
            String priority = String.valueOf(body.get("priority")).trim();
            todo.setPriority(priority);
        }
        if (body.containsKey("status")) {
            String status = String.valueOf(body.get("status")).trim();
            todo.setStatus(status);
            todo.setDone("done".equalsIgnoreCase(status));
        }
        if (body.containsKey("done")) {
            boolean done = Boolean.parseBoolean(String.valueOf(body.get("done")));
            todo.setDone(done);
            if (done && !body.containsKey("status")) {
                todo.setStatus("done");
            }
            if (!done && !body.containsKey("status") && "done".equalsIgnoreCase(todo.getStatus())) {
                todo.setStatus("open");
            }
        }
        CalendarTodo saved = calendarTodoRepository.save(todo);
        return ResponseEntity.ok(ApiResponse.success("Updated",
                                                     toDto(saved)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        User me = currentUser();
        CalendarTodo todo = calendarTodoRepository.findByIdAndUser_Id(id,
                                                                      me.getId())
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        calendarTodoRepository.delete(todo);
        return ResponseEntity.ok(ApiResponse.success("Deleted",
                                                     "ok"));
    }

    private User currentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Map<String, Object> toDto(CalendarTodo t) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", t.getId());
        dto.put("date", t.getDate() != null ? t.getDate().toString() : null);
        dto.put("text", t.getText());
        dto.put("project", t.getProject());
        dto.put("priority", t.getPriority());
        dto.put("status", t.getStatus() != null ? t.getStatus() : "open");
        dto.put("done", t.isDone());
        dto.put("createdAt", t.getCreatedAt());
        dto.put("updatedAt", t.getUpdatedAt());
        return dto;
    }
}
