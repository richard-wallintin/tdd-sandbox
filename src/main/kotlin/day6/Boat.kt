package day6

import util.longs
import kotlin.math.*

data class Boat(val remainingTime: Long, val speed: Long = 0) {

    val distance = remainingTime * speed

    fun push(pushTime: Long): Boat {
        return copy(speed = pushTime, remainingTime = remainingTime - pushTime)
    }

    fun pushTime(distanceToBeat: Long): LongRange {
        val remainingTime = remainingTime.toDouble()
        val sqrt = sqrt(remainingTime.pow(2) - 4 * distanceToBeat)
        return floor((remainingTime - sqrt).div(2)).roundToLong().plus(1)..
                ceil((remainingTime + sqrt).div(2)).roundToLong().minus(1)
    }

    companion object {
        val LongRange.size: Long get() = (last - first) + 1

        fun margin(text: String, singleRace: Boolean = false) = text.lineSequence().map { line ->
            val segments = line.split(Regex("\\s+")).drop(1)
            if (singleRace) segments.joinToString("").longs() else segments.longs()
        }.take(2).toList().let { (times, distances) ->
            times.zip(distances)
        }.map { Boat(remainingTime = it.first).pushTime(it.second).size }
            .reduce(Long::times)
    }
}
