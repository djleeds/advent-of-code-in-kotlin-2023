package lib.regex

fun String.extractGroupsFrom(regex: Regex): List<String> =
    extractGroupsFromOrNull(regex) ?: throw NoSuchElementException("Processing [$this]")

fun String.extractGroupsFromOrNull(regex: Regex): List<String>? =
    regex.matchEntire(this)?.groupValues?.drop(1)
