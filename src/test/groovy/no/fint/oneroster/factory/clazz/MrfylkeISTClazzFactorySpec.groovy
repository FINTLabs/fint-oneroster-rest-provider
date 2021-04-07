package no.fint.oneroster.factory.clazz

import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.felles.kompleksedatatyper.Periode
import no.fint.model.resource.utdanning.elev.BasisgruppeResource
import no.fint.model.resource.utdanning.timeplan.FagResource
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource
import no.fint.oneroster.model.vocab.ClazzType
import no.fint.oneroster.util.FintObjectFactory
import spock.lang.Specification

import java.time.LocalDate
import java.time.ZoneId

class MrfylkeISTClazzFactorySpec extends Specification {
    MrfylkeISTClazzFactory mrFylkeClazzFactory = new MrfylkeISTClazzFactory()

    def "basisGroupNameConverter() returns modified name"() {
        when:
        def name = mrFylkeClazzFactory.basisGroupNameConverter(getBasisGroup(), null)

        then:
        name == '1SSB SFVS'
    }

    def "basisGroup() returns class object of type homeroom and with modified identifier as title"() {
        when:
        def clazz = mrFylkeClazzFactory.basisGroup(getBasisGroup(), getLevel(), getSchool(), [FintObjectFactory.newTerm()])

        then:
        clazz.sourcedId == '1_1SSB_SFVS@38026'
        clazz.title == '1SSB SFVS'
        clazz.classType == ClazzType.HOMEROOM
        clazz.course.sourcedId == 'level-sourced-id'
        clazz.school.sourcedId == 'school-sourced-id'
        clazz.terms.first().sourcedId == 'term-sourced-id'
    }

    def "teachingGroupNameConverter() returns modified name"() {
        when:
        def name = mrFylkeClazzFactory.teachingGroupNameConverter(getTeachingGroup(), null, null)

        then:
        name == 'Kroppsøving 3STG/191KRO1006 GRVS'
    }

    def "teachingGroup() returns class object of type schduled and with modified identifier as title"() {
        when:
        def clazz = mrFylkeClazzFactory.teachingGroup(getTeachingGroup(), getSubject(), getSchool(), [FintObjectFactory.newTerm()])

        then:
        clazz.sourcedId == '2_3STG_191KRO1006_GRVS@38034'
        clazz.title == 'Kroppsøving 3STG/191KRO1006 GRVS'
        clazz.classType == ClazzType.SCHEDULED
        clazz.course.sourcedId == 'subject-sourced-id'
        clazz.school.sourcedId == 'school-sourced-id'
        clazz.terms.first().sourcedId == 'term-sourced-id'
    }

    def getBasisGroup() {
        return new BasisgruppeResource(
                systemId: new Identifikator(identifikatorverdi: '1_1SSB_SFVS@38026'),
                periode: [new Periode(start: Date.from(LocalDate.of(2020, 8, 1).atStartOfDay(ZoneId.of('Z')).toInstant()),
                        slutt: Date.from(LocalDate.of(2021, 7, 31).atStartOfDay(ZoneId.of('Z')).toInstant()))],
                navn: '1SSB',
                beskrivelse: 'Basisgruppe 1SSB ved Sandefjord videregående skole')
    }

    def getTeachingGroup() {
        return new UndervisningsgruppeResource(
                systemId: new Identifikator(identifikatorverdi: '2_3STG_191KRO1006_GRVS@38034'),
                periode: [new Periode(start: Date.from(LocalDate.of(2020, 8, 1).atStartOfDay(ZoneId.of('Z')).toInstant()),
                        slutt: Date.from(LocalDate.of(2021, 7, 31).atStartOfDay(ZoneId.of('Z')).toInstant()))],
                navn: '3STG/191KRO1006',
                beskrivelse: 'Undervisningsgruppa 3STG/191KRO1006 i Kroppsøving ved Greveskogen videregående skole')
    }

    def getSchool() {
        return new SkoleResource(
                systemId: new Identifikator(identifikatorverdi: 'school-sourced-id')
        )
    }

    def getLevel() {
        return new ArstrinnResource(
                systemId: new Identifikator(identifikatorverdi: 'level-sourced-id')
        )
    }

    def getSubject() {
        return new FagResource(
                systemId: new Identifikator(identifikatorverdi: 'subject-sourced-id')
        )
    }
}