package y2023.day19

val DEFAULT_RANGE = 1..4000

data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {

    val overall = x + m + a + s

    companion object {
        fun of(s: String) =
            Regex("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)\\}").matchEntire(s)?.destructured?.let { (x, m, a, s) ->
                Part(x.toInt(), m.toInt(), a.toInt(), s.toInt())
            } ?: throw IllegalArgumentException("part $s")
    }
}

data class PartSpec(
    val x: IntRange = DEFAULT_RANGE,
    val m: IntRange = DEFAULT_RANGE,
    val a: IntRange = DEFAULT_RANGE,
    val s: IntRange = DEFAULT_RANGE,
) {
    private fun sizeOf(r: IntRange): Long = (r.last - r.first + 1).toLong()
    val size: Long by lazy { sizeOf(x) * sizeOf(m) * sizeOf(a) * sizeOf(s) }
}

data class Selection<T>(
    val selected: T,
    val remainder: T? = null
) {
    companion object {
        fun <T> selectSome(subset: T, remainder: T) = Selection(subset, remainder)
        infix fun <T> T.vs(remainder: T) = selectSome(this, remainder)

        fun greaterThan(v: Int): Selector<IntRange> = {
            if (it.first > v) Selection(it)
            else v + 1..it.last vs it.first..v
        }

        fun lessThan(v: Int): Selector<IntRange> = {
            if (it.last < v) Selection(it)
            else it.first..<v vs v..it.last
        }

        fun <T> all(): Selector<T> = { Selection(it) }
    }

    fun <R> map(t: (T) -> R) = Selection(
        selected.let(t),
        remainder?.let(t)
    )
}

typealias Selector<T> = (T) -> Selection<T>
typealias Test = (Part) -> Boolean

enum class Attribute(
    val letter: String,
    val getter: (Part) -> Int,
    val spec: (PartSpec) -> IntRange,
    val specSetter: PartSpec.(IntRange) -> PartSpec
) {
    X("x", Part::x, PartSpec::x, { copy(x = it) }),
    M("m", Part::m, PartSpec::m, { copy(m = it) }),
    A("a", Part::a, PartSpec::a, { copy(a = it) }),
    S("s", Part::s, PartSpec::s, { copy(s = it) });

    fun test(selector: Selector<IntRange>): Test {
        val range = selector(DEFAULT_RANGE).selected
        return { getter(it) in range }
    }

    fun restrict(selector: Selector<IntRange>): Selector<PartSpec> {
        return { spec ->
            selector(spec(spec)).map { spec.specSetter(it) }
        }
    }

    companion object {
        fun of(letter: String) = entries.first { it.letter == letter }
    }
}