package yummi.test

import io.kotest.matchers.ComparableMatcherResult
import io.kotest.matchers.Matcher
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

/**
 * Custom matcher alternative to [io.kotest.matchers.shouldBe] that has no special treatment of any collections or iterables.
 * It rather simply compares with `==` ([kotlin.Any.equals]) and displays objects using [kotlin.Any.toString]
 */
fun <T> beEqualTo(expected: Any) = object : Matcher<T> {
    override fun test(value: T): ComparableMatcherResult {
        return ComparableMatcherResult(
            value == expected,
            { "$value is not equal to $expected" },
            { "should not be equal to $expected" },
            value.toString(),
            expected.toString()
        )
    }
}

infix fun <T> T.shouldBeEqualTo(expected: Any): T {
    this should beEqualTo(expected)
    return this
}

infix fun <T> T.shouldNotBeEqualTo(expected: Any): T {
    this shouldNot beEqualTo(expected)
    return this
}