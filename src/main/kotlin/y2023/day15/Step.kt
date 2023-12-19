package y2023.day15

import y2023.day15.Boxes.Lens

data class Step(val label: String, val focalLength: Int) {
    fun applyTo(boxes: Boxes) = boxes.modify(boxNumber) {
        if (focalLength < 1) it.remove(label)
        else it.addOrReplace(Lens(label, focalLength))
    }

    val boxNumber = hash(label)

    companion object {
        fun of(s: String): Step {
            Regex("(\\w+)=(\\d)").matchEntire(s)?.destructured?.let { (label, focalLength) ->
                return Step(label, focalLength.toInt())
            }
            Regex("(\\w+)-").matchEntire(s)?.destructured?.let { (label) ->
                return Step(label, -1)
            }
            throw IllegalArgumentException("step? '$s'")
        }

        fun hash(s: String): Int {
            return s.fold(0) { v, c -> v.plus(c.code).times(17).mod(256) }
        }

        fun verificationNumber(procedure: String) = procedure.split(Regex(",")).sumOf {
            hash(it)
        }

        fun ofMany(procedure: String): Sequence<Step> {
            return procedure.split(Regex(",")).asSequence().map { of(it) }
        }

        fun Sequence<Step>.applyTo(boxes: Boxes = Boxes()) = fold(boxes) { b, s -> s.applyTo(b) }
    }
}
