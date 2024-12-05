package y2024

import util.integers

data class Rule(val before: Int, val after: Int) {
    companion object {
        fun parse(text: String): List<Rule> {
            return text.lineSequence().takeWhile { it.isNotBlank() }.map {
                val (a, b) = it.split('|').integers()
                a mustBeBefore b
            }.toList()
        }

        infix fun Int.mustBeBefore(other: Int): Rule {
            return Rule(this, other)
        }

        fun List<Update>.filter(rules: List<Rule>): List<Update> {
            return filter { update ->
                rules.all { it.isValid(update) }
            }
        }

        fun readValidUpdates(text: String) = Update.parse(text)
            .filter(parse(text))
    }

    fun isValid(update: Update) =
        if (update.pages.contains(before) && update.pages.contains(after)) {
            update.pages.indexOf(before) < update.pages.indexOf(after)
        } else true
}