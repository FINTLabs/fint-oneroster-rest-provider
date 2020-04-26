package no.fint.oneroster.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.factory.clazz.ClazzFactory;
import no.fint.oneroster.model.AcademicSession;
import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.repository.FintEducationService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClazzService {
    private final FintEducationService fintEducationService;
    private final AcademicSessionService academicSessionService;
    private final ClazzFactory clazzFactory;

    public ClazzService(FintEducationService fintEducationService, AcademicSessionService academicSessionService, ClazzFactory clazzFactory) {
        this.fintEducationService = fintEducationService;
        this.academicSessionService = academicSessionService;
        this.clazzFactory = clazzFactory;
    }

    public List<Clazz> getAllClazzes() {
        List<AcademicSession> terms = academicSessionService.getAllTerms();

        List<Clazz> clazzes = new ArrayList<>();

        fintEducationService.getBasisGroups()
                .values()
                .stream()
                .distinct()
                .forEach(basisGroup -> {
                    Optional<ArstrinnResource> level = basisGroup.getTrinn()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getLevels()::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    Optional<SkoleResource> school = basisGroup.getSkole()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getSchools()::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    if (level.isPresent() && school.isPresent() && !terms.isEmpty()) {
                        clazzes.add(clazzFactory.basisGroup(basisGroup, level.get(), school.get(), terms));
                    }
                });

        fintEducationService.getTeachingGroups()
                .values()
                .stream()
                .distinct()
                .forEach(teachingGroup -> {
                    Optional<FagResource> subject = teachingGroup.getFag()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getSubjects()::get)
                            .filter(Objects::nonNull)
                            .findFirst();

                    Optional<SkoleResource> school = teachingGroup.getSkole()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getSchools()::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    if (subject.isPresent() && school.isPresent() && !terms.isEmpty()) {
                        clazzes.add(clazzFactory.teachingGroup(teachingGroup, subject.get(), school.get(), terms));
                    }
                });

        return clazzes;
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
