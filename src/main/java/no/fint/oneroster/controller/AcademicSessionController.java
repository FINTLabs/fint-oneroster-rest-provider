package no.fint.oneroster.controller;

import no.fint.oneroster.model.AcademicSession;
import no.fint.oneroster.response.OneRosterCollectionResponse;
import no.fint.oneroster.response.OneRosterItemResponse;
import no.fint.oneroster.service.AcademicSessionService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AcademicSessionController {
    private final AcademicSessionService academicSessionService;

    public AcademicSessionController(AcademicSessionService academicSessionService) {
        this.academicSessionService = academicSessionService;
    }

    @GetMapping("/academicSessions")
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "filter", required = false) String filter,
                                         @RequestParam(value = "fields", required = false) String fields,
                                         Pageable pageable) {

        List<AcademicSession> academicSessions = academicSessionService.getAllAcademicSessions();

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(academicSessions, AcademicSession.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @GetMapping("/academicSessions/{sourcedId}")
    public ResponseEntity<?> getAcademicSession(@PathVariable String sourcedId,
                                                @RequestParam(value = "fields", required = false) String fields) {

        AcademicSession academicSession = academicSessionService.getAcademicSession(sourcedId);

        OneRosterItemResponse response = new OneRosterItemResponse.Builder<>(academicSession)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/terms")
    public ResponseEntity<?> getAllTerms(@RequestParam(value = "filter", required = false) String filter,
                                         @RequestParam(value = "fields", required = false) String fields,
                                         Pageable pageable) {

        List<AcademicSession> terms = academicSessionService.getAllTerms();

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(terms, AcademicSession.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @GetMapping("/terms/{sourcedId}")
    public ResponseEntity<?> getTerm(@PathVariable String sourcedId,
                                     @RequestParam(value = "fields", required = false) String fields) {

        AcademicSession term = academicSessionService.getTerm(sourcedId);

        OneRosterItemResponse response = new OneRosterItemResponse.Builder<>(term)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/gradingPeriods")
    public ResponseEntity<?> getAllGradingPeriods(@RequestParam(value = "filter", required = false) String filter,
                                                  @RequestParam(value = "fields", required = false) String fields,
                                                  Pageable pageable) {

        List<AcademicSession> gradingPeriods = academicSessionService.getAllGradingPeriods();

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(gradingPeriods, AcademicSession.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @GetMapping("/gradingPeriods/{sourcedId}")
    public ResponseEntity<?> getGradingPeriod(@PathVariable String sourcedId,
                                              @RequestParam(value = "fields", required = false) String fields) {

        AcademicSession gradingPeriod = academicSessionService.getGradingPeriod(sourcedId);

        OneRosterItemResponse response = new OneRosterItemResponse.Builder<>(gradingPeriod)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok(response.getBody());
    }
}
