package y2023.day19

import AOC
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import y2023.day19.Selection.Companion.all
import y2023.day19.Selection.Companion.greaterThan
import y2023.day19.Selection.Companion.lessThan
import y2023.day19.Selection.Companion.selectSome
import y2023.day19.Selection.Companion.vs

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

    @Test
    fun `part spec and selectors`() {
        val spec = PartSpec()
        spec.x shouldBe DEFAULT_RANGE
        spec.m shouldBe DEFAULT_RANGE
        spec.a shouldBe DEFAULT_RANGE
        spec.s shouldBe DEFAULT_RANGE
        spec.size shouldBe 4000L*4000L*4000L*4000L

        Attribute.X.restrict(greaterThan(100))(spec) shouldBe selectSome(
            PartSpec(x = 101..4000),
            PartSpec(x = 1..100)
        )

        Attribute.M.restrict(greaterThan(2))(spec) shouldBe selectSome(
            PartSpec(m = 3..4000),
            PartSpec(m = 1..2),
        )

        Attribute.A.restrict(greaterThan(4000))(spec) shouldBe selectSome(
            PartSpec(a = IntRange.EMPTY),
            PartSpec(a = 1..4000),
        )

        Attribute.S.restrict(greaterThan(0))(spec) shouldBe Selection(
            spec
        )

        Attribute.M.restrict(lessThan(6))(PartSpec(m = 1..10)) shouldBe
                (PartSpec(m = 1..5) vs PartSpec(m = 6..10))

        Attribute.X.restrict(all())(PartSpec(x = 3..5)) shouldBe Selection(
            PartSpec(x = 3..5)
        )
    }

    @Test
    fun `a rule can select & test`() {
        val rule = Rule.condition(Attribute.X, greaterThan(100), "A")
        rule.select(PartSpec()) shouldBe (
                PartSpec(x = 101..4000) vs
                        PartSpec(x = 1..100)
                )
        rule.test(Part(x = 1, m = 5, a = 5, s = 5)) shouldBe false
    }

    @Test
    fun `workflow produces spec pairs`() {
        sampleWorkflowEx.process(PartSpec()).toList() shouldBe listOf(
            PartSpec(x = 11..4000) to "one",
            PartSpec(x = 1..10, m = 1..19) to "two",
            PartSpec(x = 1..10, m = 20..4000, a = 31..4000) to "R",
            PartSpec(x = 1..10, m = 20..4000, a = 1..30) to "A",
        )
    }

    @Test
    fun `system produces accepted specs`() {
        val system = WorkflowSystem.of(sequenceOf(Workflow.of("in{x>10:A,a>30:R,A}")))
        system.acceptSpecs().toList() shouldBe listOf(
            PartSpec(x = 11..4000),
            PartSpec(x = 1..10, a = 1..30)
        )

        workflowSystem(referenceData).acceptSpecs().sumOf { it.size } shouldBe 167409079868000
    }

    @Test
    fun `part 2`() {
        workflowSystem(input).acceptSpecs().sumOf { it.size } shouldBe 127447746739409L
    }

    private fun workflowSystem(text: String) = WorkflowSystem.of(readWorkflows(text))

    private fun readWorkflows(text: String) =
        text.lineSequence().takeWhile { it.isNotBlank() }.map {
            Workflow.of(it)
        }

    private fun readParts(text: String) =
        text.lineSequence().dropWhile { it.isNotBlank() }.drop(1).map { Part.of(it) }
}