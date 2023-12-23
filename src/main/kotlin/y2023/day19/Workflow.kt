package y2023.day19

import y2023.day19.Selection.Companion.greaterThan
import y2023.day19.Selection.Companion.lessThan

data class Workflow(
    val name: String,
    val rules: List<Rule>
) {
    fun process(part: Part): String {
        return rules.first { it.test(part) }.result
    }

    fun process(spec: PartSpec): Sequence<Pair<PartSpec, String>> = sequence {
        var remaining: PartSpec = spec
        rules.forEach {
            val (selected, rest) = it.select(remaining)
            yield(selected to it.result)
            remaining = rest ?: return@sequence
        }
    }

    companion object {
        fun of(s: String) =
            Regex("(\\w+)\\{(.+?)\\}").matchEntire(s)?.destructured?.let { (name, rules) ->
                Workflow(name = name, rules = rules.split(',').map { rule(it) })
            } ?: throw IllegalArgumentException("workflow '$s' broken")

        private fun rule(s: String) =
            Regex("([xmas])([<>])(\\d+):(\\w+)").matchEntire(s)?.destructured?.let { (a, c, v, r) ->
                Rule.condition(Attribute.of(a), selector(c, v.toInt()), r)
            } ?: Rule(s)

        private fun selector(comparison: String, value: Int): Selector<IntRange> {
            return when (comparison) {
                ">" -> greaterThan(value)
                "<" -> lessThan(value)
                else -> throw IllegalArgumentException("comparison $comparison")
            }
        }
    }
}

data class Rule(
    val result: String,
    val select: Selector<PartSpec> = Selection.all(),
    val test: Test = { true }
) {

    companion object {
        fun condition(attribute: Attribute, selector: Selector<IntRange>, result: String): Rule {
            return Rule(result, attribute.restrict(selector), attribute.test(selector))
        }
    }
}