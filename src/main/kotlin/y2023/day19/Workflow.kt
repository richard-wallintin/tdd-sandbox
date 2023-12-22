package y2023.day19

data class Workflow(
    val name: String,
    val rules: List<Rule>
) {
    fun process(part: Part): String {
        return rules.first { it.test(part) }.result
    }

    companion object {
        fun of(s: String) =
            Regex("(\\w+)\\{(.+?)\\}").matchEntire(s)?.destructured?.let { (name, rules) ->
                Workflow(name = name, rules = rules.split(',').map { rule(it) })
            } ?: throw IllegalArgumentException("workflow '$s' broken")

        private fun rule(s: String) =
            Regex("([xmas])([<>])(\\d+):(\\w+)").matchEntire(s)?.destructured?.let { (a, c, v, r) ->
                Rule(r, predicate(a, c, v.toInt()))
            } ?: Rule(s) { true }

        private fun predicate(
            attribute: String,
            comparison: String,
            value: Int
        ): (Part) -> Boolean {
            val getter: (Part) -> Int = when (attribute) {
                "x" -> Part::x
                "m" -> Part::m
                "a" -> Part::a
                "s" -> Part::s
                else -> throw IllegalArgumentException("attribute $attribute")
            }
            val check: (Int) -> Boolean = when (comparison) {
                ">" -> { i -> i > value }
                "<" -> { i -> i < value }
                else -> throw IllegalArgumentException("comparison $comparison")
            }
            return { p -> check(getter(p)) }
        }
    }
}

data class Rule(val result: String, val test: (Part) -> Boolean)