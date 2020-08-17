package no.fint.oneroster.service.scheduling;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.repository.FintService;
import no.fint.oneroster.repository.OneRosterService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;

@Slf4j
@Service
public class SchedulingService {
    private final FintService fintService;
    private final OneRosterService oneRosterService;

    public SchedulingService(FintService fintService, OneRosterService oneRosterService) {
        this.fintService = fintService;
        this.oneRosterService = oneRosterService;
    }

    @Scheduled(initialDelayString = "${scheduling.initial-delay}", fixedDelayString = "${scheduling.fixed-delay}")
    public void update() {
        try {
            fintService.updateResources();

            if (emptyCaches()) {
                log.warn("One or more empty FINT caches. Update postponed");
                return;
            }

            oneRosterService.updateResources();
        } catch (OAuth2AuthorizationException | WebClientException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private Boolean emptyCaches() {
        return fintService.getSchools().isEmpty() ||
                fintService.getStudents().isEmpty() ||
                fintService.getTeachers().isEmpty() ||
                fintService.getStudentRelations().isEmpty() ||
                fintService.getTeachingRelations().isEmpty() ||
                fintService.getBasisGroups().isEmpty() ||
                fintService.getTeachingGroups().isEmpty() ||
                fintService.getContactTeacherGroups().isEmpty() ||
                fintService.getSubjects().isEmpty() ||
                fintService.getLevels().isEmpty() ||
                fintService.getPersons().isEmpty() ||
                fintService.getPersonnel().isEmpty();
    }
}
