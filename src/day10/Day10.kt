package day10

import day10.Direction.*
import solve

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

    fun at(position: Position) = map[position.y][position.x]

    companion object {
        fun from(input: List<String>) =
            World(input.map { line -> line.map { char -> Tile.from(char) } })
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val world = World.from(input)

        var position = world.start
        val firstStepDirection = world.firstStepDirection

        position = position.go(firstStepDirection)
        var steps = 1
        var lastStepDirection = firstStepDirection

        while (world.at(position) != Tile.START) {
            val currentTile = world.at(position)
            println(currentTile)
            lastStepDirection = currentTile.nextDirection(lastStepDirection)
            position = position.go(lastStepDirection)
            steps++
        }

        return steps / 2
    }

    fun part2(input: List<String>): Int {
        val world = World.from(input)

        val ewBounds = input[0].indices
        val nsBounds = input.indices

        val loopPositions = mutableSetOf<Position>()
        val rightSidePositions = mutableSetOf<Position>()
        val leftSidePositions = mutableSetOf<Position>()

        val y = input.indexOfFirst { it.contains('S') }
        val x = input[y].indexOfFirst { it == 'S' }
        val startPosition = Position(x, y)
        var position = startPosition

        println("Start position = $position")
        loopPositions.add(position)

        val firstStepDirection = when {
            world.at(position.east).connections.contains(WEST)   -> EAST
            world.at(position.south).connections.contains(NORTH) -> SOUTH
            world.at(position.west).connections.contains(EAST)   -> WEST
            world.at(position.north).connections.contains(SOUTH) -> NORTH
            else                                                 -> throw IllegalStateException()
        }

        println("First step = $firstStepDirection")

        position = position.go(firstStepDirection)
        loopPositions.add(position)
        var steps = 1
        var lastStepDirection = firstStepDirection

        while (world.at(position) != Tile.START) {
            val currentTile = world.at(position)
            println(currentTile)
            lastStepDirection = currentTile.nextDirection(lastStepDirection)
            position = position.go(lastStepDirection)
            loopPositions.add(position)
            steps++
        }

        println(loopPositions.count() / 2)

        println(loopPositions)

        // Traverse again, now that we have the loop charted, and fire rays off to the sides.
        var outsideIsToTheLeft: Boolean? = null

        position = position.go(firstStepDirection)
        lastStepDirection = firstStepDirection

        while (world.at(position) != Tile.START) {
            val currentTile = world.at(position)
            val nextStepDirection = currentTile.nextDirection(lastStepDirection)

            var right = lastStepDirection.toRight()
            var rayPosition: Position

            rayPosition = position.go(right)
            while (rayPosition !in loopPositions && rayPosition.x in ewBounds && rayPosition.y in nsBounds) {
                println("RIGHT RAY $rayPosition")
                rightSidePositions.add(rayPosition)
                rayPosition = rayPosition.go(right)
                if (rayPosition.x !in ewBounds || rayPosition.y !in nsBounds) outsideIsToTheLeft = false
            }

            right = nextStepDirection.toRight()
            rayPosition = position.go(right)
            while (rayPosition !in loopPositions && rayPosition.x in ewBounds && rayPosition.y in nsBounds) {
                println("RIGHT RAY $rayPosition")
                rightSidePositions.add(rayPosition)
                rayPosition = rayPosition.go(right)
                if (rayPosition.x !in ewBounds || rayPosition.y !in nsBounds) outsideIsToTheLeft = false
            }

            var left = lastStepDirection.toLeft()
            rayPosition = position.go(left)
            while (rayPosition !in loopPositions && rayPosition.x in ewBounds && rayPosition.y in nsBounds) {
                println("LEFT RAY $rayPosition")
                leftSidePositions.add(rayPosition)
                rayPosition = rayPosition.go(left)
                if (rayPosition.x !in ewBounds || rayPosition.y !in nsBounds) outsideIsToTheLeft = true
            }

            left = nextStepDirection.toLeft()
            rayPosition = position.go(left)
            while (rayPosition !in loopPositions && rayPosition.x in ewBounds && rayPosition.y in nsBounds) {
                println("LEFT RAY $rayPosition")
                leftSidePositions.add(rayPosition)
                rayPosition = rayPosition.go(left)
                if (rayPosition.x !in ewBounds || rayPosition.y !in nsBounds) outsideIsToTheLeft = true
            }

            lastStepDirection = nextStepDirection
            position = position.go(lastStepDirection)
        }

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

        return if (outsideIsToTheLeft) rightSidePositions.size else leftSidePositions.size
    }

    solve(::part1, withInput = "day10/test", andAssert = 8)
    solve(::part1, withInput = "day10/input", andAssert = 7107)

    solve(::part2, withInput = "day10/test2", andAssert = 10)
    solve(::part2, withInput = "day10/input", andAssert = 281)
}
