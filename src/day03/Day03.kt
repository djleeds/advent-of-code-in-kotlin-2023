package day03

import solve
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        val result = parse(input)
        println(result)
        return result.filter { it.symbol != null }.sumOf { it.number }
    }

    fun part2(input: List<String>) = 1

    solve(::part1, withInput = "day03/test", andAssert = 4361)
    solve(::part1, withInput = "day03/input")
//
//    solve(::part2, withInput = "day02/test", andAssert = 2286)
//    solve(::part2, withInput = "day02/input", andAssert = 84538)
}

data class PartNumber(val number: Int, val symbol: Char? = null)

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
                    val partNumber = runningNumber.toInt()

                    val scanX1 = max(x - runningNumber.length - 1, 0)
                    val scanX2 = min(x, line.length - 1)
                    val scanY1 = max(y - 1, 0)
                    val scanY2 = min(y + 1, input.size - 1)

                    var symbol: Char? = null
                    for (sx in scanX1..scanX2) {
                        for (sy in scanY1..scanY2) {
                            val scanChar = input[sy][sx]
                            if (!scanChar.isDigit() && scanChar != '.') {
                                symbol = scanChar
                            }
                        }
                    }

                    partNumbers.add(PartNumber(partNumber, symbol))


                    runningNumber = ""
                }
            }
        }
    }
    return partNumbers
}
