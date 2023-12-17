package y2023.day14

private const val ROCK = 'O'
private const val BLOCK = '#'
private const val SPACE = '.'

data class Lane(val elements: String) {
    fun tilt(): Lane {
        return copy(
            elements = elements.split(BLOCK).joinToString(BLOCK.toString()) { tilt(it) }
        )
    }

    private fun tilt(segment: String): String {
        val rocks = segment.count { it == ROCK }
        return ROCK.repeat(rocks) + SPACE.repeat(segment.length - rocks)
    }

    private val size = elements.length

    val load: Int = elements.mapIndexed { i, c ->
        if (c == ROCK) (elements.length - i) else 0
    }.sum()

    companion object {
        fun of(text: String): Lane {
            return Lane(text)
        }

        fun List<Lane>.transpose(): List<Lane> = (0 until first().size)
            .map { c -> map { it[c] }.toCharArray().concatToString().let(::Lane) }
    }

    private operator fun get(i: Int) = elements[i]
}

private fun Char.repeat(n: Int) = toString().repeat(n)
