package yummi

import com.fasterxml.jackson.databind.JsonNode
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class YummiApplicationIT(
    @Autowired val rest: TestRestTemplate
) {
    @Test
    internal fun `kann das Menue abrufen`() {
        val response = rest.getForEntity<JsonNode>("/eis")
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
    }
}
