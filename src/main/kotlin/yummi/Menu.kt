package yummi

data class Menu(val offers: List<Offer>) {
    constructor(vararg offer: Offer) : this(listOf(*offer))
}
