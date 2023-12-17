package y2023.day7

enum class Label(val c: Char) {
    N2('2'), N3('3'), N4('4'), N5('5'), N6('6'), N7('7'), N8('8'), N9('9'),
    T('T'), J('J'), Q('Q'), K('K'), A('A');

    companion object {
        fun of(c: Char) = entries.first { it.c == c }
    }
}
