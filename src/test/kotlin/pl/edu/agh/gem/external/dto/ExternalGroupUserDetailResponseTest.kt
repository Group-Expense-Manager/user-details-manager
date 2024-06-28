package pl.edu.agh.gem.external.dto

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
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
        val ids = listOf("id1", "id2", "id3")
        val usernames = listOf("name1", "name2", "name3")
        val firstNames = listOf("firstName1", "firstName2", "firstName3")
        val lastNames = listOf("lastName1", "lastName2", "lastName3")
        val attachmentIds = listOf("attachmentId1", "attachmentId2", "attachmentId3")

        val groupUserDetails = ids.mapIndexed { index, id ->
            createUserDetails(
                id = id,
                username = usernames[index],
                firstName = firstNames[index],
                lastName = lastNames[index],
                attachmentId = attachmentIds[index],
            )
        }

        // when
        val externalGroupUserDetailsResponse = groupUserDetails.toExternalGroupUserDetailsResponse()

        // then
        externalGroupUserDetailsResponse.details.also {
            it.size shouldBe 3
            it.map { dto -> dto.id } shouldContainExactly ids
            it.map { dto -> dto.username } shouldContainExactly usernames
            it.map { dto -> dto.firstName } shouldContainExactly firstNames
            it.map { dto -> dto.lastName } shouldContainExactly lastNames
            it.map { dto -> dto.attachmentId } shouldContainExactly attachmentIds
        }
    }
},)
