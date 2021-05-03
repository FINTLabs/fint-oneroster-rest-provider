package no.fint.oneroster

import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.felles.kompleksedatatyper.Kontaktinformasjon
import no.fint.model.felles.kompleksedatatyper.Periode
import no.fint.model.felles.kompleksedatatyper.Personnavn
import no.fint.model.resource.Link
import no.fint.model.resource.administrasjon.personal.PersonalressursResource
import no.fint.model.resource.felles.PersonResource
import no.fint.model.resource.utdanning.elev.BasisgruppeResource
import no.fint.model.resource.utdanning.elev.ElevResource
import no.fint.model.resource.utdanning.elev.ElevforholdResource
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource
import no.fint.model.resource.utdanning.elev.SkoleressursResource
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource
import no.fint.model.resource.utdanning.kodeverk.SkolearResource
import no.fint.model.resource.utdanning.kodeverk.TerminResource
import no.fint.model.resource.utdanning.timeplan.FagResource
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource

import java.time.LocalDate
import java.time.ZoneId

class FintObjectFactory {

    static SkoleResource newSchool() {
        SkoleResource resource = new SkoleResource()
        resource.setSystemId(new Identifikator(identifikatorverdi: 'school-sourced-id'))
        resource.setNavn('School')
        resource.setSkolenummer(new Identifikator(identifikatorverdi: 'identifier'))
        resource.setOrganisasjonsnummer(new Identifikator(identifikatorverdi: 'identifier'))
        resource.addBasisgruppe(Link.with('/basis-group-sourced-id'))
        resource.addUndervisningsgruppe(Link.with('/teaching-group-sourced-id'))
        resource.addKontaktlarergruppe(Link.with('/contact-teacher-group-sourced-id'))
        resource.addElevforhold(Link.with('/student-relation-sourced-id'))
        resource.addUndervisningsforhold(Link.with('/teaching-relation-sourced-id'))
        resource.addSelf(Link.with('school-sourced-id'))
        return resource
    }

    static PersonResource newPerson() {
        PersonResource resource = new PersonResource()
        Personnavn name = new Personnavn()
        name.setFornavn('given-name')
        name.setMellomnavn('middle-name')
        name.setEtternavn('family-name')
        resource.setNavn(name)
        resource.setFodselsnummer(new Identifikator(identifikatorverdi: 'identifier'))
        resource.addSelf(Link.with('/person-sourced-id'))

        return resource
    }

    static ElevResource newStudent() {
        ElevResource resource = new ElevResource()
        resource.setSystemId(new Identifikator(identifikatorverdi: 'student-sourced-id'))
        resource.setBrukernavn(new Identifikator(identifikatorverdi: 'username'))
        resource.setElevnummer(new Identifikator(identifikatorverdi: 'identifier'))
        resource.setFeidenavn(new Identifikator(identifikatorverdi: 'feide'))
        Kontaktinformasjon contactInformation = new Kontaktinformasjon()
        contactInformation.setEpostadresse('email')
        contactInformation.setMobiltelefonnummer('sms')
        contactInformation.setTelefonnummer('phone')
        resource.setKontaktinformasjon(contactInformation)
        resource.addPerson(Link.with('/person-sourced-id'))
        resource.addElevforhold(Link.with('/student-relation-sourced-id'))
        resource.addSelf(Link.with('/student-sourced-id'))
        return resource
    }

    static ElevforholdResource newStudentRelation() {
        ElevforholdResource resource = new ElevforholdResource()
        resource.setSystemId(new Identifikator(identifikatorverdi: 'student-relation-sourced-id'))
        resource.addElev(Link.with('/student-sourced-id'))
        resource.addBasisgruppe(Link.with('/basis-group-sourced-id'))
        resource.addUndervisningsgruppe(Link.with('/teaching-group-sourced-id'))
        resource.addSkole(Link.with('/school-sourced-id'))
        resource.addSelf(Link.with('/student-relation-sourced-id'))
        return resource
    }

    static SkoleressursResource newTeacher() {
        SkoleressursResource resource = new SkoleressursResource()
        resource.setSystemId(new Identifikator(identifikatorverdi: 'teacher-sourced-id'))
        resource.setFeidenavn(new Identifikator(identifikatorverdi: 'feide'))
        resource.addPersonalressurs(Link.with('/personnel-resource-sourced-id'))
        resource.addUndervisningsforhold(Link.with('/teaching-relation-sourced-id'))
        resource.addSelf(Link.with('/teacher-sourced-id'))
        return resource
    }

    static PersonalressursResource newPersonnel() {
        PersonalressursResource resource = new PersonalressursResource()
        resource.setBrukernavn(new Identifikator(identifikatorverdi: 'username'))
        resource.setAnsattnummer(new Identifikator(identifikatorverdi: 'identifier'))
        Kontaktinformasjon contactInformation = new Kontaktinformasjon()
        contactInformation.setEpostadresse('email')
        contactInformation.setMobiltelefonnummer('sms')
        contactInformation.setTelefonnummer('phone')
        resource.setKontaktinformasjon(contactInformation)
        resource.addPerson(Link.with('/person-sourced-id'))
        resource.addSelf(Link.with('/personnel-resource-sourced-id'))
        return resource
    }

    static UndervisningsforholdResource newTeachingRelation() {
        UndervisningsforholdResource resource = new UndervisningsforholdResource()
        resource.setSystemId(new Identifikator(identifikatorverdi: 'teaching-relation-sourced-id'))
        resource.addSkoleressurs(Link.with('/teacher-sourced-id'))
        resource.addBasisgruppe(Link.with('/basis-group-sourced-id'))
        resource.addUndervisningsgruppe(Link.with('/teaching-group-sourced-id'))
        resource.addSkole(Link.with('/school-sourced-id'))
        resource.addSelf(Link.with('/teaching-relation-sourced-id'))
        return resource
    }

    static BasisgruppeResource newBasisGroup() {
        BasisgruppeResource resource = new BasisgruppeResource()
        resource.setSystemId(new Identifikator(identifikatorverdi: 'basis-group-sourced-id'))
        resource.setPeriode([new Periode(start: Date.from(LocalDate.of(2020, 8, 1).atStartOfDay(ZoneId.of('Z')).toInstant()),
                slutt: Date.from(LocalDate.of(2030, 7, 31).atStartOfDay(ZoneId.of('Z')).toInstant()))])
        resource.setNavn('Basis group')
        resource.setBeskrivelse('Basis group at school')
        resource.addSkole(Link.with('/school-sourced-id'))
        resource.addTrinn(Link.with('/level-sourced-id'))
        resource.addElevforhold(Link.with('/student-relation-sourced-id'))
        resource.addUndervisningsforhold(Link.with('/teaching-relation-sourced-id'))
        resource.addTermin(Link.with('/term-sourced-id'))
        resource.addSkolear(Link.with('/school-year-sourced-id'))
        resource.addSelf(Link.with('/basis-group-sourced-id'))
        return resource
    }

    static UndervisningsgruppeResource newTeachingGroup() {
        UndervisningsgruppeResource resource = new UndervisningsgruppeResource()
        resource.setSystemId(new Identifikator(identifikatorverdi: 'teaching-group-sourced-id'))
        resource.setPeriode([new Periode(start: Date.from(LocalDate.of(2020, 8, 1).atStartOfDay(ZoneId.of('Z')).toInstant()),
                slutt: Date.from(LocalDate.of(2030, 7, 31).atStartOfDay(ZoneId.of('Z')).toInstant()))])
        resource.setNavn('Teaching group')
        resource.setBeskrivelse('Teaching group at school')
        resource.addSkole(Link.with('/school-sourced-id'))
        resource.addFag(Link.with('/subject-sourced-id'))
        resource.addElevforhold(Link.with('/student-relation-sourced-id'))
        resource.addUndervisningsforhold(Link.with('/teaching-relation-sourced-id'))
        resource.addTermin(Link.with('/term-sourced-id'))
        resource.addSkolear(Link.with('/school-year-sourced-id'))
        resource.addSelf(Link.with('/teaching-group-sourced-id'))
        return resource
    }

    static KontaktlarergruppeResource newContactTeacherGroup() {
        KontaktlarergruppeResource resource = new KontaktlarergruppeResource()
        resource.setSystemId(new Identifikator(identifikatorverdi: 'contact-teacher-group-sourced-id'))
        resource.setPeriode([new Periode(start: Date.from(LocalDate.of(2020, 8, 1).atStartOfDay(ZoneId.of('Z')).toInstant()),
                slutt: Date.from(LocalDate.of(2030, 7, 31).atStartOfDay(ZoneId.of('Z')).toInstant()))])
        resource.setNavn('Contact teacher group')
        resource.setBeskrivelse('Contact teacher group at school')
        resource.addSkole(Link.with('/school-sourced-id'))
        resource.addBasisgruppe(Link.with('/basis-group-sourced-id'))
        resource.addElevforhold(Link.with('/student-relation-sourced-id'))
        resource.addUndervisningsforhold(Link.with('/teaching-relation-sourced-id'))
        resource.addTermin(Link.with('/term-sourced-id'))
        resource.addSkolear(Link.with('/school-year-sourced-id'))
        resource.addSelf(Link.with('/contact-teacher-group-sourced-id'))
        return resource
    }

    static ArstrinnResource newLevel() {
        ArstrinnResource resource = new ArstrinnResource()
        resource.setSystemId(new Identifikator(identifikatorverdi: 'level-sourced-id'))
        resource.setPeriode([])
        resource.setNavn('Level')
        resource.setBeskrivelse('A level')
        resource.addGrepreferanse(Link.with('/grep-level'))
        resource.addSelf(Link.with('/level-sourced-id'))
        return resource
    }

    static FagResource newSubject() {
        FagResource resource = new FagResource()
        resource.setSystemId(new Identifikator(identifikatorverdi: 'subject-sourced-id'))
        resource.setPeriode([])
        resource.setNavn('Subject')
        resource.setBeskrivelse('A subject')
        resource.addGrepreferanse(Link.with('/grep-subject'))
        resource.addSelf(Link.with('/subject-sourced-id'))
        return resource
    }
}