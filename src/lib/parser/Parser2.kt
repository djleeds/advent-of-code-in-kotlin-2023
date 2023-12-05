package lib.parser

fun <T1, T2> String.parse(block: Parser2<T1, T2>.() -> Unit): ParseResult2<T1, T2> {
    return Parser2<T1, T2>(this).apply(block).result()
}

open class ParseResult2<out T1, out T2>(item1: T1, val item2: T2) : ParseResult1<T1>(item1) {
    operator fun component2() = item2
}

open class Parser2<T1, T2>(string: String) : Parser1<T1>(string) {
    protected var item2: T2? = null
    fun result2(item: T2) = run { item2 = item }
    override fun result() = ParseResult2(item1!!, item2!!)
}

context(Parser2<T1, T2>)
fun <T1, T2> ParseSection.result2(transform: String.() -> T2) = result2(transform(value))

context(Parser2<T1, String>)
fun <T1> ParseSection.result2() = result2(value)

context(Parser2<T1, List<T>>)
fun <T1, T> ParseSections.resultMany2(transform: String.() -> T) =
    values.map { transform(it) }.also(::result2)

context(Parser2<T1, List<String>>)
fun <T1> ParseSections.resultMany2() = result2(values)
