package yummi

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class MenuControllerTest {

    @Test
    internal fun `provides icecream menu`() {
        val repository = mockk<MenuRepository>()
        val controller = MenuController(repository)

        every { repository.loadMenu() } returns Menu(
            Offer("Vanilla", Size.Small, Amount(2.5)),
            Offer("Chocolate", Size.Medium, Amount(4.0))
        )

        val menu = controller.getMenu()

        assertThat(
            menu, equalTo(
                Menu(
                    Offer("Vanilla", Size.Small, Amount(2.5)),
                    Offer("Chocolate", Size.Medium, Amount(4.0))
                )
            )
        )
    }
}
