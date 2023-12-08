package lib.collection

fun Iterable<Int>.product(): Int {
    var result = 1
    for (element in this) {
        result *= element
    }
    return result
}

fun Iterable<Long>.product(): Long {
    var result = 1L
    for (element in this) {
        result *= element
    }
    return result
}
