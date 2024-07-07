package pl.edu.agh.gem.external.dto

import pl.edu.agh.gem.internal.model.PaymentMethod
import pl.edu.agh.gem.internal.model.UserDetails

data class UserDetailsResponse(
    val id: String,
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?,
    val bankAccountNumber: String?,
    val preferredPaymentMethod: PaymentMethod,
    val attachmentId: String,
)

fun UserDetails.toUserDetailsResponse() = UserDetailsResponse(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumber,
    bankAccountNumber = bankAccountNumber,
    preferredPaymentMethod = preferredPaymentMethod,
    attachmentId = attachmentId,
)
