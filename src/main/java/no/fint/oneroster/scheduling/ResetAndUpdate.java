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
        cacheSizes.put("skole", fintRepository.getSchools().size());
        cacheSizes.put("elev", fintRepository.getStudents().size());
        cacheSizes.put("skoleressurs", fintRepository.getTeachers().size());
        cacheSizes.put("elevforhold", fintRepository.getStudentRelations().size());
        cacheSizes.put("undervisningsforhold", fintRepository.getTeachingRelations().size());
        cacheSizes.put("klasse", fintRepository.getClasses().size());
        cacheSizes.put("undervisningsgruppe", fintRepository.getTeachingGroups().size());
        cacheSizes.put("kontaktlarergruppe", fintRepository.getContactTeacherGroups().size());
        cacheSizes.put("fag", fintRepository.getSubjects().size());
        cacheSizes.put("arstrinn", fintRepository.getLevels().size());
        cacheSizes.put("person", fintRepository.getPersons().size());
        cacheSizes.put("personalressurs", fintRepository.getPersonnel().size());
        cacheSizes.put("termin", fintRepository.getTerms().size());
        cacheSizes.put("skolear", fintRepository.getSchoolYears().size());

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