package pl.edu.agh.gem.external.dto

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import pl.edu.agh.gem.util.DummyData.USER_ID
import pl.edu.agh.gem.util.createUserDetails

class UserDetailsUpdateResponseTest : ShouldSpec({
    should("map UserDetails to UserDetailsUpdateResponse correctly") {
        // given
        val userDetails = createUserDetails(USER_ID)

        // when
        val userDetailsUpdateResponse = userDetails.toUserDetailsUpdateResponse()

        // then
        userDetailsUpdateResponse.also {
            it.userId shouldBe USER_ID
        }
    }
},)
