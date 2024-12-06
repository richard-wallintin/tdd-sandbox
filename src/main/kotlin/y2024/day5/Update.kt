package y2024.day5

import util.integers

data class Update(val pages: List<Int>) {
    val middlePage = pages[pages.size / 2]

    companion object {
        fun parse(text: String): List<Update> {
            return text.lineSequence().dropWhile { it.isNotBlank() }.drop(1).map {
                Update(it.split(',').integers())
            }.toList()
        }

        fun List<Update>.middlePageSum() = sumOf { it.middlePage }
    }
}