package lib.parser

fun <T1, T2, T3, T4, T5> String.parse(block: Parser5<T1, T2, T3, T4, T5>.() -> Unit): ParseResult5<T1, T2, T3, T4, T5> {
    return Parser5<T1, T2, T3, T4, T5>(this).apply(block).result()
}

open class ParseResult5<out T1, out T2, out T3, out T4, out T5>(
    item1: T1,
    item2: T2,
    item3: T3,
    item4: T4,
    val item5: T5
) : ParseResult4<T1, T2, T3, T4>(item1, item2, item3, item4) {
    operator fun component5() = item5
}

open class Parser5<T1, T2, T3, T4, T5>(string: String) : Parser4<T1, T2, T3, T4>(string) {
    protected var item5: T5? = null
    fun result5(item: T5) = run { item5 = item }
    override fun result() = ParseResult5(item1!!, item2!!, item3!!, item4!!, item5!!)
}

context(Parser5<T1, T2, T3, T4, T5>)
fun <T1, T2, T3, T4, T5> ParseSection.result5(transform: String.() -> T5) = result5(transform(value))

context(Parser5<T1, T2, T3, T4, String>)
fun <T1, T2, T3, T4> ParseSection.result5() = result5(value)

context(Parser5<T1, T2, T3, T4, List<T>>)
fun <T1, T2, T3, T4, T> ParseSections.resultMany5(transform: String.() -> T) =
    values.map { transform(it) }.also(::result5)

context(Parser5<T1, T2, T3, T4, List<String>>)
fun <T1, T2, T3, T4> ParseSections.resultMany5() = result5(values)
