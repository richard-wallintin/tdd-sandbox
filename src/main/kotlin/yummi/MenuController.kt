package yummi

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MenuController(private val repository: MenuRepository) {
    @GetMapping("menu")
    fun getMenu(): Menu {
        return repository.loadMenu()
    }
}
