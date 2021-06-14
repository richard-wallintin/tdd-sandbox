package yummi

import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext

@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.WHEN_AVAILABLE)
internal class YummiApplicationIT(
    @Autowired private val ctx: ApplicationContext
) {

    @Test
    internal fun `application context starts`() {
        ctx shouldNotBe null
    }
}