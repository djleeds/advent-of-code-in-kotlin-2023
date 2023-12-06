package lib.collection

fun Iterable<Int>.product(): Int {
    var result = 1
    for (element in this) {
        result *= element
    }
    return result
}
