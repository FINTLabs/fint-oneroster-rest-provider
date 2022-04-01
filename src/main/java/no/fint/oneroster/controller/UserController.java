package no.fint.oneroster.controller;

import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.model.User;
import no.fint.oneroster.response.OneRosterCollectionResponse;
import no.fint.oneroster.response.OneRosterItemResponse;
import no.fint.oneroster.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(userService.getAllUsers(), User.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @GetMapping("/users/{sourcedId}")
    public ResponseEntity<?> getUser(@PathVariable String sourcedId,
                                     @RequestParam(value = "fields", required = false) String fields,
                                     Pageable pageable) {

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(userService.getUserRoles(sourcedId) , User.class )
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents(@RequestParam(value = "filter", required = false) String filter,
                                            @RequestParam(value = "fields", required = false) String fields,
                                            Pageable pageable) {

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(userService.getAllStudents(), User.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @GetMapping("/students/{sourcedId}")
    public ResponseEntity<?> getStudent(@PathVariable String sourcedId,
                                        @RequestParam(value = "fields", required = false) String fields) {

        OneRosterItemResponse response = new OneRosterItemResponse.Builder<>(userService.getStudent(sourcedId))
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/teachers")
    public ResponseEntity<?> getAllTeachers(@RequestParam(value = "filter", required = false) String filter,
                                            @RequestParam(value = "fields", required = false) String fields,
                                            Pageable pageable) {

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(userService.getAllTeachers(), User.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @GetMapping("/teachers/{sourcedId}")
    public ResponseEntity<?> getTeacher(@PathVariable String sourcedId,
                                        @RequestParam(value = "fields", required = false) String fields) {

        OneRosterItemResponse response = new OneRosterItemResponse.Builder<>(userService.getTeacher(sourcedId))
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/administrators")
    public ResponseEntity<?> getAllAdministrators(@RequestParam(value = "filter", required = false) String filter,
                                                  @RequestParam(value = "fields", required = false) String fields,
                                                  Pageable pageable) {

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(userService.getAllAdministrators(), User.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @GetMapping("/administrators/{sourcedId}")
    public ResponseEntity<?> getAdministrator(@PathVariable String sourcedId,
                                              @RequestParam(value = "fields", required = false) String fields) {

        OneRosterItemResponse response = new OneRosterItemResponse.Builder<>(userService.getAdministrator(sourcedId))
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/students/{sourcedId}/classes")
    public ResponseEntity<?> getClazzesForStudent(@PathVariable String sourcedId, Pageable pageable,
                                                  @RequestParam(value = "filter", required = false) String filter,
                                                  @RequestParam(value = "fields", required = false) String fields) {

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(userService.getClazzesForStudent(sourcedId), Clazz.class)
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

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(userService.getClazzesForTeacher(sourcedId), Clazz.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }
}