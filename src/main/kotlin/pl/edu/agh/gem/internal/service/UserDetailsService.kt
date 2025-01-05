package pl.edu.agh.gem.internal.service

import org.springframework.stereotype.Service
import pl.edu.agh.gem.internal.client.GroupManagerClient
import pl.edu.agh.gem.internal.model.UserDetails
import pl.edu.agh.gem.internal.model.UserDetailsUpdate
import pl.edu.agh.gem.internal.persistance.UserDetailsRepository

@Service
class UserDetailsService(
    private val userDetailsRepository: UserDetailsRepository,
    private val groupManagerClient: GroupManagerClient,
) {
    fun create(userDetails: UserDetails) {
        userDetailsRepository.save(userDetails)
    }

    fun getGroupUserDetails(groupId: String): List<UserDetails> {
        val groupMembers = groupManagerClient.getMembers(groupId)
        return groupMembers.members.map { userDetailsRepository.findById(it.id) ?: throw MissingUserDetailsException(it.id) }
    }

    fun getUserDetails(userId: String): UserDetails {
        return userDetailsRepository.findById(userId) ?: throw MissingUserDetailsException(userId)
    }

    fun updateUserDetails(userDetailsUpdate: UserDetailsUpdate): UserDetails {
        val userDetails = userDetailsRepository.findById(userDetailsUpdate.userId) ?: throw MissingUserDetailsException(userDetailsUpdate.userId)

        val updatedUserDetails =
            UserDetails(
                id = userDetails.id,
                username = userDetailsUpdate.username,
                firstName = userDetailsUpdate.firstName,
                lastName = userDetailsUpdate.lastName,
                phoneNumber = userDetailsUpdate.phoneNumber,
                bankAccountNumber = userDetailsUpdate.bankAccountNumber,
                preferredPaymentMethod = userDetailsUpdate.preferredPaymentMethod,
                attachmentId = userDetails.attachmentId,
            )

        return userDetailsRepository.save(updatedUserDetails)
    }
}

class MissingUserDetailsException(userId: String) :
    RuntimeException("Failed to find userDetails for id: $userId")
