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
    NORTH_AND_EAST('F', setOf(SOUTH, EAST)),
    NORTH_AND_SOUTH('|', setOf(NORTH, SOUTH)),
    NORTH_AND_WEST('7', setOf(SOUTH, WEST)),
    SOUTH_AND_EAST('L', setOf(NORTH, EAST)),
    SOUTH_AND_WEST('J', setOf(NORTH, WEST)),
    EAST_AND_WEST('-', setOf(EAST, WEST)),
    START('S', emptySet()),
    EMPTY('.', emptySet());

    fun nextDirection(lastDirection: Direction) = connections.first { it != lastDirection.toBack() }

    override fun toString() = "$char"

    companion object {
        fun from(char: Char) = entries.first { it.char == char }
    }
}

class World(val map: List<List<Tile>>) {
    val start: Position = run {
        val y = map.indexOfFirst { it.contains(Tile.START) }
        val x = map[y].indexOfFirst { it == Tile.START }
        Position(x, y)
    }

    val firstStepDirection = when {
        at(start.east).connections.contains(WEST)   -> EAST
        at(start.south).connections.contains(NORTH) -> SOUTH
        at(start.west).connections.contains(EAST)   -> WEST
        at(start.north).connections.contains(SOUTH) -> NORTH
        else                                        -> throw IllegalStateException()
    }

    private val nsBounds = map.indices
    private val ewBounds = map[0].indices
    fun isInBounds(position: Position): Boolean = position.y in nsBounds && position.x in ewBounds

    fun at(position: Position) = map[position.y][position.x]

    fun traversePipeLoop(): Sequence<Pair<Position, Tile>> = sequence {
        var position = start
        var direction = firstStepDirection
        var tile: Tile

        do {
            position = position.go(direction)
            tile = at(position)
            yield(position to tile)
            if (tile != Tile.START) direction = tile.nextDirection(direction)
        } while (tile != Tile.START)
    }

    companion object {
        fun from(input: List<String>) =
            World(input.map { line -> line.map { char -> Tile.from(char) } })
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val world = World.from(input)
        return world.traversePipeLoop().count() / 2
    }

    fun part2(input: List<String>): Int {
        val world = World.from(input)

        val loopPositions = mutableSetOf<Position>()
        val rightSidePositions = mutableSetOf<Position>()
        val leftSidePositions = mutableSetOf<Position>()

        var position = world.start

        loopPositions.add(position)

        position = position.go(world.firstStepDirection)
        loopPositions.add(position)
        var lastStepDirection = world.firstStepDirection

        while (world.at(position) != Tile.START) {
            val currentTile = world.at(position)
            lastStepDirection = currentTile.nextDirection(lastStepDirection)
            position = position.go(lastStepDirection)
            loopPositions.add(position)
        }

        // Traverse again, now that we have the loop charted, and fire rays off to the sides.
        var outsideIsToTheLeft: Boolean? = null

        position = position.go(world.firstStepDirection)
        lastStepDirection = world.firstStepDirection

        while (world.at(position) != Tile.START) {
            val currentTile = world.at(position)
            val nextStepDirection = currentTile.nextDirection(lastStepDirection)

            var right = lastStepDirection.toRight()
            var rayPosition: Position

            rayPosition = position.go(right)
            while (rayPosition !in loopPositions && world.isInBounds(rayPosition)) {
                rightSidePositions.add(rayPosition)
                rayPosition = rayPosition.go(right)
                if (!world.isInBounds(rayPosition)) outsideIsToTheLeft = false
            }

            right = nextStepDirection.toRight()
            rayPosition = position.go(right)
            while (rayPosition !in loopPositions && world.isInBounds(rayPosition)) {
                rightSidePositions.add(rayPosition)
                rayPosition = rayPosition.go(right)
                if (!world.isInBounds(rayPosition)) outsideIsToTheLeft = false
            }

            var left = lastStepDirection.toLeft()
            rayPosition = position.go(left)
            while (rayPosition !in loopPositions && world.isInBounds(rayPosition)) {
                leftSidePositions.add(rayPosition)
                rayPosition = rayPosition.go(left)
                if (!world.isInBounds(rayPosition)) outsideIsToTheLeft = true
            }

            left = nextStepDirection.toLeft()
            rayPosition = position.go(left)
            while (rayPosition !in loopPositions && world.isInBounds(rayPosition)) {
                leftSidePositions.add(rayPosition)
                rayPosition = rayPosition.go(left)
                if (!world.isInBounds(rayPosition)) outsideIsToTheLeft = true
            }

            lastStepDirection = nextStepDirection
            position = position.go(lastStepDirection)
        }

        if (debug) {
            println("Left Positions = $leftSidePositions")
            println("Right Positions = $rightSidePositions")

            println("Left Positions Count = ${leftSidePositions.size}")
            println("Right Positions Count = ${rightSidePositions.size}")

            input.forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    val pos = Position(x, y)
                    when {
                        pos in loopPositions      -> print(char)
                        pos in leftSidePositions  -> print("O")
                        pos in rightSidePositions -> print("I")
                        else                      -> print(".")
                    }
                }
                println()
            }

            println("Outside is on the ${if (outsideIsToTheLeft!!) "left" else "right"}")
        }

        return if (outsideIsToTheLeft!!) rightSidePositions.size else leftSidePositions.size
    }

    solve(::part1, withInput = "day10/test", andAssert = 8)
    solve(::part1, withInput = "day10/input", andAssert = 7107)

    solve(::part2, withInput = "day10/test2", andAssert = 10)
    solve(::part2, withInput = "day10/input", andAssert = 281)
}
