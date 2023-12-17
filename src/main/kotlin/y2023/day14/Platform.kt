package y2023.day14

import util.Direction
import y2023.day14.Lane.Companion.transpose

data class Platform(val westLanes: List<Lane>) {
    private fun north(): List<Lane> {
        return westLanes.transpose()
    }

    fun load(d: Direction): Int {
        return north().sumOf { it.load }
    }

    fun tilt(d: Direction): Platform {
        return of(north().map { it.tilt() })
    }

    companion object {
        fun of(text: String) = Platform(text.lines().map(::Lane))
        fun of(north: List<Lane>) = Platform(
            north.transpose()
        )
    }
}
