package day05

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull

fun main() {

    run {
        val mapping = Mapping(98, 50, 2)

        assertThat(mapping.of(97)).isNull()
        assertThat(mapping.of(98)).isEqualTo(50)
        assertThat(mapping.of(99)).isEqualTo(51)
        assertThat(mapping.of(100)).isNull()
    }

    run {
        val mappings = listOf(Mapping(98, 50, 2))

        assertThat(mappings.of(97)).isEqualTo(97)
        assertThat(mappings.of(98)).isEqualTo(50)
        assertThat(mappings.of(99)).isEqualTo(51)
        assertThat(mappings.of(100)).isEqualTo(100)
    }

}
