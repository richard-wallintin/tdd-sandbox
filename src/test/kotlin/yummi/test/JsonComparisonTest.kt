package yummi.test

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test

class JsonComparisonTest {

    val a = JSON.objectNode("foo" to JSON.textNode("bar"))
    val b = JSON.objectNode("foo" to JSON.textNode("bar"))
    val c = JSON.objectNode("foo" to JSON.textNode("bam"))

    @Test
    internal fun `can test equality of json objects`() {
        a shouldBeEqualTo a
        a shouldBeEqualTo b
        a shouldNotBeEqualTo c
    }

    @Test
    internal fun `error message contains toString representation`() {
        val exc = shouldThrow<Throwable> {
            a shouldBeEqualTo c
        }
        exc.message shouldContain a.toString()
        exc.message shouldContain c.toString()
    }
}