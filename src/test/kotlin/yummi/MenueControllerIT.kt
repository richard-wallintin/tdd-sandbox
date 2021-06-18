package yummi

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MenueControllerIT(
    @Autowired val rest: TestRestTemplate
) {
    @MockkBean
    private lateinit var repository: MenueRepository

    @Test
    internal fun `kann per GET das Menue abrufen`() {
        every {
            repository.getMenue()
        } returns Menue(
            Eintrag(Sorte("Vanille"), Betrag(1.2))
        )


        val response = rest.getForEntity<JsonNode>("/eis")
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        assertThat(response.body.nodeType, equalTo(JsonNodeType.OBJECT))
        assertThat(response.body["eintraege"].nodeType, equalTo(JsonNodeType.ARRAY))
    }
}
