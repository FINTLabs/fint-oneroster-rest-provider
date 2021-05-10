package no.fint.oneroster.controller;

import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.model.User;
import no.fint.oneroster.response.OneRosterCollectionResponse;
import no.fint.oneroster.response.OneRosterItemResponse;
import no.fint.oneroster.service.ClazzService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/classes")
public class ClazzController {
    private final ClazzService clazzService;

    public ClazzController(ClazzService clazzService) {
        this.clazzService = clazzService;
    }

    @GetMapping
    public ResponseEntity<?> getAllClazzes(@RequestParam(value = "filter", required = false) String filter,
                                           @RequestParam(value = "fields", required = false) String fields,
                                           Pageable pageable) {

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(clazzService.getAllClazzes(), Clazz.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @GetMapping("/{sourcedId}")
    public ResponseEntity<?> getClazz(@PathVariable String sourcedId,
                                      @RequestParam(value = "fields", required = false) String fields) {

        OneRosterItemResponse response = new OneRosterItemResponse.Builder<>(clazzService.getClazz(sourcedId))
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/{sourcedId}/students")
    public ResponseEntity<?> getStudentsForClazz(@PathVariable String sourcedId, Pageable pageable,
                                                 @RequestParam(value = "filter", required = false) String filter,
                                                 @RequestParam(value = "fields", required = false) String fields) {

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(clazzService.getStudentsForClazz(sourcedId), User.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @GetMapping("/{sourcedId}/teachers")
    public ResponseEntity<?> getTeachersForClazz(@PathVariable String sourcedId, Pageable pageable,
                                                 @RequestParam(value = "filter", required = false) String filter,
                                                 @RequestParam(value = "fields", required = false) String fields) {

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(clazzService.getTeachersForClazz(sourcedId), User.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }
}
