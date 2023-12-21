package y2023.day18

import util.CardinalDirection

data class Instruction(val direction: CardinalDirection, val meters: Int, val color: String) {
    companion object {
        fun of(s: String): Instruction {
            return Regex("(\\w) (\\d+) \\((#\\w+)\\)").matchEntire(s)?.destructured?.let { (dir, meters, color) ->
                Instruction(
                    direction = CardinalDirection.of(dir),
                    meters = meters.toInt(),
                    color = color
                )
            } ?: throw IllegalArgumentException("no instruction '$s'")
        }

        fun ofMany(lines: String) = lines.lineSequence().map { of(it) }
    }
}