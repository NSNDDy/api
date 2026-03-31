package org.example.demojwt.info.controller;

import org.example.demojwt.common.dto.ApiResponse;
import org.example.demojwt.info.entity.CalendarTodo;
import org.example.demojwt.info.entity.User;
import org.example.demojwt.info.repository.CalendarTodoRepository;
import org.example.demojwt.info.repository.UserRepository;
import org.example.demojwt.info.service.CalendarService;
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
    private CalendarService calendarService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAll() {
        return calendarService.getAll();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> list(@RequestParam(value = "date", required = false) String dateIso,
                                                                       @RequestParam(value = "from", required = false) String fromIso,
                                                                       @RequestParam(value = "to", required = false) String toIso) {
        return calendarService.getList(dateIso,
                                       fromIso,
                                       toIso);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> create(@RequestBody Map<String, Object> body) {
        return calendarService.create(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> update(@PathVariable Long id,
                                                                   @RequestBody Map<String, Object> body) {
        return calendarService.update(id,
                                      body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        return calendarService.delete(id);
    }
}
