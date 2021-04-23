package no.fint.oneroster.response

import no.fint.oneroster.model.Org
import no.fint.oneroster.model.vocab.OrgType
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import spock.lang.Specification

class OneRosterCollectionResponseSpec extends Specification {

    def "OneRosterCollectionResponse returns map of orgs given list of orgs"() {
        when:
        def resources = new OneRosterCollectionResponse.Builder(getOrgs(), Org.class)
                .build()

        then:
        (resources.body.value as Map<String, List<Org>>).get('orgs').size() == 4
    }

    def "OneRosterCollectionResponse returns map of selected orgs given list of orgs and filter"() {
        when:
        def resources = new OneRosterCollectionResponse.Builder(getOrgs(), Org.class)
                .filter('sourcedId=\'12\' OR sourcedId~\'5\'')
                .build()

        then:
        def orgs = (resources.body.value as Map<String, List<Org>>).get('orgs')
        orgs.size() == 2
        orgs.first().name == 'jkl'
        orgs.last().name == 'def'
    }

    def "OneRosterCollectionResponse returns map of selected orgs given list of orgs and limit and offset"() {
        given:

        when:
        def resources = new OneRosterCollectionResponse.Builder(getOrgs(), Org.class)
                .pagingAndSorting(PageRequest.of(2, 2))
                .build()

        then:
        def orgs = (resources.body.value as Map<String, List<Org>>).get('orgs')
        orgs.size() == 2
        orgs.first().name == 'def'
        orgs.last().name == 'abc'
    }

    def "OneRosterCollectionResponse returns map of sorted orgs given list of orgs and sort"() {
        given:

        when:
        def resources = new OneRosterCollectionResponse.Builder(getOrgs(), Org.class)
                .pagingAndSorting(PageRequest.of(0, 4, Sort.by('name')))
                .build()

        then:
        def orgs = (resources.body.value as Map<String, List<Org>>).get('orgs')
        orgs.size() == 4
        orgs.first().name == 'abc'
        orgs.last().name == 'jkl'
    }

    def "OneRosterCollectionResponse returns map of selected orgs given list of orgs and filter, limit, offset and sort"() {
        when:
        def resources = new OneRosterCollectionResponse.Builder(getOrgs(), Org.class)
                .filter('sourcedId>\'12\'')
                .pagingAndSorting(PageRequest.of(0, 3, Sort.by('name')))
                .fieldSelection(null)
                .build()

        then:
        def orgs = (resources.body.value as Map<String, List<Org>>).get('orgs')
        orgs.size() == 3
        orgs.first().name == 'abc'
        orgs.last().name == 'ghi'
    }

    List<Org> getOrgs() {
        return [new Org('12', 'jkl', OrgType.SCHOOL),
                new Org('34', 'ghi', OrgType.SCHOOL),
                new Org('56', 'def', OrgType.SCHOOL),
                new Org('78', 'abc', OrgType.SCHOOL)]
    }
}