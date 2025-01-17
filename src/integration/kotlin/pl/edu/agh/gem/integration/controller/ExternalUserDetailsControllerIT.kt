package pl.edu.agh.gem.integration.controller

import io.kotest.datatest.withData
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.OK
import pl.edu.agh.gem.assertion.shouldBody
import pl.edu.agh.gem.assertion.shouldHaveErrors
import pl.edu.agh.gem.assertion.shouldHaveHttpStatus
import pl.edu.agh.gem.assertion.shouldHaveValidationError
import pl.edu.agh.gem.exception.UserWithoutGroupAccessException
import pl.edu.agh.gem.external.controller.UserNotGroupMemberException
import pl.edu.agh.gem.external.dto.ExternalGroupUserDetailsResponse
import pl.edu.agh.gem.external.dto.UserDetailsResponse
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
import pl.edu.agh.gem.util.createEmptyUserDetailsUpdateRequest
import pl.edu.agh.gem.util.createGroupsUserDetails
import pl.edu.agh.gem.util.createUserDetails
import pl.edu.agh.gem.util.createUserDetailsUpdateRequest
import pl.edu.agh.gem.util.createUserGroupsResponse
import pl.edu.agh.gem.validation.ValidationMessage.BANK_ACCOUNT_NUMBER_PATTERN_MESSAGE
import pl.edu.agh.gem.validation.ValidationMessage.NAME_PATTERN_MESSAGE
import pl.edu.agh.gem.validation.ValidationMessage.PHONE_NUMBER_PATTERN_MESSAGE
import pl.edu.agh.gem.validation.ValidationMessage.USERNAME_PATTERN_MESSAGE

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

            should("return INTERNAL_SERVER_ERROR when getting group user details and user details doesn't exist") {
                // given
                stubUserGroupsUrl(createUserGroupsResponse(GROUP_ID, ANOTHER_GROUP_ID), USER_ID)
                stubMembersUrl(createGroupMembersResponse(USER_ID, ANOTHER_USER_ID), GROUP_ID)

                // when
                val response = service.getExternalGroupUserDetails(createGemUser(USER_ID), GROUP_ID)

                // then
                response shouldHaveHttpStatus INTERNAL_SERVER_ERROR
                response shouldHaveErrors {
                    errors shouldHaveSize 1
                    errors.first().code shouldBe MissingUserDetailsException::class.simpleName
                }
            }

            should("return FORBIDDEN when getting group user details and user is not a group member") {
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

            should("get user details") {
                // given
                val user = createGemUser(USER_ID)
                val userDetails = createUserDetails(USER_ID)
                userDetailsRepository.save(userDetails)

                // when
                val response = service.getUserDetails(user)

                // then
                response shouldHaveHttpStatus OK
                response.shouldBody<UserDetailsResponse> {
                    id shouldBe userDetails.id
                    username shouldBe userDetails.username
                    firstName shouldBe userDetails.firstName
                    lastName shouldBe userDetails.lastName
                    phoneNumber shouldBe userDetails.phoneNumber
                    bankAccountNumber shouldBe userDetails.bankAccountNumber
                    preferredPaymentMethod shouldBe userDetails.preferredPaymentMethod
                    attachmentId shouldBe userDetails.attachmentId
                }
            }

            should("return INTERNAL_SERVER_ERROR when getting user details and user details doesn't exist") {
                // given
                val user = createGemUser(USER_ID)

                // when
                val response = service.getUserDetails(user)

                // then
                response shouldHaveHttpStatus INTERNAL_SERVER_ERROR
                response shouldHaveErrors {
                    errors shouldHaveSize 1
                    errors.first().code shouldBe MissingUserDetailsException::class.simpleName
                }
            }

            should("get group member details") {
                // given
                val user = createGemUser(USER_ID)
                val userDetails = createUserDetails(ANOTHER_USER_ID)
                userDetailsRepository.save(userDetails)
                stubUserGroupsUrl(createUserGroupsResponse(GROUP_ID), USER_ID)
                stubMembersUrl(createGroupMembersResponse(USER_ID, ANOTHER_USER_ID), GROUP_ID)

                // when
                val response = service.getGroupMemberDetails(user, GROUP_ID, ANOTHER_USER_ID)

                // then
                response shouldHaveHttpStatus OK
                response.shouldBody<UserDetailsResponse> {
                    id shouldBe userDetails.id
                    username shouldBe userDetails.username
                    firstName shouldBe userDetails.firstName
                    lastName shouldBe userDetails.lastName
                    phoneNumber shouldBe userDetails.phoneNumber
                    bankAccountNumber shouldBe userDetails.bankAccountNumber
                    preferredPaymentMethod shouldBe userDetails.preferredPaymentMethod
                    attachmentId shouldBe userDetails.attachmentId
                }
            }

            should("return FORBIDDEN when getting group member user details and user is not a group member") {
                // given
                val user = createGemUser(USER_ID)
                stubMembersUrl(createGroupMembersResponse(ANOTHER_USER_ID), GROUP_ID)

                // when
                val response = service.getGroupMemberDetails(user, GROUP_ID, ANOTHER_USER_ID)

                // then
                response shouldHaveHttpStatus FORBIDDEN
                response shouldHaveErrors {
                    errors shouldHaveSize 1
                    errors.first().code shouldBe UserWithoutGroupAccessException::class.simpleName
                }
            }

            should("return FORBIDDEN when getting group member user details and other user is not a group member") {
                // given
                val user = createGemUser(USER_ID)
                stubMembersUrl(createGroupMembersResponse(USER_ID), GROUP_ID)

                // when
                val response = service.getGroupMemberDetails(user, GROUP_ID, ANOTHER_USER_ID)

                // then
                response shouldHaveHttpStatus FORBIDDEN
                response shouldHaveErrors {
                    errors shouldHaveSize 1
                    errors.first().code shouldBe UserNotGroupMemberException::class.simpleName
                }
            }

            should("return INTERNAL_SERVER_ERROR when getting group member details and  members' user details doesn't exist") {
                // given
                val user = createGemUser(USER_ID)
                stubMembersUrl(createGroupMembersResponse(USER_ID, ANOTHER_USER_ID), GROUP_ID)

                // when
                val response = service.getGroupMemberDetails(user, GROUP_ID, ANOTHER_USER_ID)

                // then
                response shouldHaveHttpStatus INTERNAL_SERVER_ERROR
                response shouldHaveErrors {
                    errors shouldHaveSize 1
                    errors.first().code shouldBe MissingUserDetailsException::class.simpleName
                }
            }

            should("update user details successfully") {
                // given
                val user = createGemUser(USER_ID)
                val updateRequest = createUserDetailsUpdateRequest()
                val userDetails = createUserDetails(USER_ID)
                userDetailsRepository.save(userDetails)

                // when
                val response = service.updateGroupUserDetails(user, updateRequest)

                // then
                response shouldHaveHttpStatus OK
                response.shouldBody<UserDetailsResponse> {
                    id shouldBe userDetails.id
                    username shouldBe updateRequest.username
                    firstName shouldBe updateRequest.firstName
                    lastName shouldBe updateRequest.lastName
                    phoneNumber shouldBe updateRequest.phoneNumber
                    bankAccountNumber shouldBe updateRequest.bankAccountNumber
                    preferredPaymentMethod shouldBe updateRequest.preferredPaymentMethod
                    attachmentId shouldBe userDetails.attachmentId
                }

                userDetailsRepository.findById(USER_ID).also {
                    it.shouldNotBeNull()
                    it.username shouldBe updateRequest.username
                    it.firstName shouldBe updateRequest.firstName
                    it.lastName shouldBe updateRequest.lastName
                    it.bankAccountNumber shouldBe updateRequest.bankAccountNumber
                    it.preferredPaymentMethod shouldBe updateRequest.preferredPaymentMethod
                    it.phoneNumber shouldBe updateRequest.phoneNumber
                }
            }

            should("return INTERNAL_SERVER_ERROR when updating user details and user details doesn't exist") {
                // given
                val user = createGemUser(USER_ID)
                val updateRequest = createUserDetailsUpdateRequest(USER_ID)

                // when
                val response = service.updateGroupUserDetails(user, updateRequest)

                // then
                response shouldHaveHttpStatus INTERNAL_SERVER_ERROR
                response shouldHaveErrors {
                    errors shouldHaveSize 1
                    errors.first().code shouldBe MissingUserDetailsException::class.simpleName
                }
            }

            context("return validation exception cause:") {
                withData(
                    nameFn = { it.first },
                    Pair(USERNAME_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(username = "ło")),
                    Pair(USERNAME_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(username = "lolololololololololololololololololololol")),
                    Pair(USERNAME_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(username = "name&")),
                    Pair(NAME_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(firstName = "Ł")),
                    Pair(NAME_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(firstName = "Łolololololololololololololololololololol")),
                    Pair(NAME_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(firstName = "fŁs")),
                    Pair(NAME_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(firstName = "Fs0")),
                    Pair(NAME_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(lastName = "Ł")),
                    Pair(NAME_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(lastName = "Lolololololololololololololololololololol")),
                    Pair(NAME_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(lastName = "fss")),
                    Pair(NAME_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(lastName = "Fs0")),
                    Pair(PHONE_NUMBER_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(phoneNumber = "0000111")),
                    Pair(PHONE_NUMBER_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(phoneNumber = "00001111222")),
                    Pair(PHONE_NUMBER_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(phoneNumber = "000011112f")),
                    Pair(PHONE_NUMBER_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(phoneNumber = "+0000111")),
                    Pair(PHONE_NUMBER_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(phoneNumber = "+000011112222")),
                    Pair(BANK_ACCOUNT_NUMBER_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(bankAccountNumber = "00001111222233")),
                    Pair(
                        BANK_ACCOUNT_NUMBER_PATTERN_MESSAGE,
                        createEmptyUserDetailsUpdateRequest(bankAccountNumber = "0000111122223333444455556666777788889"),
                    ),
                    Pair(BANK_ACCOUNT_NUMBER_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(bankAccountNumber = "00pl000011112123123")),
                    Pair(BANK_ACCOUNT_NUMBER_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(bankAccountNumber = "PLPL000011112123123")),
                    Pair(BANK_ACCOUNT_NUMBER_PATTERN_MESSAGE, createEmptyUserDetailsUpdateRequest(bankAccountNumber = "pl000011112123123")),
                ) { (expectedMessage, userDetailsUpdateRequest) ->
                    // when
                    val response = service.updateGroupUserDetails(createGemUser(), userDetailsUpdateRequest)

                    // then
                    response shouldHaveHttpStatus BAD_REQUEST
                    response shouldHaveValidationError expectedMessage
                }
            }
        },
    )
