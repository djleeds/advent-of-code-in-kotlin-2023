package day10

import day10.Direction.*
import solve

val debug = false

data class Position(val x: Int, val y: Int) {
    val east get() = copy(x = x + 1)
    val south get() = copy(y = y + 1)
    val west get() = copy(x = x - 1)
    val north get() = copy(y = y - 1)

    fun go(direction: Direction) = when (direction) {
        NORTH -> north
        SOUTH -> south
        EAST  -> east
        WEST  -> west
    }
}

enum class Direction {
    NORTH, EAST, SOUTH, WEST;

    fun toRight() = rotate(1)
    fun toLeft() = rotate(3)
    fun toBack() = rotate(2)

    private fun rotate(ninetyDegreeIncrements: Int) =
        entries[(ordinal + ninetyDegreeIncrements).rem(entries.size)]
}

enum class Tile(val char: Char, val connections: Set<Direction>) {
    NORTH_AND_SOUTH('|', setOf(NORTH, SOUTH)),
    EAST_AND_WEST('-', setOf(EAST, WEST)),
    NORTH_AND_EAST('L', setOf(NORTH, EAST)),
    NORTH_AND_WEST('J', setOf(NORTH, WEST)),
    SOUTH_AND_WEST('7', setOf(SOUTH, WEST)),
    SOUTH_AND_EAST('F', setOf(SOUTH, EAST)),
    START('S', emptySet()),
    EMPTY('.', emptySet());

    fun nextDirection(lastDirection: Direction) = connections.first { it != lastDirection.toBack() }

    override fun toString() = "$char"

    companion object {
        fun from(char: Char) = entries.first { it.char == char }
    }
}

class World(val map: List<List<Tile>>) {
    private val start: Position = run {
        val y = map.indexOfFirst { it.contains(Tile.START) }
        val x = map[y].indexOfFirst { it == Tile.START }
        Position(x, y)
    }

    private val firstStepDirection = when {
        at(start.east).connections.contains(WEST) -> EAST
        at(start.south).connections.contains(NORTH) -> SOUTH
        at(start.west).connections.contains(EAST) -> WEST
        at(start.north).connections.contains(SOUTH) -> NORTH
        else -> throw IllegalStateException()
    }

    private val northSouthBounds = map.indices
    private val eastWestBounds = map[0].indices

    private fun isInBounds(position: Position): Boolean =
        position.y in northSouthBounds && position.x in eastWestBounds

    private fun at(position: Position) =
        map[position.y][position.x]

    fun traversePipeLoop() = traversePipeLoop { _, _, _ -> }

    fun <T> traversePipeLoop(transform: (Position, Tile, Direction) -> T): List<T> {
        val result = mutableListOf<T>()
        var position = start
        var direction = firstStepDirection
        var tile: Tile

        do {
            position = position.go(direction)
            tile = at(position)
            transform(position, tile, direction).also { result.add(it) }
            if (tile != Tile.START) direction = tile.nextDirection(direction)
        } while (tile != Tile.START)

        return result
    }

    fun probe(
        start: Position,
        direction: Direction,
        terminalPositions: Set<Position>,
        onOutOfBounds: () -> Unit = {},
        forEach: (Position) -> Unit
    ) {
        var position = start.go(direction)
        while (position !in terminalPositions && isInBounds(position)) {
            forEach(position)
            position = position.go(direction)
        }
        if (!isInBounds(position)) onOutOfBounds()
    }

    companion object {
        fun from(input: List<String>) =
            World(input.map { line -> line.map { char -> Tile.from(char) } })
    }
}

fun main() {
    fun part1(input: List<String>): Int =
        World.from(input).traversePipeLoop().count() / 2

    fun part2(input: List<String>): Int {
        val world = World.from(input)

        val loopPositions = world.traversePipeLoop { position, _, _ -> position }.toSet()

        val rightSidePositions = mutableSetOf<Position>()
        val leftSidePositions = mutableSetOf<Position>()
        var clockwise: Boolean? = null

        world.traversePipeLoop { pos, tile, dir ->
            val nextDir =
                if (tile == Tile.START) dir else tile.nextDirection(dir)

            world.probe(pos, dir.toRight(), loopPositions, { clockwise = false }, { rightSidePositions.add(it) })
            world.probe(pos, nextDir.toRight(), loopPositions, { clockwise = true }, { rightSidePositions.add(it) })
            world.probe(pos, dir.toLeft(), loopPositions, { clockwise = false }, { leftSidePositions.add(it) })
            world.probe(pos, nextDir.toLeft(), loopPositions, { clockwise = true }, { leftSidePositions.add(it) })
        }

        debug(leftSidePositions, rightSidePositions, input, loopPositions, clockwise!!)

        return if (clockwise!!) rightSidePositions.size else leftSidePositions.size
    }

    solve(::part1, withInput = "day10/test", andAssert = 8)
    solve(::part1, withInput = "day10/input", andAssert = 7107)

    solve(::part2, withInput = "day10/test2", andAssert = 10)
    solve(::part2, withInput = "day10/input", andAssert = 281)
}
