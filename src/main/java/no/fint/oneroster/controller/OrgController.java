package no.fint.oneroster.controller;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.model.Org;
import no.fint.oneroster.service.OrgService;
import no.fint.oneroster.util.OneRosterResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
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
}