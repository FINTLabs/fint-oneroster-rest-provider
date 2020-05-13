package no.fint.oneroster.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.repository.OneRosterService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClazzService {
    private final OneRosterService oneRosterService;

    public ClazzService(OneRosterService oneRosterService) {
        this.oneRosterService = oneRosterService;
    }

    public List<Clazz> getAllClazzes() {
        return oneRosterService.getAllClazzes();
    }

    public Clazz getClazz(String sourcedId) {
        return getAllClazzes()
                .stream()
                .filter(clazz -> clazz.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<Clazz> getClazzesForSchool(String sourcedId) {
        return getAllClazzes()
                .stream()
                .filter(clazz -> clazz.getSchool().getSourcedId().equals(sourcedId))
                .collect(Collectors.toList());
    }
}
