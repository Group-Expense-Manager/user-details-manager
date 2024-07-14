package pl.edu.agh.gem.external.dto

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import pl.edu.agh.gem.util.DummyData.USER_ID
import pl.edu.agh.gem.util.createUserDetailsUpdateRequest

class UserDetailsUpdateRequestTest : ShouldSpec({
    should("map UserDetailsUpdateRequest to UserDetailsUpdate correctly") {
        // given
        val userDetailsUpdateRequest = createUserDetailsUpdateRequest()

        // when
        val userDetailsUpdate = userDetailsUpdateRequest.toDomain(USER_ID)

        // then
        userDetailsUpdate.also {
            it.userId shouldBe USER_ID
            it.username shouldBe userDetailsUpdateRequest.username
            it.firstName shouldBe userDetailsUpdateRequest.firstName
            it.lastName shouldBe userDetailsUpdateRequest.lastName
            it.phoneNumber shouldBe userDetailsUpdateRequest.phoneNumber
            it.bankAccountNumber shouldBe userDetailsUpdateRequest.bankAccountNumber
            it.preferredPaymentMethod shouldBe userDetailsUpdateRequest.preferredPaymentMethod
        }
    }
},)
