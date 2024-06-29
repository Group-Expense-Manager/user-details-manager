package pl.edu.agh.gem.external.dto

import pl.edu.agh.gem.internal.model.UserDetails

data class InternalGroupUserDetailsResponse(
    val details: List<InternalUserDetailsDto>,
)

data class InternalUserDetailsDto(
    val id: String,
    val username: String,
    val firstName: String?,
    val lastName: String?,
)

fun List<UserDetails>.toInternalGroupUserDetailsResponse() = InternalGroupUserDetailsResponse(
    details = map { it.toInternalUserDetailsDto() },
)

fun UserDetails.toInternalUserDetailsDto() = InternalUserDetailsDto(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
)
