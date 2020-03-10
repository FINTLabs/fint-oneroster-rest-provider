package no.fint.oneroster.util

import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.felles.kompleksedatatyper.Kontaktinformasjon
import no.fint.model.felles.kompleksedatatyper.Personnavn
import no.fint.model.resource.Link
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource
import no.fint.model.resource.administrasjon.personal.PersonalressursResource
import no.fint.model.resource.felles.PersonResource
import no.fint.model.resource.utdanning.elev.BasisgruppeResource
import no.fint.model.resource.utdanning.elev.ElevResource
import no.fint.model.resource.utdanning.elev.ElevforholdResource
import no.fint.model.resource.utdanning.elev.SkoleressursResource
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource
import no.fint.model.resource.utdanning.timeplan.FagResource
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource

class FintObjectFactory {

    static OrganisasjonselementResource newSchoolOwner() {
        OrganisasjonselementResource resource = OrganisasjonselementResource.newInstance()
        resource.setOrganisasjonsId(Identifikator.newInstance(identifikatorverdi: 'school-owner-sourced-id'))
        resource.setNavn('SchoolOwner')
        resource.setOrganisasjonsnummer(Identifikator.newInstance(identifikatorverdi: 'identifier'))
        resource.addOverordnet(Link.with('/school-owner-sourced-id'))
        resource.addSelf(Link.with('/school-owner-sourced-id'))
        return resource
    }

    static SkoleResource newSchool() {
        SkoleResource resource = SkoleResource.newInstance()
        resource.setSystemId(Identifikator.newInstance(identifikatorverdi: 'school-sourced-id'))
        resource.setNavn('School')
        resource.setSkolenummer(Identifikator.newInstance(identifikatorverdi: ''))
        resource.setOrganisasjonsnummer(Identifikator.newInstance(identifikatorverdi: 'identifier'))
        resource.addSelf(Link.with('school-sourced-id'))
        return resource
    }

    static PersonResource newPerson() {
        PersonResource resource = PersonResource.newInstance()
        Personnavn name = Personnavn.newInstance()
        name.setFornavn('given-name')
        name.setMellomnavn('middle-name')
        name.setEtternavn('family-name')
        resource.setNavn(name)
        resource.addSelf(Link.with('/person-sourced-id'))

        return resource
    }

    static ElevResource newStudent() {
        ElevResource resource = ElevResource.newInstance()
        resource.setSystemId(Identifikator.newInstance(identifikatorverdi: 'student-sourced-id'))
        resource.setBrukernavn(Identifikator.newInstance(identifikatorverdi: 'username'))
        resource.setElevnummer(Identifikator.newInstance(identifikatorverdi: 'identifier'))
        resource.setFeidenavn(Identifikator.newInstance(identifikatorverdi: 'feide'))
        Kontaktinformasjon contactInformation = Kontaktinformasjon.newInstance()
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
        ElevforholdResource resource = ElevforholdResource.newInstance()
        resource.setSystemId(Identifikator.newInstance(identifikatorverdi: 'student-relation-sourced-id'))
        resource.addElev(Link.with('/student-sourced-id'))
        resource.addBasisgruppe(Link.with('/basis-group-sourced-id'))
        resource.addUndervisningsgruppe(Link.with('/teaching-group-sourced-id'))
        resource.addSkole(Link.with('/school-sourced-id'))
        resource.addSelf(Link.with('/student-relation-sourced-id'))
        return resource
    }

    static SkoleressursResource newTeacher() {
        SkoleressursResource resource = SkoleressursResource.newInstance()
        resource.setSystemId(Identifikator.newInstance(identifikatorverdi: 'teacher-sourced-id'))
        resource.setFeidenavn(Identifikator.newInstance(identifikatorverdi: 'feide'))
        resource.addPersonalressurs(Link.with('/personnel-resource-sourced-id'))
        resource.addUndervisningsforhold(Link.with('/teaching-relation-sourced-id'))
        resource.addSelf(Link.with('/teacher-sourced-id'))
        return resource
    }

    static PersonalressursResource newPersonnelResource() {
        PersonalressursResource resource = PersonalressursResource.newInstance()
        resource.setBrukernavn(Identifikator.newInstance(identifikatorverdi: 'username'))
        resource.setAnsattnummer(Identifikator.newInstance(identifikatorverdi: 'identifier'))
        Kontaktinformasjon contactInformation = Kontaktinformasjon.newInstance()
        contactInformation.setEpostadresse('email')
        contactInformation.setMobiltelefonnummer('sms')
        contactInformation.setTelefonnummer('phone')
        resource.setKontaktinformasjon(contactInformation)
        resource.addPerson(Link.with('/person-sourced-id'))
        resource.addSelf(Link.with('/personnel-resource-sourced-id'))
        return resource
    }

    static UndervisningsforholdResource newTeachingRelation() {
        UndervisningsforholdResource resource = UndervisningsforholdResource.newInstance()
        resource.setSystemId(Identifikator.newInstance(identifikatorverdi: 'teaching-relation-sourced-id'))
        resource.addSkoleressurs(Link.with('/teacher-sourced-id'))
        resource.addBasisgruppe(Link.with('/basis-group-sourced-id'))
        resource.addUndervisningsgruppe(Link.with('/teaching-group-sourced-id'))
        resource.addSkole(Link.with('/school-sourced-id'))
        resource.addSelf(Link.with('/teaching-relation-sourced-id'))
        return resource
    }

    static BasisgruppeResource newBasisGroup() {
        BasisgruppeResource resource = BasisgruppeResource.newInstance()
        resource.setSystemId(Identifikator.newInstance(identifikatorverdi: 'basis-group-sourced-id'))
        resource.setPeriode([])
        resource.setNavn('Basis group')
        resource.setBeskrivelse('Basis group at school')
        resource.addSkole(Link.with('/school-sourced-id'))
        resource.addTrinn(Link.with('/level-sourced-id'))
        resource.addSelf(Link.with('/basis-group-sourced-id'))
        return resource
    }

    static UndervisningsgruppeResource newTeachingGroup() {
        UndervisningsgruppeResource resource = UndervisningsgruppeResource.newInstance()
        resource.setSystemId(Identifikator.newInstance(identifikatorverdi: 'teaching-group-sourced-id'))
        resource.setPeriode([])
        resource.setNavn('Teaching group')
        resource.setBeskrivelse('Teaching group at school')
        resource.addSkole(Link.with('/school-sourced-id'))
        resource.addFag(Link.with('/subject-sourced-id'))
        resource.addSelf(Link.with('/teaching-group-sourced-id'))
        return resource
    }

    static ArstrinnResource newLevel() {
        ArstrinnResource resource = ArstrinnResource.newInstance()
        resource.setSystemId(Identifikator.newInstance(identifikatorverdi: 'level-sourced-id'))
        resource.setPeriode([])
        resource.setNavn('Level')
        resource.setBeskrivelse('A level')
        resource.addGrepreferanse(Link.with('/grep-level'))
        resource.addSelf(Link.with('/level-sourced-id'))
        return resource
    }

    static FagResource newSubject() {
        FagResource resource = FagResource.newInstance()
        resource.setSystemId(Identifikator.newInstance(identifikatorverdi: 'subject-sourced-id'))
        resource.setPeriode([])
        resource.setNavn('Subject')
        resource.setBeskrivelse('A subject')
        resource.addGrepreferanse(Link.with('/grep-subject'))
        resource.addSelf(Link.with('/subject-sourced-id'))
        return resource
    }
}
