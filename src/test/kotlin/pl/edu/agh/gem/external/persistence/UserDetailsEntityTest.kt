package pl.edu.agh.gem.external.persistence

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import pl.edu.agh.gem.util.createBasicUserDetailsEntity

class UserDetailsEntityTest : ShouldSpec({

    should("map UserDetailsEntity to UserDetails") {
        // given
        val userDetailsEntity = createBasicUserDetailsEntity()

        // when
        val userDetails = userDetailsEntity.toDomain()

        // then
        userDetails.also {
            it.id shouldBe userDetailsEntity.id
            it.username shouldBe userDetailsEntity.username
            it.firstName shouldBe userDetailsEntity.firstName
            it.lastName shouldBe userDetailsEntity.lastName
            it.phoneNumber shouldBe userDetailsEntity.phoneNumber
            it.bankAccountNumber shouldBe userDetailsEntity.bankAccountNumber
            it.preferredPaymentMethod shouldBe userDetailsEntity.preferredPaymentMethod
            it.attachmentId shouldBe userDetailsEntity.attachmentId
        }
    }
})
