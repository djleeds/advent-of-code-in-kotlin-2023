package lib.parser

fun <T1> String.parse(block: Parser1<T1>.() -> Unit): ParseResult1<T1> {
    return Parser1<T1>(this).apply(block).result()
}

open class ParseResult1<out T1>(val item1: T1) {
    operator fun component1() = item1
}

open class Parser1<T1>(string: String) : Parser(string) {
    protected var item1: T1? = null
    fun result1(item: T1) = run { item1 = item }
    open fun result() = ParseResult1(item1!!)
}

context(Parser1<T>)
fun <T> ParseSection.result1(transform: String.() -> T) = result1(transform(value))

context(Parser1<String>)
fun ParseSection.result1() = result1(value)

context(Parser1<List<T>>)
fun <T> ParseSections.resultMany1(transform: String.() -> T) =
    values.map { transform(it) }.also(::result1)

context(Parser1<List<String>>)
fun ParseSections.resultMany1() = result1(values)
