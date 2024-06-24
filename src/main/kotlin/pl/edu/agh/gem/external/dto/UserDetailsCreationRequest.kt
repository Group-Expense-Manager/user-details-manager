package pl.edu.agh.gem.external.dto

import pl.edu.agh.gem.internal.model.PaymentMethod.NONE
import pl.edu.agh.gem.internal.model.UserDetails

data class UserDetailsCreationRequest(
    val userId: String,
    val username: String,
) {
    fun toDomain(attachmentId: String) =
        UserDetails(
            id = userId,
            username = username,
            firstName = null,
            lastName = null,
            phoneNumber = null,
            bankAccountNumber = null,
            attachmentId = attachmentId,
            preferredPaymentMethod = NONE,
        )
}
