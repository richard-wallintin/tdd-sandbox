package yummi

class MenuController {
    fun getMenu(): Menu {
        return Menu(
            Offer("Vanilla", Size.Small, Amount(2.5)),
            Offer("Chocolate", Size.Medium, Amount(4.0))
        )
    }
}
