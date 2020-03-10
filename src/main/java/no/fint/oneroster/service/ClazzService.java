package no.fint.oneroster.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.factory.ClazzFactory;
import no.fint.oneroster.model.AcademicSession;
import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.repository.FintRepository;
import no.fint.oneroster.util.LinkUtil;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class ClazzService {

    private final FintRepository fintRepository;
    private final AcademicSessionService academicSessionService;

    public ClazzService(FintRepository fintRepository, AcademicSessionService academicSessionService) {
        this.fintRepository = fintRepository;
        this.academicSessionService = academicSessionService;
    }

    public List<Clazz> getAllClazzes(String orgId) {
        Map<String, SkoleResource> schools = fintRepository.getSchools(orgId);

        List<Clazz> clazzes = new ArrayList<>();

        Map<String, ArstrinnResource> levels = fintRepository.getLevels(orgId);
        List<AcademicSession> terms = academicSessionService.getAllTerms(orgId);

        fintRepository.getBasisGroups(orgId)
                .values()
                .forEach(basisGroup -> {
                    Optional<ArstrinnResource> level = basisGroup.getTrinn()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(levels::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    Optional<SkoleResource> school = basisGroup.getSkole()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(schools::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    if (level.isPresent() && school.isPresent() && !terms.isEmpty()) {
                        clazzes.add(ClazzFactory.basisGroup(basisGroup, level.get(), school.get(), terms));
                    }
                });

        Map<String, FagResource> subjects = fintRepository.getSubjects(orgId);

        fintRepository.getTeachingGroups(orgId)
                .values()
                .forEach(teachingGroup -> {
                    Optional<FagResource> subject = teachingGroup.getFag()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(subjects::get)
                            .filter(Objects::nonNull)
                            .findFirst();

                    Optional<SkoleResource> school = teachingGroup.getSkole()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(schools::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    if (subject.isPresent() && school.isPresent() && !terms.isEmpty()) {
                        clazzes.add(ClazzFactory.teachingGroup(teachingGroup, subject.get(), school.get(), terms));
                    }
                });

        return clazzes;
    }

    public Clazz getClazz(String orgId, String sourcedId) {
        return getAllClazzes(orgId).stream()
                .filter(clazz -> clazz.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }
}
