package no.fint.oneroster.controller;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.model.AcademicSession;
import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.service.AcademicSessionService;
import no.fint.oneroster.util.OneRosterResponse;
import org.antlr.v4.runtime.tree.ParseTree;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
public class AcademicSessionController {

    private final AcademicSessionService academicSessionService;

    public AcademicSessionController(AcademicSessionService academicSessionService) {
        this.academicSessionService = academicSessionService;
    }

    @GetMapping("/academicSessions")
    public ResponseEntity<?> getAllUsers(@RequestHeader String orgId, ParseTree filter, Pageable pageable,
                                                          @RequestParam(value = "fields", required = false) String fields) {
        List<AcademicSession> academicSessions = academicSessionService.getAllAcademicSessions(orgId);

        List<AcademicSession> modifiedAcademicSessions = new OneRosterResponse.Builder<>(academicSessions)
                .filter(filter)
                .sort(pageable.getSort())
                .page(pageable)
                .build();

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("academicSessions", modifiedAcademicSessions));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(AcademicSession.class, fields)));

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(academicSessions.size()))
                .body(body);
    }

    @GetMapping("/academicSessions/{sourcedId}")
    public ResponseEntity<?> getAcademicSession(@RequestHeader String orgId, @PathVariable String sourcedId,
                                                           @RequestParam(value = "fields", required = false) String fields) {
        AcademicSession academicSession = academicSessionService.getAcademicSession(orgId, sourcedId);

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("academicSession", academicSession));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(Clazz.class, fields)));

        return ResponseEntity.ok(body);
    }

    @GetMapping("/terms")
    public ResponseEntity<?> getAllTerms(@RequestHeader String orgId, ParseTree filter, Pageable pageable,
                                                          @RequestParam(value = "fields", required = false) String fields) {
        List<AcademicSession> terms = academicSessionService.getAllTerms(orgId);

        List<AcademicSession> modifiedAcademicSessions = new OneRosterResponse.Builder<>(terms)
                .filter(filter)
                .sort(pageable.getSort())
                .page(pageable)
                .build();

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("academicSessions", modifiedAcademicSessions));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(AcademicSession.class, fields)));

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(terms.size()))
                .body(body);
    }

    @GetMapping("/terms/{sourcedId}")
    public ResponseEntity<?> getTerm(@RequestHeader String orgId, @PathVariable String sourcedId,
                                                @RequestParam(value = "fields", required = false) String fields) {
        AcademicSession term = academicSessionService.getTerm(orgId, sourcedId);

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("academicSession", term));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(Clazz.class, fields)));

        return ResponseEntity.ok(body);
    }

    @GetMapping("/gradingPeriods")
    public ResponseEntity<?> getAllGradingPeriods(@RequestHeader String orgId, ParseTree filter, Pageable pageable,
                                                                   @RequestParam(value = "fields", required = false) String fields) {
        List<AcademicSession> gradingPeriods = academicSessionService.getAllGradingPeriods(orgId);

        List<AcademicSession> modifiedAcademicSessions = new OneRosterResponse.Builder<>(gradingPeriods)
                .filter(filter)
                .sort(pageable.getSort())
                .page(pageable)
                .build();

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("academicSessions", modifiedAcademicSessions));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(AcademicSession.class, fields)));

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(gradingPeriods.size()))
                .body(body);
    }

    @GetMapping("/gradingPeriods/{sourcedId}")
    public ResponseEntity<?> getGradingPeriod(@RequestHeader String orgId, @PathVariable String sourcedId,
                                                         @RequestParam(value = "fields", required = false) String fields) {
        AcademicSession gradingPeriod = academicSessionService.getGradingPeriod(orgId, sourcedId);

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("academicSession", gradingPeriod));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(AcademicSession.class, fields)));

        return ResponseEntity.ok(body);
    }
}
