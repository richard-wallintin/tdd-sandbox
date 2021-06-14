package yummi

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MenuController {
    @GetMapping("menu")
    fun getMenu(): Menu {
        return Menu(
            Offer("Vanilla", Size.Small, Amount(2.5)),
            Offer("Chocolate", Size.Medium, Amount(4.0))
        )
    }
}
