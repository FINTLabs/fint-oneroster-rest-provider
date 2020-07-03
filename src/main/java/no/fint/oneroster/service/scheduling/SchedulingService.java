package no.fint.oneroster.service.scheduling;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.repository.FintService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SchedulingService {
    private final FintService fintService;

    public SchedulingService(FintService fintService) {
        this.fintService = fintService;
    }

    @Scheduled(initialDelayString = "${scheduling.initial-delay}", fixedDelayString = "${scheduling.fixed-delay}")
    @CacheEvict(value = {"orgs", "clazzes", "courses", "enrollments", "users"}, allEntries = true)
    public void update() {
        fintService.updateResources();
        log.info("Update complete");
    }
}
