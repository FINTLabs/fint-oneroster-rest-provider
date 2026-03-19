package no.fint.oneroster.scheduling;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.repository.FintRepository;
import no.fint.oneroster.repository.OneRosterRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class ResetAndUpdate {
    private final FintRepository fintRepository;
    private final OneRosterRepository oneRosterRepository;

    public ResetAndUpdate(FintRepository fintRepository, OneRosterRepository oneRosterRepository) {
        this.fintRepository = fintRepository;
        this.oneRosterRepository = oneRosterRepository;
    }

    @Scheduled(cron = "${scheduling.cron}")
    public void reset() {
        log.info("Reset");

        fintRepository.reset();
    }

    @Scheduled(initialDelayString = "${scheduling.initial-delay}", fixedDelayString = "${scheduling.fixed-delay}")
    public void update() {
        try {
            fintRepository.update();

            if (emptyCaches()) {
                fintRepository.reset();
                return;
            }

            oneRosterRepository.update();
        } catch (OAuth2AuthorizationException | WebClientException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            log.info("{} orgs, {} users, {} classes, {} courses, {} enrollments, {} academicSessions",
                    oneRosterRepository.getOrgs().size(),
                    oneRosterRepository.getUsers().size(),
                    oneRosterRepository.getClazzes().size(),
                    oneRosterRepository.getCourses().size(),
                    oneRosterRepository.getEnrollments().size(),
                    oneRosterRepository.getAcademicSessions().size());
        }
    }

    private boolean emptyCaches() {
        Map<String, Integer> cacheSizes = new LinkedHashMap<>();
        cacheSizes.put("schools", fintRepository.getSchools().size());
        cacheSizes.put("students", fintRepository.getStudents().size());
        cacheSizes.put("teachers", fintRepository.getTeachers().size());
        cacheSizes.put("studentRelations", fintRepository.getStudentRelations().size());
        cacheSizes.put("teachingRelations", fintRepository.getTeachingRelations().size());
        cacheSizes.put("classes", fintRepository.getClasses().size());
        cacheSizes.put("teachingGroups", fintRepository.getTeachingGroups().size());
        cacheSizes.put("contactTeacherGroups", fintRepository.getContactTeacherGroups().size());
        cacheSizes.put("subjects", fintRepository.getSubjects().size());
        cacheSizes.put("levels", fintRepository.getLevels().size());
        cacheSizes.put("persons", fintRepository.getPersons().size());
        cacheSizes.put("personnel", fintRepository.getPersonnel().size());
        cacheSizes.put("terms", fintRepository.getTerms().size());
        cacheSizes.put("schoolYears", fintRepository.getSchoolYears().size());

        log.debug("FINT cache sizes: {}", cacheSizes);

        boolean empty = false;

        for (Map.Entry<String, Integer> entry : cacheSizes.entrySet()) {
            if (entry.getValue() == 0) {
                log.error("FINT cache is empty: {}", entry.getKey());
                empty = true;
            }
        }

        return empty;
    }
}