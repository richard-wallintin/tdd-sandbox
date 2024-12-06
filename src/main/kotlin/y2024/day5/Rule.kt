package y2024.day5

import util.integers

data class Rule(val before: Int, val after: Int) : Comparator<Int> {
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

        fun List<Update>.findInvalid(rules: List<Rule>): List<Update> {
            return filter { update ->
                !rules.all { it.isValid(update) }
            }
        }

        fun readValidUpdates(text: String) = Update.parse(text)
            .filter(parse(text))

        fun List<Rule>.adjust(update: Update): Update {
            return Update(
                update.pages.sortedWith(
                    this.reduce(Comparator<Int>::thenComparing)
                )
            )
        }

        fun adjustInvalidUpdates(text: String): List<Update> {
            val rules = parse(text)
            val allUpdates = Update.parse(text)

            return allUpdates.findInvalid(rules).map { rules.adjust(it) }
        }
    }

    fun isValid(update: Update) =
        if (update.pages.contains(before) && update.pages.contains(after)) {
            update.pages.indexOf(before) < update.pages.indexOf(after)
        } else true

    override fun compare(a: Int, b: Int): Int {
        return if (a == before && b == after) -1
        else if (a == after && b == before) +1
        else 0
    }
}