package no.fint.oneroster.controller;

import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.model.User;
import no.fint.oneroster.response.OneRosterCollectionResponse;
import no.fint.oneroster.response.OneRosterItemResponse;
import no.fint.oneroster.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "filter", required = false) String filter,
                                         @RequestParam(value = "fields", required = false) String fields,
                                         Pageable pageable) {

        List<User> users = userService.getAllUsers();

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(users, User.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @GetMapping("/users/{sourcedId}")
    public ResponseEntity<?> getUser(@PathVariable String sourcedId,
                                     @RequestParam(value = "fields", required = false) String fields) {

        User user = userService.getUser(sourcedId);

        OneRosterItemResponse response = new OneRosterItemResponse.Builder<>(user)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents(@RequestParam(value = "filter", required = false) String filter,
                                            @RequestParam(value = "fields", required = false) String fields,
                                            Pageable pageable) {

        List<User> students = userService.getAllStudents();

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(students, User.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @GetMapping("/students/{sourcedId}")
    public ResponseEntity<?> getStudent(@PathVariable String sourcedId,
                                        @RequestParam(value = "fields", required = false) String fields) {

        User student = userService.getStudent(sourcedId);

        OneRosterItemResponse response = new OneRosterItemResponse.Builder<>(student)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/teachers")
    public ResponseEntity<?> getAllTeachers(@RequestParam(value = "filter", required = false) String filter,
                                            @RequestParam(value = "fields", required = false) String fields,
                                            Pageable pageable) {

        List<User> teachers = userService.getAllTeachers();

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(teachers, User.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @GetMapping("/teachers/{sourcedId}")
    public ResponseEntity<?> getTeacher(@PathVariable String sourcedId,
                                        @RequestParam(value = "fields", required = false) String fields) {

        User teacher = userService.getTeacher(sourcedId);

        OneRosterItemResponse response = new OneRosterItemResponse.Builder<>(teacher)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/students/{sourcedId}/classes")
    public ResponseEntity<?> getClazzesForStudent(@PathVariable String sourcedId, Pageable pageable,
                                                  @RequestParam(value = "filter", required = false) String filter,
                                                  @RequestParam(value = "fields", required = false) String fields) {

        List<Clazz> clazzes = userService.getClazzesForStudent(sourcedId);

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(clazzes, Clazz.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @GetMapping("/teachers/{sourcedId}/classes")
    public ResponseEntity<?> getClazzesForTeacher(@PathVariable String sourcedId, Pageable pageable,
                                                  @RequestParam(value = "filter", required = false) String filter,
                                                  @RequestParam(value = "fields", required = false) String fields) {

        List<Clazz> clazzes = userService.getClazzesForTeacher(sourcedId);

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(clazzes, Clazz.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }
}