package day05

import lib.collection.split
import lib.parser.parse
import lib.parser.result1
import lib.parser.result2
import lib.parser.result3
import solve

data class Mapping(val sourceStart: Long, val destinationStart: Long, val length: Long) {
    private val sourceRange = sourceStart..<(sourceStart + length)

    fun of(source: Long): Long? = source
        .takeIf { it in sourceRange }
        ?.let { destinationStart + (it - sourceStart) }

    fun reversed() = Mapping(destinationStart, sourceStart, length)
}

fun List<Mapping>.of(source: Long) =
    firstNotNullOfOrNull { it.of(source) } ?: source

data class Table(val sourceType: String, val destinationType: String, val mappings: List<Mapping>) {
    fun reversed() = Table(destinationType, sourceType, mappings.map { it.reversed() })
}

class Almanac(private val tables: List<Table>, private val debug: Boolean = false) {
    fun resolve(fromType: String, fromValue: Long, toType: String): Long {
        var currentValue = fromValue
        var currentType = fromType

        if (debug) print("$currentType($currentValue)")

        do {
            val map = tables.first { it.sourceType == currentType }
            currentType = map.destinationType
            currentValue = map.mappings.of(currentValue)
            if (debug) print(" -> $currentType($currentValue)")
        } while (currentType != toType)
        if (debug) println()

        return currentValue
    }

    fun reversed() = Almanac(tables.map { it.reversed() })
}

fun main() {
    fun part1(input: List<String>): Long {
        val seeds: List<Long> = input.first().split(":", " ").mapNotNull { it.toLongOrNull() }
        val almanac = Almanac(parseMaps(input))

        return seeds.minOf { almanac.resolve("seed", it, "location") }
    }

    fun part2(input: List<String>): Long {
        val seedRanges: List<LongRange> = input.first().split(":", " ").mapNotNull { it.toLongOrNull() }
            .chunked(2) { (start, length) -> LongRange(start, start + length - 1) }

        val almanac = Almanac(parseMaps(input)).reversed()

        return (1L..Long.MAX_VALUE).first { location ->
            val resultingSeed = almanac.resolve("location", location, "seed")
            seedRanges.any { resultingSeed in it }
        }
    }

    solve(::part1, withInput = "day05/test", andAssert = 35)
    solve(::part1, withInput = "day05/input", andAssert = 324724204)

    solve(::part2, withInput = "day05/test", andAssert = 46)
    solve(::part2, withInput = "day05/input", andAssert = 104070862)
}

fun parseMaps(input: List<String>): List<Table> {
    val maps = input.split { it.isEmpty() }.drop(1)

    return maps.map { group ->
        val (source, destination) =
            group.first()
                .parse<String, String> {
                    split("-", " ") { first.result1(); third.result2() }
                }
        val mappings = group.drop(1).map {
            it.parse<Long, Long, Long> {
                split(" ") {
                    first.result1 { toLong() }
                    second.result2 { toLong() }
                    third.result3 { toLong() }
                }
            }.let { (dStart, sStart, len) -> Mapping(sStart, dStart, len) }
        }
        Table(source, destination, mappings)
    }
}
