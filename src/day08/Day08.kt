package day08

import lib.math.leastCommonMultiple
import lib.regex.extractGroupsFrom
import solve

data class Document(val directions: String, val network: Map<String, Pair<String, String>>) {
    fun countSteps(start: String, destination: String): Long =
        countSteps(start) { it == destination }

    fun countSteps(start: String, destination: (String) -> Boolean): Long {
        var stepCount = 0L
        var currentNode = start

        do {
            val nextDirection = directions[stepCount.rem(directions.length).toInt()]
            val options = network[currentNode]!!
            stepCount++
            currentNode = if (nextDirection == 'L') options.first else options.second
        } while (!destination(currentNode))

        return stepCount
    }

    companion object {
        private val regex = Regex("([A-Z0-9]{3}) = \\(([A-Z0-9]{3}), ([A-Z0-9]{3})\\)")
        fun parse(input: List<String>) = Document(
            input[0],
            input.drop(2).map { it.extractGroupsFrom(regex) }
                .associate { (name, left, right) -> name to (left to right) }
        )
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        return Document.parse(input).countSteps("AAA", "ZZZ")
    }

    fun part2(input: List<String>): Long {
        val document = Document.parse(input)
        val stepTotals = document.network.keys
            .filter { it.endsWith("A") }
            .map { start -> document.countSteps(start) { it.endsWith("Z") } }

        return stepTotals.leastCommonMultiple()
    }

    solve(::part1, withInput = "day08/test", andAssert = 6)
    solve(::part1, withInput = "day08/input", andAssert = 19099)

    solve(::part2, withInput = "day08/test2", andAssert = 6)
    solve(::part2, withInput = "day08/input", andAssert = 17099847107071L)
}
