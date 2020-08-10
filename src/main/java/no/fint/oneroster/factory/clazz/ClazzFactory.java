package no.fint.oneroster.factory.clazz;

import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.utdanning.basisklasser.Gruppe;
import no.fint.oneroster.model.AcademicSession;
import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.vocab.ClazzType;
import no.fint.oneroster.model.vocab.GUIDType;
import no.fint.oneroster.model.vocab.StatusType;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.fint.oneroster.util.StringNormalizer.normalize;

public interface ClazzFactory {
    default Clazz basisGroup(BasisgruppeResource basisgruppeResource, ArstrinnResource arstrinnResource, SkoleResource skoleResource, List<AcademicSession> terms) {
        Clazz basisGroup = new Clazz(
                normalize(basisgruppeResource.getSystemId().getIdentifikatorverdi()),
                getStatusType(basisgruppeResource),
                basisGroupNameConverter(basisgruppeResource),
                ClazzType.HOMEROOM,
                GUIDRef.of(GUIDType.COURSE, normalize(arstrinnResource.getSystemId().getIdentifikatorverdi())),
                GUIDRef.of(GUIDType.ORG, normalize(skoleResource.getSystemId().getIdentifikatorverdi())),
                terms.stream()
                        .map(term -> GUIDRef.of(GUIDType.ACADEMICSESSION, term.getSourcedId()))
                        .collect(Collectors.toList()));

        arstrinnResource.getGrepreferanse()
                .stream()
                .map(Link::getHref)
                .findAny()
                .ifPresent(href -> basisGroup.setSubjectCodes(Collections.singletonList(href)));

        return basisGroup;
    }

    default Clazz teachingGroup(UndervisningsgruppeResource undervisningsgruppeResource, FagResource fagResource, SkoleResource skoleResource, List<AcademicSession> terms) {
        Clazz teachingGroup = new Clazz(
                normalize(undervisningsgruppeResource.getSystemId().getIdentifikatorverdi()),
                getStatusType(undervisningsgruppeResource),
                teachingGroupNameConverter(undervisningsgruppeResource),
                ClazzType.SCHEDULED,
                GUIDRef.of(GUIDType.COURSE, normalize(fagResource.getSystemId().getIdentifikatorverdi())),
                GUIDRef.of(GUIDType.ORG, normalize(skoleResource.getSystemId().getIdentifikatorverdi())),
                terms.stream()
                        .map(term -> GUIDRef.of(GUIDType.ACADEMICSESSION, term.getSourcedId()))
                        .collect(Collectors.toList()));

        if (!fagResource.getVigoreferanse().isEmpty() || !fagResource.getGrepreferanse().isEmpty()) {
            teachingGroup.setSubjectCodes(new ArrayList<>());

            Stream.of(fagResource.getVigoreferanse(), fagResource.getGrepreferanse())
                    .flatMap(List::stream)
                    .map(Link::getHref)
                    .forEach(teachingGroup.getSubjectCodes()::add);
        }

        return teachingGroup;
    }

    default Clazz contactTeacherGroup(KontaktlarergruppeResource kontaktlarergruppeResource, ArstrinnResource arstrinnResource, SkoleResource skoleResource, List<AcademicSession> terms) {
        Clazz contactTeacherGroup = new Clazz(
                normalize(kontaktlarergruppeResource.getSystemId().getIdentifikatorverdi()),
                getStatusType(kontaktlarergruppeResource),
                contactTeacherGroupNameConverter(kontaktlarergruppeResource),
                ClazzType.HOMEROOM,
                GUIDRef.of(GUIDType.COURSE, normalize(arstrinnResource.getSystemId().getIdentifikatorverdi())),
                GUIDRef.of(GUIDType.ORG, normalize(skoleResource.getSystemId().getIdentifikatorverdi())),
                terms.stream()
                        .map(term -> GUIDRef.of(GUIDType.ACADEMICSESSION, term.getSourcedId()))
                        .collect(Collectors.toList()));

        arstrinnResource.getGrepreferanse()
                .stream()
                .map(Link::getHref)
                .findAny()
                .ifPresent(href -> contactTeacherGroup.setSubjectCodes(Collections.singletonList(href)));

        contactTeacherGroup.setMetadata(Collections.singletonMap("additionalClassType", "mentor"));

        return contactTeacherGroup;
    }

    default String basisGroupNameConverter(Gruppe basisGroup) {
        return basisGroup.getNavn();
    }

    default String teachingGroupNameConverter(Gruppe teachingGroup) {
        return teachingGroup.getNavn();
    }

    default String contactTeacherGroupNameConverter(Gruppe contactTeacherGroup) {
        return contactTeacherGroup.getNavn();
    }

    default StatusType getStatusType(Gruppe group) {
        List<Periode> period = group.getPeriode();

        if (period.isEmpty()) return StatusType.ACTIVE;

        boolean active = period.stream()
                .findFirst()
                .filter(begin -> {
                    if (begin.getStart() == null) return false;
                    return begin.getStart().compareTo(Date.from(ZonedDateTime.now().toInstant())) <= 0;
                })
                .filter(end -> {
                    if (end.getSlutt() == null) return true;
                    return end.getSlutt().compareTo(Date.from(ZonedDateTime.now().toInstant())) >= 0;
                }).isPresent();

        return active ? StatusType.ACTIVE : StatusType.TOBEDELETED;
    }
}
