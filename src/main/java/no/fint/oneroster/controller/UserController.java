package no.fint.oneroster.controller;

import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.model.User;
import no.fint.oneroster.service.ClazzService;
import no.fint.oneroster.service.UserService;
import no.fint.oneroster.util.OneRosterResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;
    private final ClazzService clazzService;

    public UserController(UserService userService, ClazzService clazzService) {
        this.userService = userService;
        this.clazzService = clazzService;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "filter", required = false) String filter,
                                         @RequestParam(value = "fields", required = false) String fields,
                                         Pageable pageable) {

        List<User> users = userService.getAllUsers();

        OneRosterResponse<User> oneRosterResponse = new OneRosterResponse<>(User.class, "users")
                .collection(users)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/users/{sourcedId}")
    public ResponseEntity<?> getUser(@PathVariable String sourcedId,
                                     @RequestParam(value = "fields", required = false) String fields) {

        User user = userService.getUser(sourcedId);

        OneRosterResponse<User> oneRosterResponse = new OneRosterResponse<>(User.class, "user")
                .item(user)
                .fieldSelection(fields);

        return ResponseEntity.ok(oneRosterResponse.getBody());
    }

    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents(@RequestParam(value = "filter", required = false) String filter,
                                            @RequestParam(value = "fields", required = false) String fields,
                                            Pageable pageable) {

        List<User> students = userService.getAllStudents();

        OneRosterResponse<User> oneRosterResponse = new OneRosterResponse<>(User.class, "users")
                .collection(students)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/students/{sourcedId}")
    public ResponseEntity<?> getStudent(@PathVariable String sourcedId,
                                        @RequestParam(value = "fields", required = false) String fields) {

        User student = userService.getStudent(sourcedId);

        OneRosterResponse<User> oneRosterResponse = new OneRosterResponse<>(User.class, "user")
                .item(student)
                .fieldSelection(fields);

        return ResponseEntity.ok(oneRosterResponse.getBody());
    }

    @GetMapping("/teachers")
    public ResponseEntity<?> getAllTeachers(@RequestParam(value = "filter", required = false) String filter,
                                            @RequestParam(value = "fields", required = false) String fields,
                                            Pageable pageable) {

        List<User> teachers = userService.getAllTeachers();

        OneRosterResponse<User> oneRosterResponse = new OneRosterResponse<>(User.class, "users")
                .collection(teachers)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/teachers/{sourcedId}")
    public ResponseEntity<?> getTeacher(@PathVariable String sourcedId,
                                        @RequestParam(value = "fields", required = false) String fields) {

        User teacher = userService.getTeacher(sourcedId);

        OneRosterResponse<User> oneRosterResponse = new OneRosterResponse<>(User.class, "user")
                .item(teacher)
                .fieldSelection(fields);

        return ResponseEntity.ok(oneRosterResponse.getBody());
    }

    @GetMapping("/students/{sourcedId}/classes")
    public ResponseEntity<?> getClazzesForStudent(@PathVariable String sourcedId, Pageable pageable,
                                                  @RequestParam(value = "filter", required = false) String filter,
                                                  @RequestParam(value = "fields", required = false) String fields) {

        List<Clazz> clazzes = clazzService.getClazzesForStudent(sourcedId);

        OneRosterResponse<Clazz> oneRosterResponse = new OneRosterResponse<>(Clazz.class, "classes")
                .collection(clazzes)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/teachers/{sourcedId}/classes")
    public ResponseEntity<?> getClazzesForTeacher(@PathVariable String sourcedId, Pageable pageable,
                                                  @RequestParam(value = "filter", required = false) String filter,
                                                  @RequestParam(value = "fields", required = false) String fields) {

        List<Clazz> clazzes = clazzService.getClazzesForTeacher(sourcedId);

        OneRosterResponse<Clazz> oneRosterResponse = new OneRosterResponse<>(Clazz.class, "classes")
                .collection(clazzes)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }
}
