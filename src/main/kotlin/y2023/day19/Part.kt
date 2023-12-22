package y2023.day19

data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {

    val overall = x + m + a + s

    companion object {
        fun of(s: String) =
            Regex("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)\\}").matchEntire(s)?.destructured?.let { (x, m, a, s) ->
                Part(x.toInt(), m.toInt(), a.toInt(), s.toInt())
            } ?: throw IllegalArgumentException("part $s")
    }
}
