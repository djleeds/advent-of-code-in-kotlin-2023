package day01

import solve

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

    solve(::part1, withInput = "day01/test1", andAssert = 142)
    solve(::part1, withInput = "day01/input", andAssert = 55834)

    solve(::part2, withInput = "day01/test2", andAssert = 281)
    solve(::part2, withInput = "day01/input", andAssert = 53221)
}
