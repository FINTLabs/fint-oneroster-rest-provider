package no.fint.oneroster.controller;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.model.User;
import no.fint.oneroster.service.UserService;
import no.fint.oneroster.util.OneRosterResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestHeader String orgId, Pageable pageable,
                                         @RequestParam(value = "filter", required = false) String filter,
                                         @RequestParam(value = "fields", required = false) String fields) {
        List<User> users = userService.getAllUsers(orgId);

        List<User> modifiedUsers = new OneRosterResponse.Builder<>(users)
                .filter(filter)
                .sort(pageable.getSort())
                .page(pageable)
                .build();

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("users", modifiedUsers));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(User.class, fields)));

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(body);
    }

    @GetMapping("/users/{sourcedId}")
    public ResponseEntity<?> getUser(@RequestHeader String orgId, @PathVariable String sourcedId,
                                     @RequestParam(value = "fields", required = false) String fields) {
        User user = userService.getUser(orgId, sourcedId);

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("user", user));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(User.class, fields)));

        return ResponseEntity.ok(body);
    }

    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents(@RequestHeader String orgId, Pageable pageable,
                                            @RequestParam(value = "filter", required = false) String filter,
                                            @RequestParam(value = "fields", required = false) String fields) {
        List<User> students = userService.getAllStudents(orgId);

        List<User> modifiedstudents = new OneRosterResponse.Builder<>(students)
                .filter(filter)
                .sort(pageable.getSort())
                .page(pageable)
                .build();

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("users", modifiedstudents));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(User.class, fields)));

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(students.size()))
                .body(body);
    }

    @GetMapping("/students/{sourcedId}")
    public ResponseEntity<?> getStudent(@RequestHeader String orgId, @PathVariable String sourcedId,
                                        @RequestParam(value = "fields", required = false) String fields) {
        User student = userService.getStudent(orgId, sourcedId);

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("user", student));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(User.class, fields)));

        return ResponseEntity.ok(body);
    }

    @GetMapping("/teachers")
    public ResponseEntity<?> getAllTeachers(@RequestHeader String orgId, Pageable pageable,
                                            @RequestParam(value = "filter", required = false) String filter,
                                            @RequestParam(value = "fields", required = false) String fields) {
        List<User> teachers = userService.getAllTeachers(orgId);

        List<User> modifiedTeachers = new OneRosterResponse.Builder<>(teachers)
                .filter(filter)
                .sort(pageable.getSort())
                .page(pageable)
                .build();

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("users", modifiedTeachers));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(User.class, fields)));

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(teachers.size()))
                .body(body);
    }

    @GetMapping("/teachers/{sourcedId}")
    public ResponseEntity<?> getTeacher(@RequestHeader String orgId, @PathVariable String sourcedId,
                                        @RequestParam(value = "fields", required = false) String fields) {
        User teacher = userService.getTeacher(orgId, sourcedId);

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("user", teacher));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(User.class, fields)));

        return ResponseEntity.ok(body);
    }
}
