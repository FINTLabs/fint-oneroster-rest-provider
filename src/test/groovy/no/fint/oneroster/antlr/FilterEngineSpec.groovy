package no.fint.oneroster.antlr

import no.fint.oneroster.filter.FilterEngine
import no.fint.oneroster.model.AcademicSession
import no.fint.oneroster.model.GUIDRef
import no.fint.oneroster.model.Org
import no.fint.oneroster.model.User
import no.fint.oneroster.model.vocab.GUIDType
import no.fint.oneroster.model.vocab.OrgType
import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.model.vocab.SessionType
import spock.lang.Ignore
import spock.lang.Specification

import java.time.LocalDate

class FilterEngineSpec extends Specification {

    FilterEngine filterEngine = new FilterEngine();

    def "Simple string eq query"() {
        given:
        def query = 'sourcedId=\'sourcedId\''
        def object = new Org('sourcedId', 'name', OrgType.SCHOOL)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Simple string ne query"() {
        given:
        def query = 'sourcedId!=\'sourcedId\''
        def object = new Org('sourcedId', 'name', OrgType.SCHOOL)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        !evaluate
    }

    def "Simple string gt query"() {
        given:
        def query = 'sourcedId>\'sourcedId\''
        def object = new Org('sourcedId', 'name', OrgType.SCHOOL)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        !evaluate
    }

    def "Simple string lt query"() {
        given:
        def query = 'sourcedId<\'sourcedId\''
        def object = new Org('sourcedId', 'name', OrgType.SCHOOL)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        !evaluate
    }

    def "Simple string ge query"() {
        given:
        def query = 'sourcedId>=\'sourcedId\''
        def object = new Org('sourcedId', 'name', OrgType.SCHOOL)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Simple string le query"() {
        given:
        def query = 'sourcedId>=\'sourcedId\''
        def object = new Org('sourcedId', 'name', OrgType.SCHOOL)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Simple string co query"() {
        given:
        def query = 'sourcedId~\'sourcedId\''
        def object = new Org('sourcedId', 'name', OrgType.SCHOOL)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Simple boolean eq query"() {
        given:
        def query = 'enabledUser=\'true\''
        def object = new User('sourcedId', 'username', true, 'givenName', 'familyName', RoleType.STUDENT, [GUIDRef.of(GUIDType.ORG, 'sourcedId')])

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Simple boolean ne query"() {
        given:
        def query = 'enabledUser!=\'true\''
        def object = new User('sourcedId', 'username', true, 'givenName', 'familyName', RoleType.STUDENT, [GUIDRef.of(GUIDType.ORG, 'sourcedId')])

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        !evaluate
    }

    def "Simple local date eq query"() {
        given:
        def query = 'startDate=\'2020-01-01\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Simple local date ne query"() {
        given:
        def query = 'startDate!=\'2020-01-01\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        !evaluate
    }

    def "Simple local date gt query"() {
        given:
        def query = 'startDate>\'2020-01-01\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        !evaluate
    }

    def "Simple local date lt query"() {
        given:
        def query = 'startDate<\'2020-01-01\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        !evaluate
    }

    def "Simple local date ge query"() {
        given:
        def query = 'startDate>=\'2020-01-01\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Simple local date le query"() {
        given:
        def query = 'startDate<=\'2020-01-01\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Simple zoned date time eq query"() {
        given:
        def query = 'dateLastModified=\'1900-01-01\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        !evaluate
    }

    def "Simple zoned date time ne query"() {
        given:
        def query = 'dateLastModified!=\'1900-01-01\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Simple zoned date time gt query"() {
        given:
        def query = 'dateLastModified>\'1900-01-01\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Simple zoned date time lt query"() {
        given:
        def query = 'dateLastModified<\'1900-01-01\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        !evaluate
    }

    def "Simple zoned date time ge query"() {
        given:
        def query = 'dateLastModified>=\'1900-01-01\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Simple zoned date time le query"() {
        given:
        def query = 'dateLastModified<=\'1900-01-01\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        !evaluate
    }

    def "Simple integer eq query"() {
        given:
        def query = 'schoolYear=\'2020\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Simple integer ne query"() {
        given:
        def query = 'schoolYear!=\'2020\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        !evaluate
    }

    def "Simple integer gt query"() {
        given:
        def query = 'schoolYear>\'2020\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        !evaluate
    }

    def "Simple integer lt query"() {
        given:
        def query = 'schoolYear<\'2020\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        !evaluate
    }

    def "Simple integer ge query"() {
        given:
        def query = 'schoolYear>=\'2020\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Simple integer le query"() {
        given:
        def query = 'schoolYear<=\'2020\''
        def object = new AcademicSession('sourcedId', 'title', LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 31), SessionType.TERM, 2020)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Nested map simple string eq query"() {
        given:
        def query = 'metadata.key=\'value\''
        def object = new Org('sourcedId', 'name', OrgType.SCHOOL)
        object.setMetadata([('key'): 'value'])

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Nested object simple string query"() {
        given:
        def query = 'parent.sourcedId=\'sourcedId\''
        def object = new Org('sourcedI', 'name', OrgType.SCHOOL)
        object.setParent(GUIDRef.of(GUIDType.ORG, 'sourcedId'))

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    @Ignore
    def "Nested list simple string query"() {
        given:
        def query = 'children.sourcedId=\'sourcedId\''
        def object = new Org('sourcedId', 'name', OrgType.SCHOOL)
        object.setChildren([GUIDRef.of(GUIDType.ORG, 'sourcedId')])

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Logical AND string query"() {
        given:
        def query = 'sourcedId=\'sourcedId\' AND name=\'name\''
        def object = new Org('sourcedId', 'name', OrgType.SCHOOL)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }

    def "Logical OR string query"() {
        given:
        def query = 'sourcedId=\'sourcedId\' OR name=\'na\''
        def object = new Org('sourcedId', 'name', OrgType.SCHOOL)

        when:
        def evaluate = filterEngine.execute(query, object)

        then:
        evaluate
    }
}
