package no.fint.oneroster.controller;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.model.AcademicSession;
import no.fint.oneroster.service.AcademicSessionService;
import no.fint.oneroster.util.OneRosterResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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

        OneRosterResponse<AcademicSession> oneRosterResponse = new OneRosterResponse<>(AcademicSession.class, "academicSessions")
                .collection(academicSessions)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/academicSessions/{sourcedId}")
    public ResponseEntity<?> getAcademicSession(@PathVariable String sourcedId,
                                                @RequestParam(value = "fields", required = false) String fields) {

        AcademicSession academicSession = academicSessionService.getAcademicSession(sourcedId);

        OneRosterResponse<AcademicSession> oneRosterResponse = new OneRosterResponse<>(AcademicSession.class, "academicSession")
                .item(academicSession)
                .fieldSelection(fields);

        return ResponseEntity.ok(oneRosterResponse.getBody());
    }

    @GetMapping("/terms")
    public ResponseEntity<?> getAllTerms(@RequestParam(value = "filter", required = false) String filter,
                                         @RequestParam(value = "fields", required = false) String fields,
                                         Pageable pageable) {

        List<AcademicSession> terms = academicSessionService.getAllTerms();

        OneRosterResponse<AcademicSession> oneRosterResponse = new OneRosterResponse<>(AcademicSession.class, "academicSessions")
                .collection(terms)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/terms/{sourcedId}")
    public ResponseEntity<?> getTerm(@PathVariable String sourcedId,
                                     @RequestParam(value = "fields", required = false) String fields) {

        AcademicSession term = academicSessionService.getTerm(sourcedId);

        OneRosterResponse<AcademicSession> oneRosterResponse = new OneRosterResponse<>(AcademicSession.class, "academicSession")
                .item(term)
                .fieldSelection(fields);

        return ResponseEntity.ok(oneRosterResponse.getBody());
    }

    @GetMapping("/gradingPeriods")
    public ResponseEntity<?> getAllGradingPeriods(@RequestParam(value = "filter", required = false) String filter,
                                                  @RequestParam(value = "fields", required = false) String fields,
                                                  Pageable pageable) {

        List<AcademicSession> gradingPeriods = academicSessionService.getAllGradingPeriods();

        OneRosterResponse<AcademicSession> oneRosterResponse = new OneRosterResponse<>(AcademicSession.class, "academicSessions")
                .collection(gradingPeriods)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/gradingPeriods/{sourcedId}")
    public ResponseEntity<?> getGradingPeriod(@PathVariable String sourcedId,
                                              @RequestParam(value = "fields", required = false) String fields) {

        AcademicSession gradingPeriod = academicSessionService.getGradingPeriod(sourcedId);

        OneRosterResponse<AcademicSession> oneRosterResponse = new OneRosterResponse<>(AcademicSession.class, "academicSession")
                .item(gradingPeriod)
                .fieldSelection(fields);

        return ResponseEntity.ok(oneRosterResponse.getBody());
    }
}
