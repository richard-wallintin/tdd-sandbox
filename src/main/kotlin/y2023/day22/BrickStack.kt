package y2023.day22

import java.util.*

data class BrickStack(val settled: List<Brick>) {
    private val supportMap: Map<Brick, List<Brick>> by lazy {
        sequence {
            var remaining = settled
            while (remaining.isNotEmpty()) {
                val brick = remaining.first()
                remaining = remaining.drop(1)
                yield(brick to brick.support(remaining))
            }
        }.toMap()
    }
    private val supportedMap = mutableMapOf<Brick, Set<Brick>>()

    fun support(b: Brick) = supportMap[b] ?: emptyList()
    private fun supported(b: Brick): Set<Brick> = supportedMap.getOrPut(b) {
        supportMap.filter { b in it.value }.keys
    }

    fun supportedCount(b: Brick) = supported(b).size

    fun safeCount() = settled.count { safeCount(it) }

    private fun safeCount(b: Brick): Boolean {
        return support(b).all { supportedCount(it) > 1 }
    }

    private fun falling(start: Brick): Set<Brick> {
        val fallen = mutableSetOf(start)

        val q: Queue<Brick> = LinkedList()
        q.offer(start)
        while(q.isNotEmpty()){
            val b = q.remove()
            support(b).filter {
                fallen.containsAll(supported(it))
            }.filter {
                fallen.add(it)
            }.forEach {
                q.offer(it)
            }
        }
        return fallen
    }

    fun fall(b: Brick): Int = falling(b).size - 1
    fun fallPotential() = settled.sumOf { fall(it) }

    companion object {
        fun of(bricks: List<Brick>) = BrickStack(bricks.settle())

        fun List<Brick>.settle(): List<Brick> {
            val settled = mutableListOf<Brick>()

            sortedBy { it.bottom }.forEach { fallingBrick ->
                val fallenBrick =
                    settled.filter { it.horizontalOverlap(fallingBrick) }.maxByOrNull { it.top }
                        ?.let {
                            fallingBrick.elevateTo(it.top + 1)
                        } ?: fallingBrick.elevateTo(1)

                settled.add(fallenBrick)
            }

            return settled
        }
    }
}
