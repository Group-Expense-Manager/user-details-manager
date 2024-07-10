package pl.edu.agh.gem.internal.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import pl.edu.agh.gem.helper.group.createGroupMembers
import pl.edu.agh.gem.internal.client.GroupManagerClient
import pl.edu.agh.gem.internal.model.PaymentMethod.NONE
import pl.edu.agh.gem.internal.persistance.UserDetailsRepository
import pl.edu.agh.gem.util.DummyData.ANOTHER_USER_ID
import pl.edu.agh.gem.util.DummyData.GROUP_ID
import pl.edu.agh.gem.util.DummyData.USER_ID
import pl.edu.agh.gem.util.createBasicUserDetails
import pl.edu.agh.gem.util.createUserDetails
import pl.edu.agh.gem.util.createUserDetailsUpdate

class UserDetailsServiceTest : ShouldSpec({
    val userDetailsRepository = mock<UserDetailsRepository>()
    val groupManagerClient = mock<GroupManagerClient>()
    val userDetailsService = UserDetailsService(userDetailsRepository, groupManagerClient)

    should("create user details successfully") {
        // given
        val userDetails = createBasicUserDetails()
        whenever(userDetailsRepository.save(userDetails)).thenReturn(userDetails)

        // when
        userDetailsService.create(userDetails)

        // then
        verify(userDetailsRepository, times(1)).save(userDetails)
    }

    should("get internal group user details successfully") {
        // given
        val groupMembers = createGroupMembers(USER_ID, ANOTHER_USER_ID)
        val userDetails = createUserDetails(USER_ID)
        val anotherUserDetails = createUserDetails(ANOTHER_USER_ID)
        whenever(groupManagerClient.getMembers(GROUP_ID)).thenReturn(groupMembers)
        whenever(userDetailsRepository.findById(USER_ID)).thenReturn(userDetails)
        whenever(userDetailsRepository.findById(ANOTHER_USER_ID)).thenReturn(anotherUserDetails)

        // when
        val result = userDetailsService.getGroupUserDetails(GROUP_ID)

        // then
        result.all {
            it in listOf(userDetails, anotherUserDetails)
        }
        verify(userDetailsRepository, times(1)).findById(USER_ID)
        verify(userDetailsRepository, times(1)).findById(ANOTHER_USER_ID)
        verify(groupManagerClient, times(1)).getMembers(GROUP_ID)
    }

    should("throw MissingUserDetailsException when UserDetails with given id doesn't exist") {
        // given
        val groupMembers = createGroupMembers(USER_ID, ANOTHER_USER_ID)
        val userDetails = createUserDetails(USER_ID)
        whenever(groupManagerClient.getMembers(GROUP_ID)).thenReturn(groupMembers)
        whenever(userDetailsRepository.findById(USER_ID)).thenReturn(userDetails)
        whenever(userDetailsRepository.findById(ANOTHER_USER_ID)).thenReturn(null)

        // when & then
        shouldThrow<MissingUserDetailsException> {
            userDetailsService.getGroupUserDetails(GROUP_ID)
        }

        verify(userDetailsRepository, times(1)).findById(USER_ID)
        verify(userDetailsRepository, times(1)).findById(ANOTHER_USER_ID)
        verify(groupManagerClient, times(1)).getMembers(GROUP_ID)
    }

    should("get user details") {
        // given
        val userDetails = createUserDetails(USER_ID)
        whenever(userDetailsRepository.findById(USER_ID)).thenReturn(userDetails)

        // when
        val result = userDetailsService.getUserDetails(USER_ID)

        // then
        result shouldBe userDetails
        verify(userDetailsRepository, times(1)).findById(USER_ID)
    }

    should("throw MissingUserDetailsException when UserDetails with given id doesn't exist") {
        // given
        whenever(userDetailsRepository.findById(USER_ID)).thenReturn(null)

        // when & then
        shouldThrow<MissingUserDetailsException> {
            userDetailsService.getUserDetails(USER_ID)
        }
        verify(userDetailsRepository, times(1)).findById(USER_ID)
    }

    should("update UserDetails successfully") {
        // given
        val oldUserName = "name1"
        val newUserName = "name2"
        val oldAttachmentId = "123"
        val newFirstName = "John"
        val newLastName = "Doe"

        val userDetails = createBasicUserDetails(USER_ID, oldUserName, oldAttachmentId)
        val userDetailsUpdate = createUserDetailsUpdate(
            USER_ID,
            newUserName,
            newFirstName,
            newLastName,
            phoneNumber = null,
            bankAccountNumber = null,
            preferredPaymentMethod = NONE,
        )
        val expectedUserDetails = createUserDetails(
            id = USER_ID,
            username = newUserName,
            firstName = newFirstName,
            lastName = newLastName,
            phoneNumber = null,
            bankAccountNumber = null,
            preferredPaymentMethod = NONE,
            attachmentId = oldAttachmentId,

        )
        whenever(userDetailsRepository.findById(USER_ID)).thenReturn(userDetails)

        // when
        userDetailsService.updateUserDetails(userDetailsUpdate)

        // then
        verify(userDetailsRepository, times(1)).findById(USER_ID)
        verify(userDetailsRepository, times(1)).save(expectedUserDetails)
    }

    should("throw MissingUserDetailsException when UserDetails with given id doesn't exist") {
        // given
        whenever(userDetailsRepository.findById(USER_ID)).thenReturn(null)

        // when & then
        shouldThrow<MissingUserDetailsException> {
            userDetailsService.updateUserDetails(createUserDetailsUpdate(USER_ID))
        }
        verify(userDetailsRepository, times(1)).findById(USER_ID)
    }
},)
