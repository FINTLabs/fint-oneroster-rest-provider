package no.fint.oneroster.response

import no.fint.oneroster.model.Org
import no.fint.oneroster.model.vocab.OrgType
import spock.lang.Specification

class OneRosterItemResponseSpec extends Specification {

    def "OneRosterItemResponse returns map of org given org"() {
        when:
        def resources = new OneRosterItemResponse.Builder(new Org('78', 'abc', OrgType.SCHOOL))
                .build()

        then:
        def org = (resources.body.value as Map<String, Org>).get('org')
        org.sourcedId == '78'
        org.name == 'abc'
    }
}
