package lib.parser

fun <T1, T2, T3, T4> String.parse(block: Parser4<T1, T2, T3, T4>.() -> Unit): ParseResult4<T1, T2, T3, T4> {
    return Parser4<T1, T2, T3, T4>(this).apply(block).result()
}

open class ParseResult4<out T1, out T2, out T3, out T4>(
    item1: T1,
    item2: T2,
    item3: T3,
    val item4: T4
) : ParseResult3<T1, T2, T3>(item1, item2, item3) {
    operator fun component4() = item4
}

open class Parser4<T1, T2, T3, T4>(string: String) : Parser3<T1, T2, T3>(string) {
    protected var item4: T4? = null
    fun result4(item: T4) = run { item4 = item }
    override fun result() = ParseResult4(item1!!, item2!!, item3!!, item4!!)
}

context(Parser4<T1, T2, T3, T4>)
fun <T1, T2, T3, T4> ParseSection.result4(transform: String.() -> T4) = result4(transform(value))

context(Parser4<T1, T2, T3, String>)
fun <T1, T2, T3> ParseSection.result4() = result4(value)

context(Parser4<T1, T2, T3, List<T>>)
fun <T1, T2, T3, T> ParseSections.resultMany4(transform: String.() -> T) =
    values.map { transform(it) }.also(::result4)

context(Parser4<T1, T2, T3, List<String>>)
fun <T1, T2, T3> ParseSections.resultMany4() = result4(values)
