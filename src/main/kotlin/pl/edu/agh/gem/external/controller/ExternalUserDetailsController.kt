package pl.edu.agh.gem.external.controller

import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pl.edu.agh.gem.exception.UserWithoutGroupAccessException
import pl.edu.agh.gem.external.dto.ExternalGroupUserDetailsResponse
import pl.edu.agh.gem.external.dto.toExternalGroupUserDetailsResponse
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

    @GetMapping("/{groupId}", produces = [APPLICATION_JSON_INTERNAL_VER_1])
    @ResponseStatus(OK)
    fun getGroupUserDetails(
        @GemUserId userId: String,
        @PathVariable groupId: String,
    ): ExternalGroupUserDetailsResponse {
        userId.checkIfUserHaveAccess(groupId)
        return userDetailsService.getGroupUserDetails(groupId).toExternalGroupUserDetailsResponse()
    }

    private fun String.checkIfUserHaveAccess(groupId: String) {
        groupManagerClient.getGroups(this).find { it.groupId == groupId } ?: throw UserWithoutGroupAccessException(groupId)
    }
}