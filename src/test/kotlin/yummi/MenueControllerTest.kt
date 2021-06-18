package yummi

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class MenueControllerTest {

    @Test
    internal fun `kann Menue liefern`() {
        val repository = mockk<MenueRepository>()
        val controller = MenueController(repository)

        every {
            repository.getMenue()
        } returns Menue(
            Eintrag(Sorte("Vanille"), Betrag(1.2))
        )

        val menue = controller.getMenue()

        assertThat(
            menue, equalTo(
                Menue(
                    Eintrag(Sorte("Vanille"), Betrag(1.2))
                )
            )
        )
    }
}
