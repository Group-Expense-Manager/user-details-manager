package pl.edu.agh.gem.external.dto

import pl.edu.agh.gem.internal.model.UserDetails

data class UserDetailsUpdateResponse(
    val userId: String,
)

fun UserDetails.toUserDetailsUpdateResponse() = UserDetailsUpdateResponse(userId = id)
