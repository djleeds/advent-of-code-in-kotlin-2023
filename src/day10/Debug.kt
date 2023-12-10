package day10

fun debug(
    leftSidePositions: MutableSet<Position>,
    rightSidePositions: MutableSet<Position>,
    input: List<String>,
    loopPositions: Set<Position>,
    travelingClockwise: Boolean?
) {
    if (debug) {
        println("Left Positions = $leftSidePositions")
        println("Right Positions = $rightSidePositions")

        println("Left Positions Count = ${leftSidePositions.size}")
        println("Right Positions Count = ${rightSidePositions.size}")

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                when (Position(x, y)) {
                    in loopPositions      -> print(char)
                    in leftSidePositions  -> print("O")
                    in rightSidePositions -> print("I")
                    else                  -> print(".")
                }
            }
            println()
        }

        println("Outside is on the ${if (travelingClockwise!!) "left" else "right"}")
    }
}
