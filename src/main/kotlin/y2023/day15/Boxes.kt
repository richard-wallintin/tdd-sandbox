package y2023.day15

data class Boxes(val boxes: List<Box> = List(256) { Box() }) {

    operator fun get(boxNumber: Int): Box {
        return boxes[boxNumber]
    }

    fun modify(boxNumber: Int, f: (Box) -> Box) = copy(
        boxes = boxes.mapIndexed { index, box -> if (index == boxNumber) f(box) else box }
    )

    fun focusingPower(): Int {
        return boxes.mapIndexed { i, b -> b.focusingPower(i) }.sum()
    }

    data class Lens(val label: String, val focalLength: Int) {
        fun focusingPower(boxNumber: Int, lensIndex: Int): Int {
            return (boxNumber + 1) * (lensIndex + 1) * focalLength
        }
    }

    data class Box(val lenses: List<Lens> = emptyList()) {
        fun addOrReplace(lens: Lens): Box {
            val i = lenses.indexOfFirst { it.label == lens.label }
            return if (i == -1)
                copy(lenses = lenses + lens)
            else copy(
                lenses = lenses.map { l ->
                    if (lens.label == l.label) lens
                    else l
                }
            )
        }

        fun remove(label: String): Box {
            return copy(lenses = lenses.filter { it.label != label })
        }

        fun focusingPower(boxNumber: Int): Int {
            return lenses.mapIndexed { i, l ->
                l.focusingPower(boxNumber, i)
            }.sum()
        }
    }
}
