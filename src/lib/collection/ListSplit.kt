package lib.collection

fun <T> List<T>.split(boundary: (T) -> Boolean): List<List<T>> =
    mapIndexed { index, value -> index.takeIf { boundary(value) } }
        .filterNotNull()
        .let { listOf(-1) + it + size }
        .windowed(2) { (first, second) -> subList(first + 1, second) }
