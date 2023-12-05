package lib.parser

fun main() {
    val text = "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53"
    val (id, winners, numbers, label) = text.parse<Int, List<String>, List<Int>, String> {
        split(":") {
            left.split(" ") {
                left.result4()
                right.result1 { toInt() }
            }
            right.split("|") {
                left.split(" ").resultMany2()
                right.split(" ").resultMany3 { toInt() }
            }
        }
    }
    println(
        """
        First = $id
        Second = $winners
        Third = $numbers
        Fourth = $label
    """.trimIndent()
    )
}
