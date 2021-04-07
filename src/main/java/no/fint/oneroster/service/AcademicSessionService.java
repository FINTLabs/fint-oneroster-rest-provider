package no.fint.oneroster.service;

import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.AcademicSession;
import no.fint.oneroster.model.vocab.SessionType;
import no.fint.oneroster.repository.OneRosterRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AcademicSessionService {
    private final OneRosterRepository oneRosterRepository;

    public AcademicSessionService(OneRosterRepository oneRosterRepository) {
        this.oneRosterRepository = oneRosterRepository;
    }

    public List<AcademicSession> getAllAcademicSessions() {
        return oneRosterRepository.getAcademicSessions();
    }

    public AcademicSession getAcademicSession(String sourcedId) {
        return Optional.ofNullable(oneRosterRepository.getAcademicSessionById(sourcedId))
                .orElseThrow(NotFoundException::new);
    }

    public List<AcademicSession> getAllTerms() {
        return getAllAcademicSessions()
                .stream()
                .filter(academicSession -> academicSession.getType().equals(SessionType.TERM))
                .collect(Collectors.toList());
    }

    public AcademicSession getTerm(String sourcedId) {
        return Optional.ofNullable(oneRosterRepository.getAcademicSessionById(sourcedId))
                .filter(academicSession -> academicSession.getType().equals(SessionType.TERM))
                .orElseThrow(NotFoundException::new);
    }

    public List<AcademicSession> getAllGradingPeriods() {
        return getAllAcademicSessions()
                .stream()
                .filter(academicSession -> academicSession.getType().equals(SessionType.GRADINGPERIOD))
                .collect(Collectors.toList());
    }

    public AcademicSession getGradingPeriod(String sourcedId) {
        return Optional.ofNullable(oneRosterRepository.getAcademicSessionById(sourcedId))
                .filter(academicSession -> academicSession.getType().equals(SessionType.GRADINGPERIOD))
                .orElseThrow(NotFoundException::new);
    }
}