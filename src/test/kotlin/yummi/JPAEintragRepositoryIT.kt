package yummi

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
open class JPAEintragRepositoryIT(
    @Autowired val repository: JPAEintragRepository
) {

    @Test
    internal fun `speichern und laden von Eintraegen aus DB`() {
        repository.save(
            JPAEintrag("Cookies", 3.5)
        )

        val all = repository.findAll()

        assertThat(all.size, equalTo(1))
        assertThat(all[0].sorte, equalTo("Cookies"))
    }
}
