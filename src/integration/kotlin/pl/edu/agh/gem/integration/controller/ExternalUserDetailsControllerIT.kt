package pl.edu.agh.gem.integration.controller

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import pl.edu.agh.gem.assertion.shouldBody
import pl.edu.agh.gem.assertion.shouldHaveErrors
import pl.edu.agh.gem.assertion.shouldHaveHttpStatus
import pl.edu.agh.gem.exception.UserWithoutGroupAccessException
import pl.edu.agh.gem.external.dto.ExternalGroupUserDetailsResponse
import pl.edu.agh.gem.helper.group.createGroupMembersResponse
import pl.edu.agh.gem.helper.user.createGemUser
import pl.edu.agh.gem.integration.BaseIntegrationSpec
import pl.edu.agh.gem.integration.ability.ServiceTestClient
import pl.edu.agh.gem.integration.ability.stubMembersUrl
import pl.edu.agh.gem.integration.ability.stubUserGroupsUrl
import pl.edu.agh.gem.internal.persistance.UserDetailsRepository
import pl.edu.agh.gem.internal.service.MissingUserDetailsException
import pl.edu.agh.gem.util.DummyData.ANOTHER_GROUP_ID
import pl.edu.agh.gem.util.DummyData.ANOTHER_USER_ID
import pl.edu.agh.gem.util.DummyData.GROUP_ID
import pl.edu.agh.gem.util.DummyData.USER_ID
import pl.edu.agh.gem.util.createGroupsUserDetails
import pl.edu.agh.gem.util.createUserGroupsResponse

class ExternalUserDetailsControllerIT(
    private val service: ServiceTestClient,
    private val userDetailsRepository: UserDetailsRepository,
) : BaseIntegrationSpec(
    {

        should("get group user details") {
            // given
            val groupUserDetails = createGroupsUserDetails()
            stubUserGroupsUrl(createUserGroupsResponse(GROUP_ID, ANOTHER_GROUP_ID), USER_ID)
            stubMembersUrl(createGroupMembersResponse(*groupUserDetails.map { it.id }.toTypedArray()), GROUP_ID)
            groupUserDetails.forEach { userDetail -> userDetailsRepository.save(userDetail) }

            // when
            val response = service.getExternalGroupUserDetails(createGemUser(USER_ID), GROUP_ID)

            // then
            response shouldHaveHttpStatus OK

            response.shouldBody<ExternalGroupUserDetailsResponse> {
                details.size shouldBe 3
                details.map { dto -> dto.id } shouldContainExactly groupUserDetails.map { it.id }
                details.map { dto -> dto.username } shouldContainExactly groupUserDetails.map { it.username }
                details.map { dto -> dto.firstName } shouldContainExactly groupUserDetails.map { it.firstName }
                details.map { dto -> dto.lastName } shouldContainExactly groupUserDetails.map { it.lastName }
                details.map { dto -> dto.attachmentId } shouldContainExactly groupUserDetails.map { it.attachmentId }
            }
        }

        should("return NOT_FOUND when user detail doesn't exist") {
            // given
            stubUserGroupsUrl(createUserGroupsResponse(GROUP_ID, ANOTHER_GROUP_ID), USER_ID)
            stubMembersUrl(createGroupMembersResponse(USER_ID, ANOTHER_USER_ID), GROUP_ID)

            // when
            val response = service.getExternalGroupUserDetails(createGemUser(USER_ID), GROUP_ID)

            // then
            response shouldHaveHttpStatus NOT_FOUND
            response shouldHaveErrors {
                errors shouldHaveSize 1
                errors.first().code shouldBe MissingUserDetailsException::class.simpleName
            }
        }

        should("return FORBIDDEN when user is not a group member") {
            // given
            stubUserGroupsUrl(createUserGroupsResponse(ANOTHER_GROUP_ID), USER_ID)

            // when
            val response = service.getExternalGroupUserDetails(createGemUser(USER_ID), GROUP_ID)

            // then
            response shouldHaveHttpStatus FORBIDDEN
            response shouldHaveErrors {
                errors shouldHaveSize 1
                errors.first().code shouldBe UserWithoutGroupAccessException::class.simpleName
            }
        }
    },
)