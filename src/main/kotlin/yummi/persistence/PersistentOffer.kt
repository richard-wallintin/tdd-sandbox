package yummi.persistence

import yummi.Amount
import yummi.Offer
import yummi.Size
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class PersistentOffer(
    var flavor: String,
    var size: Size,
    var price_in_euros: Double,
    @Id @GeneratedValue var id: Long? = null
) {
    fun convert() =
        Offer(flavor, size, Amount(price_in_euros))
}
