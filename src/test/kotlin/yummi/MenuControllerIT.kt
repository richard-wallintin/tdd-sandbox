package yummi

import com.fasterxml.jackson.databind.JsonNode
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MenuControllerIT(@Autowired val restTemplate: TestRestTemplate) {

    @MockkBean
    private lateinit var repository: MenuRepository

    @Test
    internal fun `can request menu per GET`() {
        every { repository.loadMenu() } returns Menu(
            Offer("Vanilla", Size.Small, Amount(2.5)),
            Offer("Chocolate", Size.Medium, Amount(4.0))
        )

        val response = restTemplate.getForEntity<JsonNode>("/menu")
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        val json = response.body!!
        assertThat(json["offers"][0]["flavor"].textValue(), equalTo("Vanilla"))
    }
}
