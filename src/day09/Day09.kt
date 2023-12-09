package day09

import solve

fun String.parsed() = split(" ").map { it.toInt() }
fun List<Int>.withSubSequences() = mutableListOf(this).apply {
    while (last().any { it != 0 }) {
        add(last().zipWithNext { first, second -> second - first })
    }
}

fun main() {
    fun part1(input: List<String>) = input.sumOf { line ->
        line.parsed().withSubSequences().sumOf { it.last() }
    }

    fun part2(input: List<String>) = input.sumOf { line ->
        line.parsed().withSubSequences().map { it.first() }.reversed().reduce { acc, it -> it - acc }
    }

    solve(::part1, withInput = "day09/test", andAssert = 114)
    solve(::part1, withInput = "day09/input", andAssert = 1938800261)

    solve(::part2, withInput = "day09/test", andAssert = 2)
    solve(::part2, withInput = "day09/input", andAssert = 1112)
}
