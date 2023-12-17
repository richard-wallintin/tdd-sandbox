package y2023.day12

import y2023.day12.SpringState.Companion.toState

data class SpringRow(val states: List<SpringState>) {
    fun groups(stateList: List<SpringState> = states): DamagedGroups {
        return DamagedGroups(sequence {
            var c = 0
            stateList.forEach {
                if (it == SpringState.DAMAGED) {
                    c++
                } else if (c > 0) {
                    yield(c)
                    c = 0
                    if (it == SpringState.UNKNOWN)
                        return@sequence
                }
            }
            if (c > 0)
                yield(c)
        }.toList())
    }

    fun resolve() = resolveStates(states).map { SpringRow(it) }

    private fun resolveStates(states: List<SpringState>): Sequence<List<SpringState>> {
        val u = states.indexOfFirst { it == SpringState.UNKNOWN }
        return if (u == -1) {
            sequenceOf(states)
        } else {
            val head = states.subList(0, u)
            val tail = states.drop(u + 1)

            resolveStates(tail).flatMap {
                sequenceOf(
                    head + listOf(SpringState.DAMAGED) + it,
                    head + listOf(SpringState.OPERATIONAL) + it
                )
            }
        }
    }

    fun unfold(): SpringRow {
        return SpringRow((1..5).map { states }.reduce { a, b ->
            a + listOf(SpringState.UNKNOWN) + b
        })
    }

    fun resolveTo(target: DamagedGroups) = resolveStatesTo(states, target).map { SpringRow(it) }

    private fun resolveStatesTo(
        states: List<SpringState>,
        target: DamagedGroups
    ): Sequence<List<SpringState>> {
        val u = states.indexOfFirst { it == SpringState.UNKNOWN }
        return if (u == -1) {
            if (groups(states) == target) sequenceOf(states) else sequenceOf()
        } else {
            // TODO micro-optimize here: use subList AND dont re-build the list
            val head = states.take(u)
            val tail = states.drop(u + 1)

            // TODO optimize here ---> drop more states earlier
            sequenceOf(
                head + listOf(SpringState.DAMAGED),
                head + listOf(SpringState.OPERATIONAL)
            )
                .filter { groups(it) in target }
                .map { it + tail }
                .flatMap { resolveStatesTo(it, target) }
        }
    }

    fun resolveCount(groups: DamagedGroups) = Resolver.resolveCount(
        0,
        states,
        groups.groups
    )

    companion object {
        fun parse(line: String) = SpringRow(line.asIterable().map { it.toState() })
    }
}

object Resolver {
    data class Step(
        val currentGroup: Int,
        val states: List<SpringState>,
        val missingGroups: List<Int>
    )

    private val cache = mutableMapOf<Step, Long>()

    fun resolveCount(
        currentGroup: Int,
        states: List<SpringState>,
        missingGroups: List<Int>
    ) = cache.getOrPut(
        Step(currentGroup, states, missingGroups)
    ) {
        actualResolveCount(currentGroup, states, missingGroups)
    }

    private fun actualResolveCount(
        currentGroup: Int,
        states: List<SpringState>,
        missingGroups: List<Int>
    ): Long {
        if (currentGroup > 0) {
            val expectedGroup = missingGroups.firstOrNull() ?: return 0

            if (currentGroup > expectedGroup)
                return 0

            if (states.isEmpty()) {
                return resolveCount(0, states, missingGroups.dropLast(1))
            }

            val head = states.first()
            val tail = states.subList(1, states.size)

            return when (head) {
                SpringState.UNKNOWN -> continueGroup(currentGroup, tail, missingGroups) +
                        closeGroup(
                            currentGroup,
                            expectedGroup,
                            tail,
                            missingGroups
                        )

                SpringState.OPERATIONAL -> closeGroup(
                    currentGroup,
                    expectedGroup,
                    tail,
                    missingGroups
                )

                SpringState.DAMAGED -> continueGroup(currentGroup, tail, missingGroups)
            }
        }

        if (states.isEmpty()) {
            return if (missingGroups.isEmpty()) 1
            else 0
        }

        if (states.count { it != SpringState.OPERATIONAL } < missingGroups.sum())
            return 0

        val head = states.first()
        val tail = states.subList(1, states.size)

        return when (head) {
            SpringState.DAMAGED -> startGroup(tail, missingGroups)
            SpringState.OPERATIONAL -> noGroup(tail, missingGroups)
            SpringState.UNKNOWN -> startGroup(tail, missingGroups) + noGroup(tail, missingGroups)
        }
    }

    private fun continueGroup(
        currentGroup: Int,
        tail: List<SpringState>,
        missingGroups: List<Int>
    ) = resolveCount(
        currentGroup + 1,
        tail,
        missingGroups
    )

    private fun startGroup(
        tail: List<SpringState>,
        missingGroups: List<Int>
    ) = resolveCount(1, tail, missingGroups)

    private fun closeGroup(
        currentGroup: Int,
        nextGroup: Int,
        tail: List<SpringState>,
        missingGroups: List<Int>
    ) = if (currentGroup != nextGroup) 0
    else resolveCount(0, tail, missingGroups.drop(1))

    private fun noGroup(
        tail: List<SpringState>,
        missingGroups: List<Int>
    ) = resolveCount(
        0, tail, missingGroups
    )
}