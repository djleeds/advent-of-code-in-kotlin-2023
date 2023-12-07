package day07

import lib.parser.parse
import lib.parser.result1
import lib.parser.result2
import solve

enum class Card {
    C2, C3, C4, C5, C6, C7, C8, C9, CT, CJ, CQ, CK, CA;

    val label: Char = name[1];

    companion object {
        fun from(char: Char) = entries.first { it.label == char }
    }
}

enum class HandType(vararg sortedCardCount: Int) {
    HIGH_CARD(1, 1, 1, 1, 1),
    ONE_PAIR(2, 1, 1, 1),
    TWO_PAIR(2, 2, 1),
    THREE_OF_A_KIND(3, 1, 1),
    FULL_HOUSE(3, 2),
    FOUR_OF_A_KIND(4, 1),
    FIVE_OF_A_KIND(5);

    private val sortedCardCounts = sortedCardCount.toList()

    companion object {
        fun from(handCardCount: List<Int>) =
            entries.firstOrNull { it.sortedCardCounts == handCardCount }
                ?: throw IllegalStateException("Input $handCardCount did not match any hand types.")
    }
}

data class Hand(val cards: List<Card>) : Comparable<Hand> {
    private val type = HandType.from(cards.groupBy { it }.mapValues { it.value.count() }.values.sortedDescending())

    override fun compareTo(other: Hand): Int {
        return when {
            type < other.type -> -1
            type > other.type -> 1
            else              ->
                cards
                    .zip(other.cards)
                    .firstOrNull { it.first != it.second }
                    ?.let { it.first.compareTo(it.second) }
                    ?: 0
        }
    }
}

data class HandAndBid(val hand: Hand, val bid: Int) {
    companion object {
        fun from(line: String) = line.parse<Hand, Int> {
            split(" ") {
                left.result1 { map(Card::from).let(::Hand) }
                right.result2 { toInt() }
            }
        }.let { (hand, bid) -> HandAndBid(hand, bid) }
    }
}

fun main() {
    fun part1(input: List<String>): Int = input
        .map(HandAndBid::from)
        .sortedBy { it.hand }
        .foldIndexed(0) { index, acc, item -> acc + ((index + 1) * item.bid) }

    fun part2(input: List<String>): Int = -1

    solve(::part1, withInput = "day07/test", andAssert = 6440)
    solve(::part1, withInput = "day07/input", andAssert = null)

    //solve(::part2, withInput = "day07/test", andAssert = null)
    //solve(::part2, withInput = "day07/input", andAssert = null)
}
