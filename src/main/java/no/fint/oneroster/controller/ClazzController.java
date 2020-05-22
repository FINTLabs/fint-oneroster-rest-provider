package no.fint.oneroster.controller;

import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.model.User;
import no.fint.oneroster.service.ClazzService;
import no.fint.oneroster.util.OneRosterResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClazzController {
    private final ClazzService clazzService;

    public ClazzController(ClazzService clazzService) {
        this.clazzService = clazzService;
    }

    @GetMapping("/classes")
    public ResponseEntity<?> getAllClazzes(@RequestParam(value = "filter", required = false) String filter,
                                           @RequestParam(value = "fields", required = false) String fields,
                                           Pageable pageable) {

        List<Clazz> clazzes = clazzService.getAllClazzes();

        OneRosterResponse<Clazz> oneRosterResponse = new OneRosterResponse<>(Clazz.class, "classes")
                .collection(clazzes)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/classes/{sourcedId}")
    public ResponseEntity<?> getClazz(@PathVariable String sourcedId,
                                      @RequestParam(value = "fields", required = false) String fields) {

        Clazz clazz = clazzService.getClazz(sourcedId);

        OneRosterResponse<Clazz> oneRosterResponse = new OneRosterResponse<>(Clazz.class, "class")
                .item(clazz)
                .fieldSelection(fields);

        return ResponseEntity.ok(oneRosterResponse.getBody());
    }

    @GetMapping("/classes/{sourcedId}/students")
    public ResponseEntity<?> getStudentsForClazz(@PathVariable String sourcedId, Pageable pageable,
                                                 @RequestParam(value = "filter", required = false) String filter,
                                                 @RequestParam(value = "fields", required = false) String fields) {

        List<User> students = clazzService.getStudentsForClazz(sourcedId);

        OneRosterResponse<User> oneRosterResponse = new OneRosterResponse<>(User.class, "users")
                .collection(students)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/classes/{sourcedId}/teachers")
    public ResponseEntity<?> getTeachersForClazz(@PathVariable String sourcedId, Pageable pageable,
                                                 @RequestParam(value = "filter", required = false) String filter,
                                                 @RequestParam(value = "fields", required = false) String fields) {

        List<User> teachers = clazzService.getTeachersForClazz(sourcedId);

        OneRosterResponse<User> oneRosterResponse = new OneRosterResponse<>(User.class, "users")
                .collection(teachers)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }
}
