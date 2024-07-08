package pl.edu.agh.gem.external.controller

import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pl.edu.agh.gem.exception.UserWithoutGroupAccessException
import pl.edu.agh.gem.external.dto.ExternalGroupUserDetailsResponse
import pl.edu.agh.gem.external.dto.UserDetailsResponse
import pl.edu.agh.gem.external.dto.toExternalGroupUserDetailsResponse
import pl.edu.agh.gem.external.dto.toUserDetailsResponse
import pl.edu.agh.gem.internal.client.GroupManagerClient
import pl.edu.agh.gem.internal.service.UserDetailsService
import pl.edu.agh.gem.media.InternalApiMediaType.APPLICATION_JSON_INTERNAL_VER_1
import pl.edu.agh.gem.paths.Paths.EXTERNAL
import pl.edu.agh.gem.security.GemUserId

@RestController
@RequestMapping("$EXTERNAL/user-details")
class ExternalUserDetailsController(
    private val userDetailsService: UserDetailsService,
    private val groupManagerClient: GroupManagerClient,
) {

    @GetMapping("groups/{groupId}", produces = [APPLICATION_JSON_INTERNAL_VER_1])
    @ResponseStatus(OK)
    fun getGroupUserDetails(
        @GemUserId userId: String,
        @PathVariable groupId: String,
    ): ExternalGroupUserDetailsResponse {
        userId.checkIfUserHaveAccess(groupId)
        return userDetailsService.getGroupUserDetails(groupId).toExternalGroupUserDetailsResponse()
    }

    @GetMapping(produces = [APPLICATION_JSON_INTERNAL_VER_1])
    @ResponseStatus(OK)
    fun getUserDetails(
        @GemUserId userId: String,
    ): UserDetailsResponse {
        return userDetailsService.getUserDetails(userId).toUserDetailsResponse()
    }

    @GetMapping("groups/{groupId}/members/{groupMemberId}", produces = [APPLICATION_JSON_INTERNAL_VER_1])
    @ResponseStatus(OK)
    fun getGroupMemberDetails(
        @GemUserId userId: String,
        @PathVariable groupId: String,
        @PathVariable groupMemberId: String,

    ): UserDetailsResponse {
        val groupMembers = groupManagerClient.getMembers(groupId).members
        groupMembers.find { it.id == userId } ?: throw UserWithoutGroupAccessException(userId)
        groupMembers.find { it.id == groupMemberId } ?: throw UserNotGroupMemberException(groupMemberId, groupId)

        return userDetailsService.getUserDetails(groupMemberId).toUserDetailsResponse()
    }

    private fun String.checkIfUserHaveAccess(groupId: String) {
        groupManagerClient.getGroups(this).find { it.groupId == groupId } ?: throw UserWithoutGroupAccessException(this)
    }
}

class UserNotGroupMemberException(userId: String, groupId: String) :
    RuntimeException("User with id: $userId is not a member of group with id: $groupId")
