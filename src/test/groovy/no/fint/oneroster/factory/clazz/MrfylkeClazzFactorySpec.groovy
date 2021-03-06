package no.fint.oneroster.factory.clazz

import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.felles.kompleksedatatyper.Periode
import no.fint.model.resource.utdanning.elev.BasisgruppeResource
import no.fint.model.resource.utdanning.timeplan.FagResource
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource
import no.fint.oneroster.model.vocab.ClazzType
import no.fint.oneroster.FintObjectFactory
import spock.lang.Specification

import java.time.LocalDate
import java.time.ZoneId

class MrfylkeClazzFactorySpec extends Specification {
    MrfylkeClazzFactory mrFylkeClazzFactory = new MrfylkeClazzFactory()

    def "basisGroupNameConverter() returns modified name"() {
        when:
        def name = mrFylkeClazzFactory.basisGroupNameConverter(getBasisGroup(), getSchool())

        then:
        name == '1TP2 VMOL'
    }

    def "basisGroup() returns class object of type homeroom and with modified identifier as title"() {
        when:
        def clazz = mrFylkeClazzFactory.basisGroup(getBasisGroup(), getLevel(), getSchool(), [FintObjectFactory.newTerm()])

        then:
        clazz.sourcedId == '1010722'
        clazz.title == '1TP2 VMOL'
        clazz.classType == ClazzType.HOMEROOM
        clazz.course.sourcedId == 'level-sourced-id'
        clazz.school.sourcedId == 'school-sourced-id'
        clazz.terms.first().sourcedId == 'term-sourced-id'
    }

    def "teachingGroupNameConverter() returns modified name"() {
        when:
        def name = mrFylkeClazzFactory.teachingGroupNameConverter(getTeachingGroup(), getSchool(), getSubject())

        then:
        name == 'Kroppsøving 1BA2/KRO1004 VMOL'
    }

    def "teachingGroup() returns class object of type schduled and with modified identifier as title"() {
        when:
        def clazz = mrFylkeClazzFactory.teachingGroup(getTeachingGroup(), getSubject(), getSchool(), [FintObjectFactory.newTerm()])

        then:
        clazz.sourcedId == '6434852'
        clazz.title == 'Kroppsøving 1BA2/KRO1004 VMOL'
        clazz.classType == ClazzType.SCHEDULED
        clazz.course.sourcedId == 'subject-sourced-id'
        clazz.school.sourcedId == 'school-sourced-id'
        clazz.terms.first().sourcedId == 'term-sourced-id'
    }

    def getBasisGroup() {
        return new BasisgruppeResource(
                systemId: new Identifikator(identifikatorverdi: '1010722'),
                periode: [new Periode(start: Date.from(LocalDate.of(2020, 8, 1).atStartOfDay(ZoneId.of('Z')).toInstant()),
                        slutt: Date.from(LocalDate.of(2021, 7, 31).atStartOfDay(ZoneId.of('Z')).toInstant()))],
                navn: '1TP2',
                beskrivelse: '1TP2'
        )
    }

    def getTeachingGroup() {
        return new UndervisningsgruppeResource(
                systemId: new Identifikator(identifikatorverdi: '6434852'),
                periode: [new Periode(start: Date.from(LocalDate.of(2020, 8, 1).atStartOfDay(ZoneId.of('Z')).toInstant()),
                        slutt: Date.from(LocalDate.of(2021, 7, 31).atStartOfDay(ZoneId.of('Z')).toInstant()))],
                navn: '1BA2/KRO1004',
                beskrivelse: '1BA2/KRO1004'
        )
    }

    def getSchool() {
        return new SkoleResource(
                systemId: new Identifikator(identifikatorverdi: 'school-sourced-id'),
                skolenummer: new Identifikator(identifikatorverdi: '15014')
        )
    }

    def getLevel() {
        return new ArstrinnResource(
                systemId: new Identifikator(identifikatorverdi: 'level-sourced-id')
        )
    }

    def getSubject() {
        return new FagResource(
                systemId: new Identifikator(identifikatorverdi: 'subject-sourced-id'),
                navn: 'Kroppsøving'
        )
    }
}
