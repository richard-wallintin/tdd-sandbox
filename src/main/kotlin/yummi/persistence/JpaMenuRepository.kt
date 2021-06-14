package yummi.persistence

import org.springframework.stereotype.Component
import yummi.Menu
import yummi.MenuRepository

@Component
class JpaMenuRepository(private val offerRepository: PersistentOfferRepository) : MenuRepository {
    override fun loadMenu(): Menu {
        return Menu(offerRepository.findAll().map(PersistentOffer::convert))
    }
}
