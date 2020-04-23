package no.fint.oneroster.util

import groovy.util.logging.Slf4j
import no.fint.oneroster.model.Org
import no.fint.oneroster.model.vocab.OrgType
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import spock.lang.Specification

@Slf4j
class OneRosterResponseSpec extends Specification {

    OneRosterResponse oneRosterResponseCollection = new OneRosterResponse(Org.class, 'orgs')
    OneRosterResponse oneRosterResponseItem = new OneRosterResponse(Org.class, 'org')


    def "OneRosterResponseService returns map of orgs given list of orgs"() {
        when:
        def resources = oneRosterResponseCollection.collection(getOrgs())
                .fieldSelection(null)

        then:
        (resources.body.value as Map<String, List<Org>>).get('orgs').size() == 4
    }

    def "OneRosterResponseService returns map of selected orgs given list of orgs and filter"() {
        when:
        def resources = oneRosterResponseCollection.collection(getOrgs())
                .filter('sourcedId=\'12\' OR sourcedId~\'5\'')
                .fieldSelection(null)

        then:
        def orgs = (resources.body.value as Map<String, List<Org>>).get('orgs')
        orgs.size() == 2
        orgs.first().name == 'jkl'
        orgs.last().name == 'def'
    }

    def "OneRosterResponseService returns map of selected orgs given list of orgs and limit and offset"() {
        given:

        when:
        def resources = oneRosterResponseCollection.collection(getOrgs())
                .pagingAndSorting(PageRequest.of(2, 2))
                .fieldSelection(null)

        then:
        def orgs = (resources.body.value as Map<String, List<Org>>).get('orgs')
        orgs.size() == 2
        orgs.first().name == 'def'
        orgs.last().name == 'abc'
    }

    def "OneRosterResponseService returns map of sorted orgs given list of orgs and sort"() {
        given:

        when:
        def resources = oneRosterResponseCollection.collection(getOrgs())
                .pagingAndSorting(PageRequest.of(0, 4, Sort.by('name')))
                .fieldSelection(null)

        then:
        def orgs = (resources.body.value as Map<String, List<Org>>).get('orgs')
        orgs.size() == 4
        orgs.first().name == 'abc'
        orgs.last().name == 'jkl'
    }

    def "OneRosterResponseService returns map of selected orgs given list of orgs and filter, limit, offset and sort"() {
        when:
        def resources = oneRosterResponseCollection.collection(getOrgs())
                .filter('sourcedId>\'12\'')
                .pagingAndSorting(PageRequest.of(0, 3, Sort.by('name')))
                .fieldSelection(null)

        then:
        def orgs = (resources.body.value as Map<String, List<Org>>).get('orgs')
        orgs.size() == 3
        orgs.first().name == 'abc'
        orgs.last().name == 'ghi'
    }

    def "OneRosterResponseService returns map of org given org"() {
        when:
        def resources = oneRosterResponseItem.item(new Org('78', 'abc', OrgType.SCHOOL))
                .fieldSelection(null)

        then:
        def org = (resources.body.value as Map<String, Org>).get('org')
        org.sourcedId == '78'
        org.name == 'abc'
    }

    List<Org> getOrgs() {
        return [new Org('12', 'jkl', OrgType.SCHOOL),
                new Org('34', 'ghi', OrgType.SCHOOL),
                new Org('56', 'def', OrgType.SCHOOL),
                new Org('78', 'abc', OrgType.SCHOOL)]
    }
}
