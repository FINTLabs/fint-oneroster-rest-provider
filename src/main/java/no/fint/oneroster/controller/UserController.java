package no.fint.oneroster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.model.User;
import no.fint.oneroster.service.UserService;
import org.apache.commons.beanutils.BeanComparator;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/users")
    public Map<String, List<User>> getAllUsers(@RequestHeader String orgId, FilterProvider fields, Pageable pageable) {
        List<User> users = userService.getAllUsers(orgId);

        pageable.getSort().get().findFirst().ifPresent(sort -> {
            BeanComparator<User> comparator = new BeanComparator<>(sort.getProperty());
            users.sort(comparator);
        });

        List<User> filteredUsers = users.stream()
                .skip(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("users", filteredUsers);
    }

    @GetMapping("/users/{sourcedId}")
    public Map<String, User> getUser(@RequestHeader String orgId, @PathVariable String sourcedId, FilterProvider fields) {
        User user = userService.getUser(orgId, sourcedId);

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("user", user);
    }

    @GetMapping("/students")
    public Map<String, List<User>> getAllStudents(@RequestHeader String orgId, FilterProvider fields, Pageable pageable) {
        List<User> students = userService.getAllStudents(orgId);

        pageable.getSort().get().findFirst().ifPresent(sort -> {
            BeanComparator<User> comparator = new BeanComparator<>(sort.getProperty());
            students.sort(comparator);
        });

        List<User> filteredStudents = students.stream()
                .skip(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("users", filteredStudents);
    }

    @GetMapping("/students/{sourcedId}")
    public Map<String, User> getStudent(@RequestHeader String orgId, @PathVariable String sourcedId, FilterProvider fields) {
        User student = userService.getStudent(orgId, sourcedId);

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("user", student);
    }

    @GetMapping("/teachers")
    public Map<String, List<User>> getAllTeachers(@RequestHeader String orgId, FilterProvider fields, Pageable pageable) {
        List<User> teachers = userService.getAllTeachers(orgId);

        pageable.getSort().get().findFirst().ifPresent(sort -> {
            BeanComparator<User> comparator = new BeanComparator<>(sort.getProperty());
            teachers.sort(comparator);
        });

        List<User> filteredTeachers = teachers.stream()
                .skip(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("users", filteredTeachers);
    }

    @GetMapping("/teachers/{sourcedId}")
    public Map<String, User> getTeacher(@RequestHeader String orgId, @PathVariable String sourcedId, FilterProvider fields) {
        User teacher = userService.getTeacher(orgId, sourcedId);

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("user", teacher);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        log.error("WebClientException - Status: {}, Body: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
        return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
    }
}
