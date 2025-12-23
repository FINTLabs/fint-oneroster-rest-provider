package no.fint.oneroster.scheduling;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.repository.FintRepository;
import no.fint.oneroster.repository.OneRosterRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientException;

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
                log.warn("One or more empty FINT caches");

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
        return fintRepository.getSchools().isEmpty() ||
                fintRepository.getStudents().isEmpty() ||
                fintRepository.getTeachers().isEmpty() ||
                fintRepository.getStudentRelations().isEmpty() ||
                fintRepository.getTeachingRelations().isEmpty() ||
                fintRepository.getClasses().isEmpty() ||
                fintRepository.getTeachingGroups().isEmpty() ||
                fintRepository.getContactTeacherGroups().isEmpty() ||
                fintRepository.getSubjects().isEmpty() ||
                fintRepository.getLevels().isEmpty() ||
                fintRepository.getPersons().isEmpty() ||
                fintRepository.getPersonnel().isEmpty() ||
                fintRepository.getTerms().isEmpty() ||
                fintRepository.getSchoolYears().isEmpty();
    }
}