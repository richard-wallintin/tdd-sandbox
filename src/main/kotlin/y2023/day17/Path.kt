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
    val direction: CardinalDirection = CardinalDirection.E,
    val from: Path? = null,
    val loss: Int = 0,
    val straight: Int = 0
) {
    constructor(t: Trajectory) : this(
        to = t.from,
        direction = t.direction
    )

    val totalLoss: Int by lazy {
        (from?.totalLoss ?: 0) + loss
    }

    fun heuristicLoss(dest: Point, costEstimate: Int) =
        totalLoss + distanceTo(dest) * costEstimate

    fun distanceTo(dest: Point) = to distance dest

    fun walk(r: RelativeDirection, lossFunction: LossFunction): Path? {
        val dir = direction.turn(r)
        val next = to.go(dir)

        if (beenThere(next)) return null

        return lossFunction(next)?.let { loss ->
            copy(
                to = next,
                direction = dir,
                loss = loss,
                from = this,
                straight = if (r == RelativeDirection.AHEAD) straight + 1 else 1
            )
        }
    }

    private fun beenThere(point: Point): Boolean {
        return if (to == point) true
        else from?.beenThere(point) ?: false
    }

    fun next(lossFunction: LossFunction) = sequence {
        if (straight < 3) yield(RelativeDirection.AHEAD)
        yield(RelativeDirection.LEFT)
        yield(RelativeDirection.RIGHT)
    }.mapNotNull { walk(it, lossFunction) }

    private val summary: String by lazy { (from?.summary ?: "$to") + direction }

    override fun toString(): String {
        return "$summary -> $to [$totalLoss]"
    }

    val trajectory by lazy { Trajectory(to, direction, straight) }

    fun traverseBackwards(): Sequence<Path> =
        sequenceOf(this) + (from?.traverseBackwards() ?: emptySequence())
}
