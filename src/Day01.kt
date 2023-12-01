import assertk.assertThat
import assertk.assertions.isEqualTo

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val firstDigit = line.first { it.isDigit() }
            val lastDigit = line.last { it.isDigit() }
            "$firstDigit$lastDigit".toInt()
        }
    }

    val spelledDigits =
        listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    fun String.findDigit(indexTransform: IntProgression.() -> IntProgression = { this }): Int {
        indices.indexTransform().forEach { index ->
            if (this[index].isDigit()) return this[index].digitToInt()
            else {
                spelledDigits.indices
                    .forEach { digit -> if (substring(index).startsWith(spelledDigits[digit])) return digit }
            }
        }

        throw IllegalStateException()
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            val first = it.findDigit()
            val last = it.findDigit { reversed() }
            "$first$last".toInt()
        }
    }

    val testInput = readInput("Day01_test")
    assertThat(part2(testInput)).isEqualTo(281)

    val input = readInput("Day01")
    part1(input).println()
    assertThat(part1(input)).isEqualTo(55834)

    part2(input).println()
    assertThat(part2(input)).isEqualTo(53221)
}
