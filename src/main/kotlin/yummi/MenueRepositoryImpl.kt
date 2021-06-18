package yummi

import org.springframework.stereotype.Component

@Component
class MenueRepositoryImpl : MenueRepository {
    override fun getMenue(): Menue {
        return Menue()
    }
}
