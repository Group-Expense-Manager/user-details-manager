package pl.edu.agh.gem.external.dto

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import pl.edu.agh.gem.util.createUserDetails

class InternalUsernameResponseTest : ShouldSpec({
    should("map userDetails to InternalUsernameResponse correctly") {
        // given
        val userDetails = createUserDetails()

        // when
        val internalUsernameResponse = userDetails.toInternalUsernameResponse()

        // then
        internalUsernameResponse.username shouldBe userDetails.username
    }
},)
