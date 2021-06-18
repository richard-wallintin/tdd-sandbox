package yummi

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MenueTest {
    @Test
    internal fun `kann mehrere Eintraege haben`() {
        val menue = Menue(
            Eintrag(Sorte("Vanille"), Betrag(2.5)),
            Eintrag(Sorte("Schoko"), Betrag(2.5))
        )
        assertThat(menue.eintraege.size, equalTo(2))
    }
}
