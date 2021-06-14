package yummi.persistence

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import yummi.Size

@DataJpaTest
internal open class PersistentOfferRepositoryIT(
    @Autowired val offerRepository: PersistentOfferRepository
) {

    @Test
    internal fun `store and load offers`() {
        offerRepository.save(PersistentOffer("Brownie", Size.Large, 25.0))

        val offers = offerRepository.findAll()
        assertThat(offers.size, equalTo(1))
    }
}
