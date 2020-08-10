package no.fint.oneroster.util

import no.fint.model.felles.kompleksedatatyper.Periode
import no.fint.model.resource.utdanning.elev.BasisgruppeResource
import no.fint.oneroster.model.vocab.StatusType
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class FactoryUtilSpec extends Specification {

    def "When current date time is within grace period return status of type active"() {
        given:
        def group = newBasisGroup()

        when:
        def active = FactoryUtil.getStatusType(group, ZonedDateTime.of(LocalDateTime.of(2020, 8, 2, 0, 0), ZoneId.systemDefault()))

        then:
        active == StatusType.ACTIVE
    }

    def "When current date time is outside grace period return status of type tobedeleted"() {
        given:
        def group = newBasisGroup()

        when:
        def active = FactoryUtil.getStatusType(group, ZonedDateTime.of(LocalDateTime.of(2021, 8, 1, 0, 0), ZoneId.systemDefault()))

        then:
        active == StatusType.TOBEDELETED
    }

    BasisgruppeResource newBasisGroup() {
        BasisgruppeResource resource = new BasisgruppeResource()
        resource.setPeriode([new Periode(start: Date.from(LocalDate.of(2020, 8, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                slutt: Date.from(LocalDate.of(2021, 7, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()))])
        return resource
    }
}
