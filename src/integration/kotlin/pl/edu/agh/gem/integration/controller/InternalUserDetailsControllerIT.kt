package pl.edu.agh.gem.integration.controller

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import pl.edu.agh.gem.assertion.shouldBody
import pl.edu.agh.gem.assertion.shouldHaveErrors
import pl.edu.agh.gem.assertion.shouldHaveHttpStatus
import pl.edu.agh.gem.external.dto.InternalGroupUserDetailsResponse
import pl.edu.agh.gem.helper.group.createGroupMembersResponse
import pl.edu.agh.gem.integration.BaseIntegrationSpec
import pl.edu.agh.gem.integration.ability.ServiceTestClient
import pl.edu.agh.gem.integration.ability.stubDefaultAttachmentUrl
import pl.edu.agh.gem.integration.ability.stubMembersUrl
import pl.edu.agh.gem.internal.persistance.UserDetailsRepository
import pl.edu.agh.gem.internal.service.MissingUserDetailsException
import pl.edu.agh.gem.util.DummyData.ANOTHER_USER_ID
import pl.edu.agh.gem.util.DummyData.GROUP_ID
import pl.edu.agh.gem.util.DummyData.USER_ID
import pl.edu.agh.gem.util.createDefaultAttachmentResponse
import pl.edu.agh.gem.util.createGroupsUserDetails
import pl.edu.agh.gem.util.createUserDetailsCreationRequest

class InternalUserDetailsControllerIT(
    private val service: ServiceTestClient,
    private val userDetailsRepository: UserDetailsRepository,
) : BaseIntegrationSpec(
    {
        should("create user details") {
            // given
            val userDetailsRequest = createUserDetailsCreationRequest()
            stubDefaultAttachmentUrl(createDefaultAttachmentResponse(), USER_ID)

            // when
            val response = service.createUserDetails(userDetailsRequest)

            // then
            response shouldHaveHttpStatus CREATED
        }

        should("get group user details") {
            // given
            val groupUserDetails = createGroupsUserDetails()

            stubMembersUrl(createGroupMembersResponse(*groupUserDetails.map { it.id }.toTypedArray()), GROUP_ID)
            groupUserDetails.forEach { userDetail -> userDetailsRepository.save(userDetail) }

            // when
            val response = service.getGroupUserDetails(GROUP_ID)

            // then
            response shouldHaveHttpStatus OK

            response.shouldBody<InternalGroupUserDetailsResponse> {
                details.size shouldBe 3
                details.map { dto -> dto.id } shouldContainExactly groupUserDetails.map { it.id }
                details.map { dto -> dto.username } shouldContainExactly groupUserDetails.map { it.username }
                details.map { dto -> dto.firstName } shouldContainExactly groupUserDetails.map { it.firstName }
                details.map { dto -> dto.lastName } shouldContainExactly groupUserDetails.map { it.lastName }
            }
        }

        should("return NOT_FOUND when user detail doesnt exist") {
            // given
            stubMembersUrl(createGroupMembersResponse(USER_ID, ANOTHER_USER_ID), GROUP_ID)

            // when
            val response = service.getGroupUserDetails(GROUP_ID)

            // then
            response shouldHaveHttpStatus NOT_FOUND
            response shouldHaveErrors {
                errors shouldHaveSize 1
                errors.first().code shouldBe MissingUserDetailsException::class.simpleName
            }
        }
    },
)
