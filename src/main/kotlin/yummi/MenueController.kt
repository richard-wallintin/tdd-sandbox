package yummi

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/eis")
class MenueController(val repository: MenueRepository) {
    @GetMapping
    fun getMenue(): Menue {
        return repository.getMenue()
    }

}
