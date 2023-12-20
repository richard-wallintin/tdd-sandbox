package y2023.day17

import util.CardinalDirection
import util.Point
import util.RelativeDirection

typealias LossFunction = (Point) -> Int?

data class Trajectory(
    val from: Point,
    val direction: CardinalDirection,
    val straight: Int
)

data class Path(
    val to: Point,
    val direction: CardinalDirection,
    val totalLoss: Int = 0,
    val straight: Int = 0,
    val policy: Policy = Policy(0, 3)
) {
    fun heuristicLoss(dest: Point, costEstimate: Int) =
        totalLoss + distanceTo(dest) * costEstimate

    val canStop = straight >= policy.minStraight

    private fun distanceTo(dest: Point) = to distance dest

    fun walk(r: RelativeDirection, lossFunction: LossFunction): Path? {
        val dir = direction.turn(r)
        val next = to.go(dir)

        return lossFunction(next)?.let { loss ->
            copy(
                to = next,
                direction = dir,
                totalLoss = totalLoss + loss,
                straight = if (r == RelativeDirection.AHEAD) straight + 1 else 1
            )
        }
    }

    fun next(lossFunction: LossFunction) = sequence {
        if (straight < policy.maxStraight) {
            yield(RelativeDirection.AHEAD)
        }
        if (straight >= policy.minStraight) {
            yield(RelativeDirection.LEFT)
            yield(RelativeDirection.RIGHT)
        }
    }.mapNotNull { walk(it, lossFunction) }

    val trajectory by lazy { Trajectory(to, direction, straight) }
}
