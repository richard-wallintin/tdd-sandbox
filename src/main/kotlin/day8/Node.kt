package day8

private fun <T> T?.require(e: () -> Throwable) = this ?: throw e()

data class Node(val id: String, val left: String, val right: String) {
    companion object {
        fun of(spec: String): Node {
            return Regex("(\\w+) = \\((\\w+), (\\w+)\\)").matchEntire(spec)
                .require { IllegalArgumentException("cant parse '$spec' as node") }
                .destructured.let { (id, l, r) ->
                    Node(id, l, r)
                }
        }
    }
}
