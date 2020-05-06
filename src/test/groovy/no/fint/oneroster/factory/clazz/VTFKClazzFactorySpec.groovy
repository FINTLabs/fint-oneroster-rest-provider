package no.fint.oneroster.factory.clazz

import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.resource.utdanning.elev.BasisgruppeResource
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource
import no.fint.oneroster.model.AcademicSession
import no.fint.oneroster.model.vocab.ClazzType
import no.fint.oneroster.model.vocab.SessionType
import no.fint.oneroster.util.FintObjectFactory
import org.apache.commons.lang3.StringUtils
import spock.lang.Ignore
import spock.lang.Specification

import java.time.LocalDate
import java.time.Year

class VTFKClazzFactorySpec extends Specification {
    VTFKClazzFactory vtfkClazzFactory = new VTFKClazzFactory()

    def "basisGroupNameConverter() returns modified name"() {
        when:
        def name = vtfkClazzFactory.basisGroupNameConverter(getBasisGroup())

        then:
        name == 'OF-SFV-1SSB-Klasse'
    }

    def "basisGroup() returns class object of type homeroom and with modified identifier as title"() {
        when:
        def clazz = vtfkClazzFactory.basisGroup(getBasisGroup(), FintObjectFactory.newLevel(), FintObjectFactory.newSchool(), [getTerm()])

        then:
        clazz.sourcedId == '1_1SSB_SFVS@38026'
        clazz.title == 'OF-SFV-1SSB-Klasse'
        clazz.classType == ClazzType.HOMEROOM
        clazz.course.sourcedId == 'level-sourced-id'
        clazz.school.sourcedId == 'school-sourced-id'
        clazz.terms.first().sourcedId == 'T1SY20192020'
    }

    def "teachingGroupNameConverter() returns modified name with class"() {
        when:
        def name = vtfkClazzFactory.teachingGroupNameConverter(getTeachingGroup())

        then:
        name == 'OF-GRV-3STG-Kroppsøving'
    }

    @Ignore
    def "teachingGroupNameConverter() returns modified name with class 2"() {
        when:
        def name = vtfkClazzFactory.teachingGroupNameConverter(getTeachingGroup2())

        then:
        name == 'OF-GRV-3STG-Kroppsøving'
    }

    def "teachingGroupNameConverter() returns modified name without class"() {
        when:
        def name = vtfkClazzFactory.teachingGroupNameConverter(getSubjectGroup())

        then:
        name == 'OF-GRV-Norsk hovedmål, skriftlig'
    }

    @Ignore
    def "teachingGroup() returns class object of type schduled and with modified identifier as title"() {
        when:
        def clazz = vtfkClazzFactory.teachingGroup(getTeachingGroup(), FintObjectFactory.newSubject(), FintObjectFactory.newSchool(), [getTerm()])

        then:
        clazz.sourcedId == '1_1SSB_SFVS@38026'
        clazz.title == 'OF-SFV-1SSB-Klasse'
        clazz.classType == ClazzType.SCHEDULED
        clazz.course.sourcedId == 'subject-sourced-id'
        clazz.school.sourcedId == 'school-sourced-id'
        clazz.terms.first().sourcedId == 'T1SY20192020'
    }

    AcademicSession getTerm() {
        return new AcademicSession(
                'T1SY20192020',
                '1 termin 2019/2020',
                LocalDate.of(2019, 8, 1),
                LocalDate.of(2010, 12, 31),
                SessionType.TERM,
                Year.of(2020)
        )
    }

    BasisgruppeResource getBasisGroup() {
        return new BasisgruppeResource(
                systemId: new Identifikator(identifikatorverdi: '1_1SSB_SFVS@38026'),
                navn: 'Basis group')
    }

    UndervisningsgruppeResource getTeachingGroup() {
        return new UndervisningsgruppeResource(
                systemId: new Identifikator(identifikatorverdi: '2_3STG_191KRO1006_GRVS@38034'),
                navn: 'Teaching group',
                beskrivelse: 'Undervisningsgruppa 3STG/191KRO1006 i Kroppsøving ved Greveskogen videregående skole')
    }

    UndervisningsgruppeResource getTeachingGroup2() {
        return new UndervisningsgruppeResource(
                systemId: new Identifikator(identifikatorverdi: '2_19_L-TR2C_183ENG1003_FRVS@38032'),
                navn: 'Teaching group',
                beskrivelse: 'Undervisningsgruppa 19/L-TR2C/183ENG1003 i Engelsk ved Færder videregående skole')
    }

    UndervisningsgruppeResource getSubjectGroup() {
        return new UndervisningsgruppeResource(
                systemId: new Identifikator(identifikatorverdi: '2_E191-NOR1211-12_GRVS@38034'),
                navn: 'Subject group',
                beskrivelse: 'Undervisningsgruppa E191-NOR1211-12 i Norsk hovedmål, skriftlig ved Greveskogen videregående skole)')
    }
}
