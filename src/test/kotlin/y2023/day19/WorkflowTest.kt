package y2023.day19

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class WorkflowTest {

    private val referenceData = """
        px{a<2006:qkq,m>2090:A,rfg}
        pv{a>1716:R,A}
        lnx{m>1548:A,A}
        rfg{s<537:gd,x>2440:R,A}
        qs{s>3448:A,lnx}
        qkq{x<1416:A,crn}
        crn{x>2662:A,R}
        in{s<1351:px,qqz}
        qqz{s>2770:qs,m<1801:hdj,R}
        gd{a>3333:R,R}
        hdj{m>838:A,pv}

        {x=787,m=2655,a=1222,s=2876}
        {x=1679,m=44,a=2067,s=496}
        {x=2036,m=264,a=79,s=2244}
        {x=2461,m=1339,a=466,s=291}
        {x=2127,m=1623,a=2188,s=1013}
    """.trimIndent()

    private val sampleWorkflowEx = Workflow.of("ex{x>10:one,m<20:two,a>30:R,A}")

    private val input = AOC.getInput("/2023/day19.txt")

    @Test
    fun `parse a workflow`() {
        val w = sampleWorkflowEx
        w.name shouldBe "ex"
        w.rules.size shouldBe 4
        w.rules[0].result shouldBe "one"
        w.rules[3].result shouldBe "A"
    }

    @Test
    fun `send part through workflow`() {
        sampleWorkflowEx.process(Part(x = 787, m = 2655, a = 1222, s = 2876)) shouldBe "one"
        sampleWorkflowEx.process(Part(x = 3, m = 2, a = 1222, s = 2876)) shouldBe "two"
        sampleWorkflowEx.process(Part(x = 3, m = 20, a = 1222, s = 2876)) shouldBe "R"
        sampleWorkflowEx.process(Part(x = 3, m = 20, a = 30, s = 2876)) shouldBe "A"
    }

    @Test
    fun `workflow system`() {
        val system = workflowSystem(referenceData)
        system.process(Part(x = 787, m = 2655, a = 1222, s = 2876)) shouldBe "A"
        system.process(Part(x = 1679, m = 44, a = 2067, s = 496)) shouldBe "R"
    }

    @Test
    fun `overall part value`() {
        Part(x = 787, m = 2655, a = 1222, s = 2876).overall shouldBe 7540
    }


    @Test
    fun `parse parts`() {
        Part.of("{x=787,m=2655,a=1222,s=2876}") shouldBe Part(x = 787, m = 2655, a = 1222, s = 2876)

        readParts(referenceData).toList().size shouldBe 5
    }

    @Test
    fun `overall sum`() {
        workflowSystem(referenceData).processAll(readParts(referenceData)) shouldBe 19114
    }

    @Test
    fun `part 1`() {
        workflowSystem(input).processAll(readParts(input)) shouldBe 323625
    }

    private fun workflowSystem(text: String) = WorkflowSystem.of(readWorkflows(text))

    private fun readWorkflows(text: String) =
        text.lineSequence().takeWhile { it.isNotBlank() }.map {
            Workflow.of(it)
        }

    private fun readParts(text: String) =
        text.lineSequence().dropWhile { it.isNotBlank() }.drop(1).map { Part.of(it) }
}