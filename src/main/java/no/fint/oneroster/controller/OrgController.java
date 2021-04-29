package no.fint.oneroster.controller;

import no.fint.oneroster.model.*;
import no.fint.oneroster.response.OneRosterCollectionResponse;
import no.fint.oneroster.response.OneRosterItemResponse;
import no.fint.oneroster.service.OrgService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(orgService.getAllOrgs(), Org.class)
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

        OneRosterItemResponse response = new OneRosterItemResponse.Builder<>(orgService.getOrg(sourcedId))
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/schools")
    public ResponseEntity<?> getAllSchools(@RequestParam(value = "filter", required = false) String filter,
                                           @RequestParam(value = "fields", required = false) String fields,
                                           Pageable pageable) {

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(orgService.getAllSchools(), Org.class)
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

        OneRosterItemResponse response = new OneRosterItemResponse.Builder<>(orgService.getSchool(sourcedId))
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/schools/{sourcedId}/classes")
    public ResponseEntity<?> getClazzesForSchool(@PathVariable String sourcedId, Pageable pageable,
                                                 @RequestParam(value = "filter", required = false) String filter,
                                                 @RequestParam(value = "fields", required = false) String fields) {

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(orgService.getClazzesForSchool(sourcedId), Clazz.class)
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

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(orgService.getEnrollmentsForSchool(sourcedId), Enrollment.class)
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

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(orgService.getEnrollmentsForClazzInSchool(schoolSourcedId, clazzSourcedId), Enrollment.class)
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

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(orgService.getStudentsForSchool(sourcedId), User.class)
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

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(orgService.getTeachersForSchool(sourcedId), User.class)
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

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(orgService.getTermsForSchool(sourcedId), AcademicSession.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok()
                .headers(response.getHeaders())
                .body(response.getBody());
    }
}