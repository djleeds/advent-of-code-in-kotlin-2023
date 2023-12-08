package lib.math

import lib.collection.product

fun List<Long>.leastCommonMultiple(): Long {
    val gcd = greatestCommonDivisor()!!
    val product = map { it / gcd }.product()
    return gcd * product
}

fun List<Long>.greatestCommonDivisor() =
    (max() downTo 1).firstOrNull { i -> all { it.rem(i) == 0L } }
