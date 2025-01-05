package pl.edu.agh.gem.external.dto

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import pl.edu.agh.gem.util.createGroupsUserDetails
import pl.edu.agh.gem.util.createUserDetails

class ExternalGroupUserDetailResponseTest : ShouldSpec({
    should("map UserDetails to ExternalUserDetailsDto correctly") {
        // given
        val userDetails = createUserDetails()

        // when
        val externalUserDetailDto = userDetails.toExternalUserDetailsDto()

        // then
        externalUserDetailDto.also {
            it.id shouldBe userDetails.id
            it.username shouldBe userDetails.username
            it.firstName shouldBe userDetails.firstName
            it.lastName shouldBe userDetails.lastName
            it.attachmentId shouldBe userDetails.attachmentId
        }
    }

    should("map List of UserDetails to ExternalGroupUserDetailsResponse correctly") {
        // given
        val groupUserDetails = createGroupsUserDetails()

        // when
        val externalGroupUserDetailsResponse = groupUserDetails.toExternalGroupUserDetailsResponse()

        // then
        externalGroupUserDetailsResponse.details.also {
            it.size shouldBe 3
            it.map { dto -> dto.id } shouldContainExactly groupUserDetails.map { userDetails -> userDetails.id }
            it.map { dto -> dto.username } shouldContainExactly groupUserDetails.map { userDetails -> userDetails.username }
            it.map { dto -> dto.firstName } shouldContainExactly groupUserDetails.map { userDetails -> userDetails.firstName }
            it.map { dto -> dto.lastName } shouldContainExactly groupUserDetails.map { userDetails -> userDetails.lastName }
            it.map { dto -> dto.attachmentId } shouldContainExactly groupUserDetails.map { userDetails -> userDetails.attachmentId }
        }
    }
})
