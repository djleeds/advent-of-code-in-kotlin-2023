package day04

import solve
import kotlin.math.pow

data class Card(val id: Int, val winners: List<Int>, val numbers: List<Int>) {
    private val winningCount: Int = numbers.count { it in winners }
    val points get() = 2.toDouble().pow(winningCount - 1).toInt()
}


fun main() {
    fun part1(input: List<String>): Int = parse(input).sumOf { it.points }

    fun part2(input: List<String>): Int = -1

    solve(::part1, withInput = "day04/test", andAssert = 13)
    solve(::part1, withInput = "day04/input", andAssert = 23673)

    //solve(::part2, withInput = "day04/test", andAssert = null)
    //solve(::part2, withInput = "day04/input", andAssert = null)
}

fun parse(input: List<String>): List<Card> =
    input.map { line ->
        val (label, values) = line.split(":")
        val (_, id) = label.split(" ").filter { it.isNotEmpty() }
        val (winners, numbers) = values.split("|")
        Card(
            id.toInt(),
            winners.split(" ").filter { it.isNotEmpty() }.map { it.toInt() },
            numbers.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        )
    }
