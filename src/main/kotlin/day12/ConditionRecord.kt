package day12

import util.split

data class ConditionRecord(val row: SpringRow, val groups: DamagedGroups) {
    fun countResolutions() = row.resolveCount(groups)

    fun unfold(): ConditionRecord {
        return copy(
            row = row.unfold(),
            groups = groups.unfold()
        )
    }

    companion object {
        fun parse(line: String): ConditionRecord {
            val (states, counts) = line.split()
            return ConditionRecord(SpringRow.parse(states), DamagedGroups.parse(counts))
        }

        fun String.totalResolutions(unfold: Boolean = false): Long =
            lines().parallelStream().map { parse(it) }
                .map { if (unfold) it.unfold() else it }
                .map { it.countResolutions() }
                .toList().sum()

    }
}
