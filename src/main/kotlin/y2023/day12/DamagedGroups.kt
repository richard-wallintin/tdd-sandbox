package y2023.day12

data class DamagedGroups(val groups: List<Int>) {
    fun unfold(): DamagedGroups {
        return DamagedGroups((1..5).map { groups }.reduce(List<Int>::plus))
    }

    operator fun contains(partial: DamagedGroups): Boolean {
        if(partial.groups.size > this.groups.size)
            return false

        val pairs = partial.groups.zip(this.groups)

        val headMatches = pairs.dropLast(1).all { (check, ref) ->
            check == ref
        }
        if (!headMatches) return false

        return pairs.lastOrNull()?.let { (check, ref) -> check <= ref } ?: true
    }

    companion object {
        fun parse(numbers: String) = DamagedGroups(numbers.split(Regex(",")).map { it.toInt() })
    }
}
