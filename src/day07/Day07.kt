package day07

import lib.parser.parse
import lib.parser.result1
import lib.parser.result2
import solve

object Rules {
    var jokersWild = false
}

enum class Card {
    CW, C2, C3, C4, C5, C6, C7, C8, C9, CT, CJ, CQ, CK, CA;

    val label: Char = name[1]
    operator fun times(count: Int) = List(count) { this }

    companion object {
        fun from(char: Char) =
            entries.first { it.label == char }.takeUnless { Rules.jokersWild && it == CJ } ?: CW
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
            entries.first { it.sortedCardCounts == handCardCount }
    }
}

fun List<Card>.wildsReplacedWith(card: Card) = map { if (it == Card.CW) card else it }

class Hand(private val cards: List<Card>) : Comparable<Hand> {
    private val cardGroups: Map<Card, Int> = cards.groupBy { it }.mapValues { it.value.count() }
    private val type: HandType = HandType.from(cardGroups.values.sortedDescending())
    private val withWildsApplied: Hand
        get() {
            val bestCard = cardGroups.filter { it.key != Card.CW }.maxByOrNull { it.value }?.key ?: Card.CA
            return Hand(cards.wildsReplacedWith(bestCard))
        }

    override fun compareTo(other: Hand): Int =
        compareTo(other) { if (Rules.jokersWild) it.withWildsApplied.type else it.type }

    private fun compareTo(other: Hand, typeProvider: (Hand) -> HandType): Int = when {
        typeProvider(this) < typeProvider(other) -> -1
        typeProvider(this) > typeProvider(other) -> 1
        else                                     -> compareBySecondOrderingRule(other)
    }

    private fun compareBySecondOrderingRule(other: Hand): Int =
        cards.zip(other.cards).firstOrNull { it.first != it.second }?.let { it.first.compareTo(it.second) } ?: 0

    override fun toString() = String(cards.map { it.label }.toCharArray())
}

data class HandAndBid(val hand: Hand, val bid: Int) {
    companion object {
        fun from(line: String): HandAndBid {
            val (hand, bid) = line.parse<Hand, Int> {
                split(" ") {
                    left.result1 { map(Card::from).let(::Hand) }
                    right.result2 { toInt() }
                }
            }
            return HandAndBid(hand, bid)
        }
    }
}

fun main() {
    fun solve(input: List<String>): Int = input
        .map(HandAndBid::from)
        .sortedBy { it.hand }
        .foldIndexed(0) { index, acc, item -> acc + ((index + 1) * item.bid) }

    fun part1(input: List<String>): Int {
        Rules.jokersWild = false
        return solve(input)
    }

    fun part2(input: List<String>): Int {
        Rules.jokersWild = true
        return solve(input)
    }

    solve(::part1, withInput = "day07/test", andAssert = 6440)
    solve(::part1, withInput = "day07/input", andAssert = 250120186)

    solve(::part2, withInput = "day07/test", andAssert = 5905)
    solve(::part2, withInput = "day07/input", andAssert = 250665248)
}
