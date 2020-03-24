package no.fint.oneroster.controller;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.service.ClazzService;
import no.fint.oneroster.util.OneRosterResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
public class ClazzController {

    private final ClazzService clazzService;

    public ClazzController(ClazzService clazzService) {
        this.clazzService = clazzService;
    }

    @GetMapping("/classes")
    public ResponseEntity<?> getAllClazzes(@RequestHeader(defaultValue = "pwf") String orgId, Pageable pageable,
                                           @RequestParam(value = "filter", required = false) String filter,
                                           @RequestParam(value = "fields", required = false) String fields) {
        List<Clazz> clazzes = clazzService.getAllClazzes(orgId);

        List<Clazz> modifiedClazzes = new OneRosterResponse.Builder<>(clazzes)
                .filter(filter)
                .sort(pageable.getSort())
                .page(pageable)
                .build();

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("classes", modifiedClazzes));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(Clazz.class, fields)));

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(clazzes.size()))
                .body(body);
    }

    @GetMapping("/classes/{sourcedId}")
    public ResponseEntity<?> getClazz(@RequestHeader(defaultValue = "pwf") String orgId, @PathVariable String sourcedId,
                                       @RequestParam(value = "fields", required = false) String fields) {
        Clazz clazz = clazzService.getClazz(orgId, sourcedId);

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("class", clazz));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(Clazz.class, fields)));

        return ResponseEntity.ok(body);
    }

    @GetMapping("/schools/{sourcedId}/classes")
    public ResponseEntity<?> getClassesForSchool(@RequestHeader(defaultValue = "pwf") String orgId, Pageable pageable,
                                                 @PathVariable String sourcedId,
                                                 @RequestParam(value = "filter", required = false) String filter,
                                                 @RequestParam(value = "fields", required = false) String fields) {
        List<Clazz> clazzes = clazzService.getClazzesForSchool(orgId, sourcedId);

        List<Clazz> modifiedClazzes = new OneRosterResponse.Builder<>(clazzes)
                .filter(filter)
                .sort(pageable.getSort())
                .page(pageable)
                .build();

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("classes", modifiedClazzes));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(Clazz.class, fields)));

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(clazzes.size()))
                .body(body);
    }
}
