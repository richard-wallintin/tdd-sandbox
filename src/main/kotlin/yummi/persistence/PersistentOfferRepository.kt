package yummi.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface PersistentOfferRepository : JpaRepository<PersistentOffer, Long>
