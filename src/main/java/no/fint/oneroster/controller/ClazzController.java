package no.fint.oneroster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.service.ClazzService;
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
@RequestMapping("/classes")
public class ClazzController {

    private final ClazzService clazzService;
    private final ObjectMapper objectMapper;

    public ClazzController(ClazzService clazzService, ObjectMapper objectMapper) {
        this.clazzService = clazzService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public Map<String, List<Clazz>> getAllClazzes(@RequestHeader String orgId, FilterProvider fields, Pageable pageable) {
        List<Clazz> clazzes = clazzService.getAllClazzes(orgId);

        pageable.getSort().get().findFirst().ifPresent(sort -> {
            BeanComparator<Clazz> comparator = new BeanComparator<>(sort.getProperty());
            clazzes.sort(comparator);
        });

        List<Clazz> filteredClazzes = clazzes.stream()
                .skip(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("classes", filteredClazzes);
    }

    @GetMapping("/{sourcedId}")
    public Map<String, Clazz> getClazz(@RequestHeader String orgId, @PathVariable String sourcedId, FilterProvider fields) {
        Clazz clazz = clazzService.getClazz(orgId, sourcedId);

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("class", clazz);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        log.error("WebClientException - Status: {}, Body: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
        return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
    }
}
