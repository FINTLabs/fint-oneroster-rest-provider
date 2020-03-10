package no.fint.oneroster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.model.AcademicSession;
import no.fint.oneroster.service.AcademicSessionService;
import org.apache.commons.beanutils.BeanComparator;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class AcademicSessionController {

    private final AcademicSessionService academicSessionService;
    private final ObjectMapper objectMapper;

    public AcademicSessionController(AcademicSessionService academicSessionService, ObjectMapper objectMapper) {
        this.academicSessionService = academicSessionService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/academicSessions")
    public Map<String, List<AcademicSession>> getAllUsers(@RequestHeader String orgId, FilterProvider fields, Pageable pageable) {
        List<AcademicSession> academicSessions = academicSessionService.getAllAcademicSessions(orgId);

        pageable.getSort().get().findFirst().ifPresent(sort -> {
            BeanComparator<AcademicSession> comparator = new BeanComparator<>(sort.getProperty());
            academicSessions.sort(comparator);
        });

        List<AcademicSession> filteredAcademicSession = academicSessions.stream()
                .skip(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("academicSessions", filteredAcademicSession);
    }

    @GetMapping("/academicSessions/{sourcedId}")
    public Map<String, AcademicSession> getAcademicSession(@RequestHeader String orgId, @PathVariable String sourcedId, FilterProvider fields) {
        AcademicSession academicSession = academicSessionService.getAcademicSession(orgId, sourcedId);

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("academicSession", academicSession);
    }

    @GetMapping("/terms")
    public Map<String, List<AcademicSession>> getAllTerms(@RequestHeader String orgId, FilterProvider fields, Pageable pageable) {
        List<AcademicSession> terms = academicSessionService.getAllTerms(orgId);

        pageable.getSort().get().findFirst().ifPresent(sort -> {
            BeanComparator<AcademicSession> comparator = new BeanComparator<>(sort.getProperty());
            terms.sort(comparator);
        });

        List<AcademicSession> filteredTerms = terms.stream()
                .skip(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("academicSessions", filteredTerms);
    }

    @GetMapping("/terms/{sourcedId}")
    public Map<String, AcademicSession> getTerm(@RequestHeader String orgId, @PathVariable String sourcedId, FilterProvider fields) {
        AcademicSession term = academicSessionService.getTerm(orgId, sourcedId);

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("academicSession", term);
    }

    @GetMapping("/gradingPeriods")
    public Map<String, List<AcademicSession>> getAllGradingPeriods(@RequestHeader String orgId, FilterProvider fields, Pageable pageable) {
        List<AcademicSession> gradingPeriods = academicSessionService.getAllGradingPeriods(orgId);

        pageable.getSort().get().findFirst().ifPresent(sort -> {
            BeanComparator<AcademicSession> comparator = new BeanComparator<>(sort.getProperty());
            gradingPeriods.sort(comparator);
        });

        List<AcademicSession> filteredGradingPeriods = gradingPeriods.stream()
                .skip(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("academicSessions", filteredGradingPeriods);
    }

    @GetMapping("/gradingPeriods/{sourcedId}")
    public Map<String, AcademicSession> getGradingPeriod(@RequestHeader String orgId, @PathVariable String sourcedId, FilterProvider fields) {
        AcademicSession gradingPeriod = academicSessionService.getGradingPeriod(orgId, sourcedId);

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("academicSession", gradingPeriod);
    }
}
