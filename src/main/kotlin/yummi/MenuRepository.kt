package yummi

import org.springframework.stereotype.Component

interface MenuRepository {
    fun loadMenu(): Menu
}

@Component
internal class PlaceholderMenuRepository : MenuRepository {
    override fun loadMenu(): Menu {
        return Menu()
    }
}
