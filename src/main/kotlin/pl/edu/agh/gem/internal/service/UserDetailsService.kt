package pl.edu.agh.gem.internal.service

import org.springframework.stereotype.Service
import pl.edu.agh.gem.internal.client.GroupManagerClient
import pl.edu.agh.gem.internal.model.UserDetails
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
}

class MissingUserDetailsException(userId: String) :
    RuntimeException("Failed to find userDetails for id: $userId")
