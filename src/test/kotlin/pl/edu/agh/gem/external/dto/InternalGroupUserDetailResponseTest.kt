package pl.edu.agh.gem.external.dto

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import pl.edu.agh.gem.util.createUserDetails

class InternalGroupUserDetailResponseTest : ShouldSpec({
    should("map UserDetails to InternalUserDetailsDto correctly") {
        // given
        val userDetails = createUserDetails()

        // when
        val internalUserDetailDto = userDetails.toInternalUserDetailsDto()

        // then
        internalUserDetailDto.also {
            it.id shouldBe userDetails.id
            it.username shouldBe userDetails.username
            it.firstName shouldBe userDetails.firstName
            it.lastName shouldBe userDetails.lastName
        }
    }

    should("map List of UserDetails to InternalGroupUserDetailsResponse correctly") {
        // given
        val ids = listOf("id1", "id2", "id3")
        val usernames = listOf("name1", "name2", "name3")
        val firstNames = listOf("firstName1", "firstName2", "firstName3")
        val lastNames = listOf("lastName1", "lastName2", "lastName3")

        val groupUserDetails = ids.mapIndexed { index, id ->
            createUserDetails(
                id = id,
                username = usernames[index],
                firstName = firstNames[index],
                lastName = lastNames[index],
            )
        }

        // when
        val internalGroupUserDetailsResponse = groupUserDetails.toInternalGroupUserDetailsResponse()

        // then
        internalGroupUserDetailsResponse.details.also {
            it.size shouldBe 3
            it.map { dto -> dto.id } shouldContainExactly ids
            it.map { dto -> dto.username } shouldContainExactly usernames
            it.map { dto -> dto.firstName } shouldContainExactly firstNames
            it.map { dto -> dto.lastName } shouldContainExactly lastNames
        }
    }
},)
