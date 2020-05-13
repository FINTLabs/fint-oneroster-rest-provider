package no.fint.oneroster.service.scheduling;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.repository.FintAdministrationService;
import no.fint.oneroster.repository.FintEducationService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SchedulingService {
    private final FintEducationService fintEducationService;
    private final FintAdministrationService fintAdministrationService;

    public SchedulingService(FintEducationService fintEducationService, FintAdministrationService fintAdministrationService) {
        this.fintEducationService = fintEducationService;
        this.fintAdministrationService = fintAdministrationService;
    }

    @Scheduled(initialDelayString = "${scheduling.initial-delay}", fixedDelayString = "${scheduling.fixed-delay}")
    @CacheEvict(value = {"orgs", "clazzes", "courses", "enrollments", "users"}, allEntries = true)
    public void update() {
        fintEducationService.updateSchools();
        fintEducationService.updatePersons();
        fintEducationService.updateStudents();
        fintEducationService.updateStudentRelations();
        fintEducationService.updateTeachers();
        fintEducationService.updateTeachingRelations();
        fintEducationService.updateBasisGroups();
        fintEducationService.updateTeachingGroups();
        fintEducationService.updateLevels();
        fintEducationService.updateSubjects();
        fintAdministrationService.updatePersons();
        fintAdministrationService.updatePersonnel();
        log.info("Update complete");
    }
}
