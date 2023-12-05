package lib.parser

abstract class Parser(private val string: String) : IParseSection by ParseSection(string)


interface IParseSection {
    fun split(vararg delimiters: String, block: ParseSections.() -> Unit)
}

class ParseSection(private val string: String) : IParseSection {
    val value = string

    override fun split(vararg delimiters: String, block: ParseSections.() -> Unit) {
        split(*delimiters).apply(block)
    }

    fun split(vararg delimiters: String): ParseSections =
        string
            .split(*delimiters)
            .filter { it.isNotEmpty() }
            .map(::ParseSection)
            .let(::ParseSections)
}

@Suppress("MemberVisibilityCanBePrivate")
class ParseSections(private val sections: List<ParseSection>) {
    val values = sections.map { it.value }

    val left get() = sections[0]
    val right get() = sections[1]

    val first get() = sections[0]
    val second get() = sections[1]
    val third get() = sections[2]
    val fourth get() = sections[3]
    val fifth get() = sections[4]
}
