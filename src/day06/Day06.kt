package day06

import lib.collection.product
import solve

data class Race(val time: Long, val recordDistance: Long)

fun distanceTraveled(raceTime: Long, holdTime: Long) =
    (raceTime - holdTime) * holdTime

fun winningHoldTimes(race: Race): List<Long> =
    (0..race.time)
        .map { holdTime -> holdTime to distanceTraveled(race.time, holdTime) }
        .filter { (_, distance) -> distance > race.recordDistance }
        .map { it.first }

fun parseRaces(input: List<String>): List<Race> {
    val times = input.first().split(":").last().trim().split(" ").mapNotNull { it.toLongOrNull() }
    val recordDistances = input.drop(1).first().split(":").last().trim().split(" ").mapNotNull { it.toLongOrNull() }

    return times.zip(recordDistances).map { (time, recordDistance) -> Race(time, recordDistance) }
}

fun parseAsSingleRace(input: List<String>): Race {
    val time = input.first().replace(" ", "").split(":")[1].toLong()
    val distance = input.drop(1).first().replace(" ", "").split(":")[1].toLong()
    return Race(time, distance)
}

fun main() {
    println(winningHoldTimes(Race(7, 9)))

    fun part1(input: List<String>) = parseRaces(input)
        .map { winningHoldTimes(it).size }
        .product()

    fun part2(input: List<String>): Int = parseAsSingleRace(input)
        .let { winningHoldTimes(it).size }

    solve(::part1, withInput = "day06/test", andAssert = 288)
    solve(::part1, withInput = "day06/input", andAssert = 503424)

    solve(::part2, withInput = "day06/test", andAssert = 71503)
    solve(::part2, withInput = "day06/input", andAssert = 32607562)
}
