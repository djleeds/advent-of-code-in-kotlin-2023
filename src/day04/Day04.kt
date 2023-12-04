package day04

import solve
import kotlin.math.pow

data class Card(
    val id: Int,
    val winners: List<Int>,
    val numbers: List<Int>,
    val copiesWon: List<Card> = emptyList()
) {
    val winningCount: Int = numbers.count { it in winners }
    val points = 2.toDouble().pow(winningCount - 1).toInt()
    val total: Int = 1 + copiesWon.sumOf { it.total }
}

fun main() {
    fun part1(input: List<String>): Int = parse(input).sumOf { it.points }
    fun part2(input: List<String>): Int = parse(input).sumOf { it.total }

    solve(::part1, withInput = "day04/test", andAssert = 13)
    solve(::part1, withInput = "day04/input", andAssert = 23673)

    solve(::part2, withInput = "day04/test", andAssert = 30)
    solve(::part2, withInput = "day04/input", andAssert = 12263631)
}

fun <T> List<T>.duplicate() = ArrayList(this)

fun parse(input: List<String>): List<Card> {
    val cards = mutableListOf<Card>()
    for (i in input.indices.reversed()) {
        val line = input[i]
        val (label, values) = line.split(":")
        val (_, id) = label.split(" ").filter { it.isNotEmpty() }
        val (winners, numbers) = values.split("|")
        val card = Card(
            id.toInt(),
            winners.split(" ").filter { it.isNotEmpty() }.map { it.toInt() },
            numbers.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        ).let {
            if (it.winningCount == 0) it else
                it.copy(copiesWon = cards.subList(cards.size - it.winningCount, cards.size).duplicate())
        }
        cards.add(card)
    }
    return cards.reversed()
}
