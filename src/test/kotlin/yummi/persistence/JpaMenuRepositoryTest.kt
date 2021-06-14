package yummi.persistence

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import yummi.Amount
import yummi.Menu
import yummi.Offer
import yummi.Size

class JpaMenuRepositoryTest {

    @Test
    internal fun `loads menu from using spring data JPA`() {
        val offerRepository = mockk<PersistentOfferRepository>()
        val repo = JpaMenuRepository(offerRepository)

        every { offerRepository.findAll() } returns listOf(
            PersistentOffer("Strawberry", Size.Small, 2.0)
        )

        assertThat(
            repo.loadMenu(), equalTo(
                Menu(
                    Offer("Strawberry", Size.Small, Amount(2.0))
                )
            )
        )
    }
}
