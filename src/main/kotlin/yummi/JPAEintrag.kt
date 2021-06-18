package yummi

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="EINTRAG")
class JPAEintrag(
    @Id var sorte: String = "",
    var preis: Double = 0.0)
