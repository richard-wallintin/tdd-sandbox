package y2023.day19

data class WorkflowSystem(val workflows: Map<String, Workflow>) {
    fun process(part: Part): String {
        var result = "in"
        while (result !in setOf("A", "R")) {
            val w = workflows[result] ?: throw IllegalStateException("unknown workflow $result")
            result = w.process(part)
        }
        return result
    }

    fun processAll(parts: Sequence<Part>) =
        parts.filter { process(it) == "A" }.sumOf { it.overall }

    companion object {
        fun of(workflows: Sequence<Workflow>) =
            WorkflowSystem(workflows.associateBy { it.name })
    }

}
