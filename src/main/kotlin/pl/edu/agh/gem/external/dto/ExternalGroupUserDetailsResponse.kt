package pl.edu.agh.gem.external.dto

import pl.edu.agh.gem.internal.model.UserDetails

data class ExternalGroupUserDetailsResponse(
    val details: List<ExternalUserDetailsDto>,
)

data class ExternalUserDetailsDto(
    val id: String,
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val attachmentId: String,
)

fun List<UserDetails>.toExternalGroupUserDetailsResponse() = ExternalGroupUserDetailsResponse(
    details = map { it.toExternalUserDetailsDto() },
)

fun UserDetails.toExternalUserDetailsDto() = ExternalUserDetailsDto(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
    attachmentId = attachmentId,
)
