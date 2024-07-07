package pl.edu.agh.gem.external.dto

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import pl.edu.agh.gem.util.createUserDetails

class UserDetailsResponseTest : ShouldSpec({
    should("map UserDetails to UserDetailsResponse correctly") {
        // given
        val userDetails = createUserDetails()

        // when
        val userDetailResponse = userDetails.toUserDetailsResponse()

        // then
        userDetailResponse.also {
            it.id shouldBe userDetails.id
            it.username shouldBe userDetails.username
            it.firstName shouldBe userDetails.firstName
            it.lastName shouldBe userDetails.lastName
            it.phoneNumber shouldBe userDetails.phoneNumber
            it.bankAccountNumber shouldBe userDetails.bankAccountNumber
            it.preferredPaymentMethod shouldBe userDetails.preferredPaymentMethod
            it.attachmentId shouldBe userDetails.attachmentId
        }
    }
},)
