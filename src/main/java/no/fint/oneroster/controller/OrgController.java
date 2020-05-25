package no.fint.oneroster.controller;

import no.fint.oneroster.model.*;
import no.fint.oneroster.service.OrgService;
import no.fint.oneroster.util.OneRosterResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class OrgController {
    private final OrgService orgService;

    public OrgController(OrgService orgService) {
        this.orgService = orgService;
    }

    @GetMapping("/orgs")
    public ResponseEntity<?> getAllOrgs(@RequestParam(value = "filter", required = false) String filter,
                                        @RequestParam(value = "fields", required = false) String fields,
                                        Pageable pageable) {

        List<Org> orgs = orgService.getAllOrgs();

        OneRosterResponse<Org> oneRosterResponse = new OneRosterResponse<>(Org.class, "orgs")
                .collection(orgs)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/orgs/{sourcedId}")
    public ResponseEntity<?> getOrg(@PathVariable String sourcedId,
                                    @RequestParam(value = "fields", required = false) String fields) {

        Org org = orgService.getOrg(sourcedId);

        OneRosterResponse<Org> oneRosterResponse = new OneRosterResponse<>(Org.class, "org")
                .item(org)
                .fieldSelection(fields);

        return ResponseEntity.ok(oneRosterResponse.getBody());
    }

    @GetMapping("/schools")
    public ResponseEntity<?> getAllSchools(@RequestParam(value = "filter", required = false) String filter,
                                           @RequestParam(value = "fields", required = false) String fields,
                                           Pageable pageable) {

        List<Org> schools = orgService.getAllSchools();

        OneRosterResponse<Org> oneRosterResponse = new OneRosterResponse<>(Org.class, "orgs")
                .collection(schools)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/schools/{sourcedId}")
    public ResponseEntity<?> getSchool(@PathVariable String sourcedId,
                                       @RequestParam(value = "fields", required = false) String fields) {

        Org school = orgService.getSchool(sourcedId);

        OneRosterResponse<Org> oneRosterResponse = new OneRosterResponse<>(Org.class, "org")
                .item(school)
                .fieldSelection(fields);

        return ResponseEntity.ok(oneRosterResponse.getBody());
    }

    @GetMapping("/schools/{sourcedId}/classes")
    public ResponseEntity<?> getClazzesForSchool(@PathVariable String sourcedId, Pageable pageable,
                                                 @RequestParam(value = "filter", required = false) String filter,
                                                 @RequestParam(value = "fields", required = false) String fields) {

        List<Clazz> clazzes = orgService.getClazzesForSchool(sourcedId);

        OneRosterResponse<Clazz> oneRosterResponse = new OneRosterResponse<>(Clazz.class, "classes")
                .collection(clazzes)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/schools/{sourcedId}/enrollments")
    public ResponseEntity<?> getEnrollmentsForSchool(@PathVariable String sourcedId, Pageable pageable,
                                                     @RequestParam(value = "filter", required = false) String filter,
                                                     @RequestParam(value = "fields", required = false) String fields) {

        List<Enrollment> enrollments = orgService.getEnrollmentsForSchool(sourcedId);

        OneRosterResponse<Enrollment> oneRosterResponse = new OneRosterResponse<>(Enrollment.class, "enrollments")
                .collection(enrollments)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/schools/{schoolSourcedId}/classes/{clazzSourcedId}/enrollments")
    public ResponseEntity<?> getEnrollmentsForClazzInSchool(@PathVariable String schoolSourcedId, @PathVariable String clazzSourcedId, Pageable pageable,
                                                     @RequestParam(value = "filter", required = false) String filter,
                                                     @RequestParam(value = "fields", required = false) String fields) {

        List<Enrollment> enrollments = orgService.getEnrollmentsForClazzInSchool(schoolSourcedId, clazzSourcedId);

        OneRosterResponse<Enrollment> oneRosterResponse = new OneRosterResponse<>(Enrollment.class, "enrollments")
                .collection(enrollments)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/schools/{sourcedId}/students")
    public ResponseEntity<?> getStudentsForSchool(@PathVariable String sourcedId, Pageable pageable,
                                                  @RequestParam(value = "filter", required = false) String filter,
                                                  @RequestParam(value = "fields", required = false) String fields) {

        List<User> students = orgService.getStudentsForSchool(sourcedId);

        OneRosterResponse<User> oneRosterResponse = new OneRosterResponse<>(User.class, "users")
                .collection(students)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/schools/{sourcedId}/teachers")
    public ResponseEntity<?> getTeachersForSchool(@PathVariable String sourcedId, Pageable pageable,
                                                  @RequestParam(value = "filter", required = false) String filter,
                                                  @RequestParam(value = "fields", required = false) String fields) {

        List<User> teachers = orgService.getTeachersForSchool(sourcedId);

        OneRosterResponse<User> oneRosterResponse = new OneRosterResponse<>(User.class, "users")
                .collection(teachers)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/schools/{sourcedId}/terms")
    public ResponseEntity<?> getTermsForSchool(@PathVariable String sourcedId, Pageable pageable,
                                                  @RequestParam(value = "filter", required = false) String filter,
                                                  @RequestParam(value = "fields", required = false) String fields) {

        List<AcademicSession> terms = orgService.getTermsForSchool(sourcedId);

        OneRosterResponse<AcademicSession> oneRosterResponse = new OneRosterResponse<>(AcademicSession.class, "academicSessions")
                .collection(terms)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }
}