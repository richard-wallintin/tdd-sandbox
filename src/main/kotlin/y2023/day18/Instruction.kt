package y2023.day18

import util.CardinalDirection

data class Instruction(val direction: CardinalDirection, val units: Int) {
    companion object {
        fun of(s: String, hex: Boolean = false): Instruction {
            return Regex("(\\w) (\\d+) \\(#(\\w+)\\)").matchEntire(s)?.destructured?.let { (dir, meters, hexCode) ->
                if (hex) {
                    Instruction(
                        direction = directionFromHex(hexCode.last()),
                        units = hexCode.substring(0, 5).toInt(16)
                    )
                } else
                    Instruction(
                        direction = CardinalDirection.of(dir),
                        units = meters.toInt()
                    )
            } ?: throw IllegalArgumentException("no instruction '$s'")
        }

        private fun directionFromHex(c: Char): CardinalDirection {
            return when (c) {
                '0' -> CardinalDirection.E
                '1' -> CardinalDirection.S
                '2' -> CardinalDirection.W
                '3' -> CardinalDirection.N
                else -> throw IllegalArgumentException("unknown hex direction $c")
            }
        }

        fun ofMany(lines: String, hex: Boolean = false) = lines.lineSequence().map { of(it, hex) }
    }
}