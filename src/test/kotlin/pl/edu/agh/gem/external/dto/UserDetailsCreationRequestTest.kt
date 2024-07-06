package pl.edu.agh.gem.external.dto

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import pl.edu.agh.gem.internal.model.PaymentMethod.NONE
import pl.edu.agh.gem.util.DummyData.ATTACHMENT_ID
import pl.edu.agh.gem.util.createUserDetailsCreationRequest

class UserDetailsCreationRequestTest : ShouldSpec({
    should("map UserDetailsRequest to UserDetails correctly") {
        // given
        val userDetailsRequest = createUserDetailsCreationRequest()

        // when
        val userDetails = userDetailsRequest.toDomain(ATTACHMENT_ID)

        // then
        userDetails.also {
            it.id shouldBe userDetailsRequest.userId
            it.username shouldBe userDetailsRequest.username
            it.firstName.shouldBeNull()
            it.lastName.shouldBeNull()
            it.phoneNumber.shouldBeNull()
            it.bankAccountNumber.shouldBeNull()
            it.preferredPaymentMethod shouldBe NONE
            it.attachmentId shouldBe ATTACHMENT_ID
        }
    }
},)
