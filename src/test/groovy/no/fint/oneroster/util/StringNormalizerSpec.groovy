package no.fint.oneroster.util

import spock.lang.Specification

class StringNormalizerSpec extends Specification {

    def "normalize() removes norwegian character given text string"() {
        given:
        def id = '2_1ENG-STÆTTE1_191_SFVS@38026'

        when:
        def normalized = StringNormalizer.normalize(id)

        then:
        normalized == '2_1ENG-STTTE1_191_SFVS@38026'
    }

    def "normalize() converts accent given text string"() {
        given:
        def id = '2_1ENG-STÅTTE1_191_SFVS@38026'

        when:
        def normalized = StringNormalizer.normalize(id)

        then:
        normalized == '2_1ENG-STATTE1_191_SFVS@38026'
    }
}
