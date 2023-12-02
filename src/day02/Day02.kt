package day02

import assertk.assertThat
import assertk.assertions.isEqualTo
import readInput

data class Draw(val red: Int, val green: Int, val blue: Int) {
    fun isPossible(fullDraw: Draw) =
        red <= fullDraw.red && green <= fullDraw.green && blue <= fullDraw.blue

    val power = red * green * blue

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

    fun smallestFullDraw() =
        Draw(draws.maxOf { it.red }, draws.maxOf { it.green }, draws.maxOf { it.blue })

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

    fun part2(input: List<String>) =
        Game.parseMany(input).map { it.smallestFullDraw() }.sumOf { it.power }

    val part1TestResult = part1(readInput("day02/test-input-1"))
    println(part1TestResult)
    assertThat(part1TestResult).isEqualTo(8)

    val part1Result = part1(readInput("day02/input"))
    println(part1Result)
    assertThat(part1Result).isEqualTo(1867)

    val part2TestResult = part2(readInput("day02/test-input-1"))
    println(part2TestResult)
    assertThat(part2TestResult).isEqualTo(2286)

    val part2Result = part2(readInput("day02/input"))
    println(part2Result)
    assertThat(part2Result).isEqualTo(84538)
}
