package y2024.day12

import util.Grid

data class Garden(val regions: Set<Region> = emptySet()) {

    val totalPrice: Int by lazy { regions.sumOf { it.price } }

    companion object {
        fun parse(text: String) = of(Grid.charGridOf(text))

        fun of(grid: Grid<Char>): Garden {
            val unassigned = grid.extent.innerPoints().toMutableSet()
            val regions = mutableSetOf<Region>()
            while (unassigned.isNotEmpty()) {
                val start = unassigned.first().also { unassigned.remove(it) }
                val plant = grid[start]

                regions.add(Region(start.traverse { grid[it] == plant }
                    .onEach { unassigned.remove(it) }.toSet()))
            }

            return Garden(regions.toSet())
        }
    }
}
