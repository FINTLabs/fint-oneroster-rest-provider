package no.fint.oneroster.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.repository.OneRosterRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClazzService {
    private final OneRosterRepository oneRosterRepository;

    public ClazzService(OneRosterRepository oneRosterRepository) {
        this.oneRosterRepository = oneRosterRepository;
    }

    public List<Clazz> getAllClazzes() {
        return oneRosterRepository.getAllClazzes();
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
