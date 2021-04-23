package no.fint.oneroster.controller;

import no.fint.oneroster.model.*;
import no.fint.oneroster.response.OneRosterCollectionResponse;
import no.fint.oneroster.response.OneRosterItemResponse;
import no.fint.oneroster.service.OrgService;
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

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(orgs, Org.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok()
                .headers(response.getHeaders())
                .body(response.getBody());
    }

    @GetMapping("/orgs/{sourcedId}")
    public ResponseEntity<?> getOrg(@PathVariable String sourcedId,
                                    @RequestParam(value = "fields", required = false) String fields) {

        Org org = orgService.getOrg(sourcedId);

        OneRosterItemResponse response = new OneRosterItemResponse.Builder<>(org)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/schools")
    public ResponseEntity<?> getAllSchools(@RequestParam(value = "filter", required = false) String filter,
                                           @RequestParam(value = "fields", required = false) String fields,
                                           Pageable pageable) {

        List<Org> schools = orgService.getAllSchools();

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(schools, Org.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok()
                .headers(response.getHeaders())
                .body(response.getBody());
    }

    @GetMapping("/schools/{sourcedId}")
    public ResponseEntity<?> getSchool(@PathVariable String sourcedId,
                                       @RequestParam(value = "fields", required = false) String fields) {

        Org school = orgService.getSchool(sourcedId);

        OneRosterItemResponse response = new OneRosterItemResponse.Builder<>(school)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/schools/{sourcedId}/classes")
    public ResponseEntity<?> getClazzesForSchool(@PathVariable String sourcedId, Pageable pageable,
                                                 @RequestParam(value = "filter", required = false) String filter,
                                                 @RequestParam(value = "fields", required = false) String fields) {

        List<Clazz> clazzes = orgService.getClazzesForSchool(sourcedId);

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(clazzes, Clazz.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok()
                .headers(response.getHeaders())
                .body(response.getBody());
    }

    @GetMapping("/schools/{sourcedId}/enrollments")
    public ResponseEntity<?> getEnrollmentsForSchool(@PathVariable String sourcedId, Pageable pageable,
                                                     @RequestParam(value = "filter", required = false) String filter,
                                                     @RequestParam(value = "fields", required = false) String fields) {

        List<Enrollment> enrollments = orgService.getEnrollmentsForSchool(sourcedId);

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(enrollments, Enrollment.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok()
                .headers(response.getHeaders())
                .body(response.getBody());
    }

    @GetMapping("/schools/{schoolSourcedId}/classes/{clazzSourcedId}/enrollments")
    public ResponseEntity<?> getEnrollmentsForClazzInSchool(@PathVariable String schoolSourcedId, @PathVariable String clazzSourcedId, Pageable pageable,
                                                     @RequestParam(value = "filter", required = false) String filter,
                                                     @RequestParam(value = "fields", required = false) String fields) {

        List<Enrollment> enrollments = orgService.getEnrollmentsForClazzInSchool(schoolSourcedId, clazzSourcedId);

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(enrollments, Enrollment.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok()
                .headers(response.getHeaders())
                .body(response.getBody());
    }

    @GetMapping("/schools/{sourcedId}/students")
    public ResponseEntity<?> getStudentsForSchool(@PathVariable String sourcedId, Pageable pageable,
                                                  @RequestParam(value = "filter", required = false) String filter,
                                                  @RequestParam(value = "fields", required = false) String fields) {

        List<User> students = orgService.getStudentsForSchool(sourcedId);

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(students, User.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok()
                .headers(response.getHeaders())
                .body(response.getBody());
    }

    @GetMapping("/schools/{sourcedId}/teachers")
    public ResponseEntity<?> getTeachersForSchool(@PathVariable String sourcedId, Pageable pageable,
                                                  @RequestParam(value = "filter", required = false) String filter,
                                                  @RequestParam(value = "fields", required = false) String fields) {

        List<User> teachers = orgService.getTeachersForSchool(sourcedId);

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(teachers, User.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok()
                .headers(response.getHeaders())
                .body(response.getBody());
    }

    @GetMapping("/schools/{sourcedId}/terms")
    public ResponseEntity<?> getTermsForSchool(@PathVariable String sourcedId, Pageable pageable,
                                                  @RequestParam(value = "filter", required = false) String filter,
                                                  @RequestParam(value = "fields", required = false) String fields) {

        List<AcademicSession> terms = orgService.getTermsForSchool(sourcedId);

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(terms, AcademicSession.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok()
                .headers(response.getHeaders())
                .body(response.getBody());
    }
}