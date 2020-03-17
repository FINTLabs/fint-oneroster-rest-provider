package no.fint.oneroster.controller;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.model.Org;
import no.fint.oneroster.service.OrgService;
import no.fint.oneroster.util.OneRosterResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
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
    public ResponseEntity<?> getAllOrgs(@RequestHeader String orgId, Pageable pageable,
                                        @RequestParam(value = "filter", required = false) String filter,
                                        @RequestParam(value = "fields", required = false) String fields) {
        List<Org> orgs = orgService.getAllOrgs(orgId);

        List<Org> modifiedOrgs = new OneRosterResponse.Builder<>(orgs)
                .filter(filter)
                .sort(pageable.getSort())
                .page(pageable)
                .build();

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("orgs", modifiedOrgs));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(Org.class, fields)));

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(orgs.size()))
                .body(body);
    }

    @GetMapping("/orgs/{sourcedId}")
    public ResponseEntity<?> getOrg(@RequestHeader String orgId, @PathVariable String sourcedId,
                                    @RequestParam(value = "fields", required = false) String fields) {
        Org org = orgService.getOrg(orgId, sourcedId);

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("org", org));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(Org.class, fields)));

        return ResponseEntity.ok(body);
    }

    @GetMapping("/schools")
    public ResponseEntity<?> getAllSchools(@RequestHeader String orgId, Pageable pageable,
                                           @RequestParam(value = "filter", required = false) String filter,
                                           @RequestParam(value = "fields", required = false) String fields) {
        List<Org> schools = orgService.getAllSchools(orgId);

        List<Org> modifiedSchools = new OneRosterResponse.Builder<>(schools)
                .filter(filter)
                .sort(pageable.getSort())
                .page(pageable)
                .build();

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("orgs", modifiedSchools));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(Org.class, fields)));

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(schools.size()))
                .body(body);
    }

    @GetMapping("/schools/{sourcedId}")
    public ResponseEntity<?> getSchool(@RequestHeader String orgId, @PathVariable String sourcedId,
                                       @RequestParam(value = "fields", required = false) String fields) {
        Org school = orgService.getSchool(orgId, sourcedId);

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("org", school));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(Org.class, fields)));

        return ResponseEntity.ok(body);
    }
}