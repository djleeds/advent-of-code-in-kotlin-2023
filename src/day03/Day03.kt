package day03

import solve
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int =
        parse(input)
            .filter { it.symbol != null }
            .sumOf { it.number }

    fun part2(input: List<String>) =
        parse(input)
            .filter { it.symbol?.char == '*' }
            .groupBy { it.symbol }
            .filterValues { it.size == 2 }
            .map { (_, partNumbers) -> partNumbers.first().number * partNumbers.last().number }
            .sum()

    solve(::part1, withInput = "day03/test", andAssert = 4361)
    solve(::part1, withInput = "day03/input", andAssert = 553079)

    solve(::part2, withInput = "day03/test", andAssert = 467835)
    solve(::part2, withInput = "day03/input", andAssert = 84363105)
}

data class Symbol(val char: Char?, val x: Int, val y: Int)
data class PartNumber(val number: Int, val symbol: Symbol?)

data class IntRange2D(val xRange: IntRange, val yRange: IntRange) {
    fun forEach(block: (Int, Int) -> Unit) =
        xRange.forEach { x -> yRange.forEach { y -> block(x, y) } }
}

fun parse(input: List<String>): List<PartNumber> {
    val partNumbers = mutableListOf<PartNumber>()
    input.forEachIndexed { y, line ->
        var runningNumber = ""
        line.forEachIndexed { x, char ->
            if (char.isDigit()) {
                runningNumber = "$runningNumber$char"
            }
            if (!char.isDigit() || x == line.length - 1) {
                if (runningNumber.isNotEmpty()) {
                    val range = IntRange2D(
                        max(x - runningNumber.length - 1, 0)..min(x, line.length - 1),
                        max(y - 1, 0)..min(y + 1, input.size - 1)
                    )

                    var symbol: Symbol? = null
                    range.forEach { sx, sy ->
                        val scanChar = input[sy][sx]
                        if (!scanChar.isDigit() && scanChar != '.') {
                            symbol = Symbol(scanChar, sx, sy)
                            return@forEach
                        }
                    }

                    partNumbers.add(PartNumber(runningNumber.toInt(), symbol))

                    runningNumber = ""
                }
            }
        }
    }
    return partNumbers
}
