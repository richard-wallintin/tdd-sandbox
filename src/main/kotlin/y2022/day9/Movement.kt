package y2022.day9

import util.CardinalDirection
import util.split

data class Movement(val d: CardinalDirection, val steps: Int) {
    fun applyTo(r: AbstractRope) = trace(r).last()
    fun trace(start: AbstractRope) = toDirections().runningFold(start, AbstractRope::move)

    private fun toDirections() = (1..steps).asSequence().map { d }

    companion object {
        fun parseMany(text: String): Sequence<Movement> {
            return text.lineSequence().map { of(it) }
        }

        fun of(line: String) =
            line.split().let { (d, s) -> Movement(CardinalDirection.of(d), s.toInt()) }


        fun Sequence<Movement>.trace(start: AbstractRope) =
            flatMap { it.toDirections() }
                .runningFold(start, AbstractRope::move)

        fun Sequence<Movement>.traceTail(start: AbstractRope) = trace(start).map { it.tail }.toSet().size
    }

}
