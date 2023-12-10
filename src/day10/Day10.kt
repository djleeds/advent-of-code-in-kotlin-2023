package day10

import solve

data class Position(val x: Int, val y: Int) {
    val east get() = copy(x = x + 1)
    val south get() = copy(y = y + 1)
    val west get() = copy(x = x - 1)
    val north get() = copy(y = y - 1)
    fun go(direction: Direction) = when (direction) {
        Direction.NORTH -> north
        Direction.SOUTH -> south
        Direction.EAST  -> east
        Direction.WEST  -> west
    }
}

enum class Direction { NORTH, SOUTH, EAST, WEST }

val Char.connectsToEast get() = this in listOf('F', '-', 'L')
val Char.connectsToSouth get() = this in listOf('F', '|', '7')
val Char.connectsToWest get() = this in listOf('J', '-', '7')
val Char.connectsToNorth get() = this in listOf('J', '|', 'L')

fun Char.nextDirection(lastDirection: Direction) = when (lastDirection) {
    Direction.NORTH -> when (this) {
        'F'  -> Direction.EAST
        '|'  -> Direction.NORTH
        '7'  -> Direction.WEST
        else -> throw IllegalStateException()
    }

    Direction.SOUTH -> when (this) {
        'L'  -> Direction.EAST
        '|'  -> Direction.SOUTH
        'J'  -> Direction.WEST
        else -> throw IllegalStateException()
    }

    Direction.EAST  -> when (this) {
        'J'  -> Direction.NORTH
        '-'  -> Direction.EAST
        '7'  -> Direction.SOUTH
        else -> throw IllegalStateException()
    }

    Direction.WEST  -> when (this) {
        'F'  -> Direction.SOUTH
        '-'  -> Direction.WEST
        'L'  -> Direction.NORTH
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
            input.at(position.east).connectsToWest   -> Direction.EAST
            input.at(position.south).connectsToNorth -> Direction.SOUTH
            input.at(position.west).connectsToEast   -> Direction.WEST
            input.at(position.north).connectsToSouth -> Direction.NORTH
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

    fun part2(input: List<String>): Int = -1

    solve(::part1, withInput = "day10/test", andAssert = 8)
    solve(::part1, withInput = "day10/input", andAssert = null)

    //solve(::part2, withInput = "day10/test", andAssert = null)
    //solve(::part2, withInput = "day10/input", andAssert = null)
}
