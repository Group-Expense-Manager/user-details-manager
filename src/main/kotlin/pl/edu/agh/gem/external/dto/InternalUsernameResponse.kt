package pl.edu.agh.gem.external.dto

import pl.edu.agh.gem.internal.model.UserDetails

data class InternalUsernameResponse(
    val username: String,
)

fun UserDetails.toInternalUsernameResponse() =
    InternalUsernameResponse(
        username = this.username,
    )
