package yummi

data class Menue(val eintraege: List<Eintrag>) {
    constructor(vararg eintraege: Eintrag): this(listOf(*eintraege))
}
