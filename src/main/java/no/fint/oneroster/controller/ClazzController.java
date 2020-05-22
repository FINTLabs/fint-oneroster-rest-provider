package no.fint.oneroster.controller;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.service.ClazzService;
import no.fint.oneroster.util.OneRosterResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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

    @GetMapping("/schools/{sourcedId}/classes")
    public ResponseEntity<?> getClazzesForSchool(@PathVariable String sourcedId, Pageable pageable,
                                                 @RequestParam(value = "filter", required = false) String filter,
                                                 @RequestParam(value = "fields", required = false) String fields) {

        List<Clazz> clazzes = clazzService.getClazzesForSchool(sourcedId);

        OneRosterResponse<Clazz> oneRosterResponse = new OneRosterResponse<>(Clazz.class, "classes")
                .collection(clazzes)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
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
