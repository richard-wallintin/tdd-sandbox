package yummi.persistence

import jakarta.persistence.*
import yummi.Amount
import yummi.Offer
import yummi.Size

@Entity
@SequenceGenerator(
    name = "persistent_offer_generator",
    sequenceName = "PERSISTENT_OFFER_SEQ",
    allocationSize = 1
)
class PersistentOffer(
    var flavor: String,
    var size: Size,
    var price_in_euros: Double,
    @Id @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "persistent_offer_generator"
    )
    var id: Long? = null
) {
    fun convert() =
        Offer(flavor, size, Amount(price_in_euros))
}
