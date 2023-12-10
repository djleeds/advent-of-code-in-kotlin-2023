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

    fun toRight() = entries[(ordinal + 1).rem(entries.size)]
    fun toLeft() = entries[(ordinal + 3).rem(entries.size)]
}

val Char.connectsToEast get() = this in listOf('F', '-', 'L')
val Char.connectsToSouth get() = this in listOf('F', '|', '7')
val Char.connectsToWest get() = this in listOf('J', '-', '7')
val Char.connectsToNorth get() = this in listOf('J', '|', 'L')

fun Char.nextDirection(lastDirection: Direction) = when (lastDirection) {
    NORTH -> when (this) {
        'F'  -> EAST
        '|'  -> NORTH
        '7'  -> WEST
        else -> throw IllegalStateException()
    }

    SOUTH -> when (this) {
        'L'  -> EAST
        '|'  -> SOUTH
        'J'  -> WEST
        else -> throw IllegalStateException()
    }

    EAST  -> when (this) {
        'J'  -> NORTH
        '-'  -> EAST
        '7'  -> SOUTH
        else -> throw IllegalStateException()
    }

    WEST  -> when (this) {
        'F'  -> SOUTH
        '-'  -> WEST
        'L'  -> NORTH
        else -> throw IllegalStateException()
    }
}

fun List<String>.at(position: Position) = at(position.x, position.y)
fun List<String>.at(x: Int, y: Int) = this[y][x]

fun main() {
    fun part1(input: List<String>): Int {
        val y = input.indexOfFirst { it.contains('S') }
        val x = input[y].indexOfFirst { it == 'S' }
        var position = Position(x, y)

        println("Start position = $position")

        val firstStepDirection = when {
            input.at(position.east).connectsToWest   -> EAST
            input.at(position.south).connectsToNorth -> SOUTH
            input.at(position.west).connectsToEast   -> WEST
            input.at(position.north).connectsToSouth -> NORTH
            else                                     -> throw IllegalStateException()
        }

        println("First step = $firstStepDirection")

        position = position.go(firstStepDirection)
        var steps = 1
        var lastStepDirection = firstStepDirection

        while (input.at(position) != 'S') {
            val currentTile = input.at(position)
            println(currentTile)
            lastStepDirection = currentTile.nextDirection(lastStepDirection)
            position = position.go(lastStepDirection)
            steps++
        }

        return steps / 2
    }

    fun part2(input: List<String>): Int {
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
            input.at(position.east).connectsToWest   -> EAST
            input.at(position.south).connectsToNorth -> SOUTH
            input.at(position.west).connectsToEast   -> WEST
            input.at(position.north).connectsToSouth -> NORTH
            else                                     -> throw IllegalStateException()
        }

        println("First step = $firstStepDirection")

        position = position.go(firstStepDirection)
        loopPositions.add(position)
        var steps = 1
        var lastStepDirection = firstStepDirection

        while (input.at(position) != 'S') {
            val currentTile = input.at(position)
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

        while (input.at(position) != 'S') {
            val currentTile = input.at(position)
            val nextStepDirection = currentTile.nextDirection(lastStepDirection)

            var right = lastStepDirection.toRight()
            var rayPosition = position.go(right)

            if (position.x == 15 && position.y == 1) {
                println("HERE")
            }

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
