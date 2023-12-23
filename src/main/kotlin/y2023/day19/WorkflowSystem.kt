package y2023.day19

private const val START_WORKFLOW = "in"

private const val ACCEPT = "A"
private const val REJECT = "R"

data class WorkflowSystem(val workflows: Map<String, Workflow>) {
    fun process(part: Part): String {
        var result = START_WORKFLOW
        while (result !in setOf(ACCEPT, REJECT)) {
            val w = get(result)
            result = w.process(part)
        }
        return result
    }

    operator fun get(result: String) =
        workflows[result] ?: throw IllegalStateException("unknown workflow $result")

    fun processAll(parts: Sequence<Part>) =
        parts.filter { process(it) == ACCEPT }.sumOf { it.overall }

    fun acceptSpecs(
        spec: PartSpec = PartSpec(),
        workflow: String = START_WORKFLOW
    ): Sequence<PartSpec> =
        get(workflow).process(spec).flatMap { (spec, result) ->
            when (result) {
                ACCEPT -> sequenceOf(spec)
                REJECT -> emptySequence()
                else -> acceptSpecs(spec, result)
            }
        }

    companion object {
        fun of(workflows: Sequence<Workflow>) =
            WorkflowSystem(workflows.associateBy { it.name })
    }

}
