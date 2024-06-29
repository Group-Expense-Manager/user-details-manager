package pl.edu.agh.gem.external.controller

import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pl.edu.agh.gem.external.dto.InternalGroupUserDetailsResponse
import pl.edu.agh.gem.external.dto.UserDetailsCreationRequest
import pl.edu.agh.gem.external.dto.toInternalGroupUserDetailsResponse
import pl.edu.agh.gem.internal.client.AttachmentStoreClient
import pl.edu.agh.gem.internal.service.UserDetailsService
import pl.edu.agh.gem.media.InternalApiMediaType.APPLICATION_JSON_INTERNAL_VER_1
import pl.edu.agh.gem.paths.Paths.INTERNAL

@RestController
@RequestMapping("$INTERNAL/user-details")
class InternalUserDetailsController(
    private val userDetailsService: UserDetailsService,
    private val attachmentStoreClient: AttachmentStoreClient,
) {
    @PostMapping(consumes = [APPLICATION_JSON_INTERNAL_VER_1])
    @ResponseStatus(CREATED)
    fun createUserDetails(
        @RequestBody userDetailsCreationRequest: UserDetailsCreationRequest,
    ) {
        val attachmentId = attachmentStoreClient.createDefaultUserAttachment(userDetailsCreationRequest.userId)
        userDetailsService.create(userDetailsCreationRequest.toDomain(attachmentId))
    }

    @GetMapping("/groups/{groupId}", produces = [APPLICATION_JSON_INTERNAL_VER_1])
    @ResponseStatus(OK)
    fun getGroupUserDetails(
        @PathVariable groupId: String,
    ): InternalGroupUserDetailsResponse {
        return userDetailsService.getGroupUserDetails(groupId).toInternalGroupUserDetailsResponse()
    }
}
