package day11

import solve
import kotlin.math.max
import kotlin.math.min

data class Position(var x: Long, var y: Long) {
    private fun canonicalizedWith(other: Position): Pair<Position, Position> =
        Position(min(x, other.x), min(y, other.y)) to Position(max(x, other.x), max(y, other.y))

    fun manhattanDistanceTo(other: Position): Long {
        val (minimum, maximum) = canonicalizedWith(other)
        return (maximum.x - minimum.x) + (maximum.y - minimum.y)
    }
}

fun List<Position>.allPairs(): List<Pair<Position, Position>> {
    val first = firstOrNull() ?: return emptyList()
    val result = drop(1).map { first to it }
    return result + drop(1).allPairs()
}

class World(private val corner: Position, val galaxies: Set<Position>) {
    fun expand(size: Long): World {
        val widthBounds = 0..corner.x
        val heightBounds = 0..corner.y

        val emptyColumns = widthBounds.filter { x -> galaxies.none { it.x == x } }
        val emptyRows = heightBounds.filter { y -> galaxies.none { it.y == y } }

        emptyColumns.reversed().forEach { x -> galaxies.filter { it.x > x }.forEach { it.x += size } }
        emptyRows.reversed().forEach { y -> galaxies.filter { it.y > y }.forEach { it.y += size } }

        corner.x += (emptyColumns.size * size)
        corner.y += (emptyColumns.size * size)

        return this
    }

    companion object {
        fun from(input: List<String>): World {
            val galaxies: MutableSet<Position> = mutableSetOf()

            input.indices.forEach { y ->
                input[0].indices.forEach { x ->
                    if (input[y][x] == '#') galaxies.add(Position(x.toLong(), y.toLong()))
                }
            }

            return World(Position(input[0].indices.last.toLong(), input.indices.last.toLong()), galaxies)
        }
    }
}

fun main() {
    fun part1(input: List<String>) =
        World.from(input).expand(1).galaxies.toList()
            .allPairs()
            .sumOf { it.first.manhattanDistanceTo(it.second) }

    fun part2(input: List<String>) =
        World.from(input).expand(1000000 - 1).galaxies.toList()
            .allPairs()
            .sumOf { it.first.manhattanDistanceTo(it.second) }

    solve(::part1, withInput = "day11/test", andAssert = 374)
    solve(::part1, withInput = "day11/input", andAssert = 9684228)

    solve(::part2, withInput = "day11/test", andAssert = null)
    solve(::part2, withInput = "day11/input", andAssert = 483844716556)
}
