package day7

data class Hand(val labels: List<Label>, val joker: Boolean = false) : Comparable<Hand> {
    init {
        assert(labels.size == 5)
    }

    val type: Type by lazy {
        if (joker) jokerType() else normalType(labels)
    }

    private fun jokerType(): Type {
        val jokers = labels.count { it == Label.J }
        val nonJokers = labels.filter { it != Label.J }

        if (jokers == 5) return Type.FiveOfAKind

        return when (val same = normalType(nonJokers)) {
            Type.HighCard -> when (jokers) {
                1 -> Type.OnePair
                2 -> Type.ThreeOfAKind
                3 -> Type.FourOfAKind
                4 -> Type.FiveOfAKind
                else -> same
            }

            Type.OnePair -> when (jokers) {
                1 -> Type.ThreeOfAKind
                2 -> Type.FourOfAKind
                3 -> Type.FiveOfAKind
                else -> same
            }

            Type.TwoPair -> when(jokers) {
                1 -> Type.FullHouse
                else -> same
            }

            Type.ThreeOfAKind, Type.FullHouse -> when (jokers) {
                1 -> Type.FourOfAKind
                2 -> Type.FiveOfAKind
                else -> same
            }

            Type.FourOfAKind -> when (jokers) {
                1 -> Type.FiveOfAKind
                else -> same
            }

            else -> same
        }
    }

    private fun normalType(labels: List<Label>): Type {
        val distinctLabels = labels.toSet()
        val groups =
            distinctLabels.toList().map { l -> labels.count { it == l } }.sorted().reversed()

        val g1 = groups.first()
        val g2 = groups.getOrNull(1) ?: 0

        return when (g1) {
            5 -> Type.FiveOfAKind
            4 -> Type.FourOfAKind
            3 -> when (g2) {
                2 -> Type.FullHouse
                else -> Type.ThreeOfAKind
            }

            2 -> {
                when (g2) {
                    2 -> Type.TwoPair
                    else -> Type.OnePair
                }
            }

            else -> Type.HighCard
        }
    }

    override fun compareTo(other: Hand): Int {
        return type.compareTo(other.type)
            .let { if (it == 0) labels.compareTo(other.labels) else it }
    }

    private fun List<Label>.compareTo(labels: List<Label>): Int {
        this.zip(labels).forEach { (a, b) ->
            val cmp = compareLabels(a, b)
            if (cmp != 0)
                return cmp
        }
        return 0
    }

    override fun toString(): String {
        return labels.joinToString("") { it.c.toString() } + " ~ " + type
    }

    private fun compareLabels(a: Label, b: Label) =
        if (joker) compareLabelsWithJoker(a, b) else a.compareTo(b)

    companion object {
        fun of(s: String, joker: Boolean = false): Hand {
            return Hand(s.toList().map { Label.of(it) }, joker)
        }

        fun compareLabelsWithJoker(a: Label, b: Label) = when {
            a == b -> 0
            a == Label.J -> -1
            b == Label.J -> +1
            else -> a.compareTo(b)
        }
    }
}
