package no.fint.oneroster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.filter.FilterEvaluator;
import no.fint.oneroster.model.Org;
import no.fint.oneroster.service.OrgService;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.beanutils.BeanComparator;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class OrgController {

    private final OrgService orgService;
    private final ObjectMapper objectMapper;

    public OrgController(OrgService orgService, ObjectMapper objectMapper) {
        this.orgService = orgService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/orgs")
    public Map<String, List<Org>> getAllOrgs(@RequestHeader String orgId, FilterProvider fields, ParseTree filter, Pageable pageable) {
        List<Org> orgs = orgService.getAllOrgs(orgId);

        pageable.getSort().get().findFirst().ifPresent(sort -> {
            BeanComparator<Org> comparator = new BeanComparator<>(sort.getProperty());
            orgs.sort(comparator);
        });

        List<Org> filteredOrgs = orgs.stream()
                .filter(org -> {
                    if (filter != null) {
                        FilterEvaluator evaluator = new FilterEvaluator(org);
                        return evaluator.visit(filter);
                    }
                    return true;
                })
                .skip(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("orgs", filteredOrgs);
    }

    @GetMapping("/orgs/{sourcedId}")
    public Map<String, Org> getOrg(@RequestHeader String orgId, @PathVariable String sourcedId, FilterProvider fields) {
        Org org = orgService.getOrg(orgId, sourcedId);

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("org", org);
    }

    @GetMapping("/schools")
    public Map<String, List<Org>> getAllSchools(@RequestHeader String orgId, FilterProvider fields, Pageable pageable) {
        List<Org> schools = orgService.getAllSchools(orgId);

        pageable.getSort().get().findFirst().ifPresent(sort -> {
            BeanComparator<Org> comparator = new BeanComparator<>(sort.getProperty());
            schools.sort(comparator);
        });

        List<Org> filteredSchools = orgService.getAllSchools(orgId).stream()
                .skip(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("orgs", filteredSchools);
    }

    @GetMapping("/schools/{sourcedId}")
    public Map<String, Org> getSchool(@RequestHeader String orgId, @PathVariable String sourcedId, FilterProvider fields) {
        Org school = orgService.getSchool(orgId, sourcedId);

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("org", school);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        log.error("WebClientException - Status: {}, Body: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
        return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
    }
}