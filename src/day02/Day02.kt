package day02

import assertk.assertThat
import assertk.assertions.isEqualTo
import readInput

data class Draw(val red: Int, val green: Int, val blue: Int) {
    fun isPossible(fullDraw: Draw) =
        red <= fullDraw.red && green <= fullDraw.green && blue <= fullDraw.blue

    companion object {
        fun parse(input: String): Draw =
            input.split(",").map { it.trim().split(" ") }
                .associate { it.last() to it.first().toInt() }
                .run { Draw(amount("red"), amount("green"), amount("blue")) }

        fun parseMany(input: String): List<Draw> =
            input.split(";").map { parse(it.trim()) }

        private fun Map<String, Int>.amount(key: String) = getOrDefault(key, 0)
    }
}

data class Game(val number: Int, val draws: List<Draw>) {
    fun isPossible(fullDraw: Draw) = draws.all { it.isPossible(fullDraw) }

    companion object {
        fun parse(input: String): Game {
            val (game, draws) = input.split(":")
            val (_, gameNumber) = game.split(" ")
            return Game(gameNumber.toInt(), Draw.parseMany(draws))
        }

        fun parseMany(input: List<String>): List<Game> =
            input.map(::parse)
    }
}

fun main() {
    fun part1(input: List<String>) =
        Game.parseMany(input).filter { it.isPossible(Draw(12, 13, 14)) }.sumOf { it.number }

    fun part2(input: List<String>): Int {
        return 1
    }

    val part1Test = part1(readInput("day02/test-input-1"))
    println(part1Test)
    assertThat(part1Test).isEqualTo(8)

    val input = readInput("day02/input")
    println(part1(input))
    assertThat(part1(input)).isEqualTo(1867)
//
//    part2(input).println()
//    assertThat(part2(input)).isEqualTo(53221)
}
