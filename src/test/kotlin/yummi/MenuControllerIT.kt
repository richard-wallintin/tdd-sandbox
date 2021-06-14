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
class MenuControllerIT(@Autowired val restTemplate: TestRestTemplate) {
    @Test
    internal fun `can request menu per GET`() {
        val response = restTemplate.getForEntity<JsonNode>("/menu")
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        val json = response.body!!
        assertThat(json["offers"][0]["flavor"].textValue(), equalTo("Vanilla"))
    }
}
