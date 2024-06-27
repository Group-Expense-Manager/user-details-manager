package pl.edu.agh.gem.external.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import pl.edu.agh.gem.internal.model.PaymentMethod
import pl.edu.agh.gem.internal.model.UserDetails

@Document("user-details")
data class UserDetailsEntity(
    @Id
    val id: String,
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?,
    val bankAccountNumber: String?,
    val preferredPaymentMethod: PaymentMethod,
    val attachmentId: String,
) {
    fun toDomain() =
        UserDetails(
            id = id,
            username = username,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            bankAccountNumber = bankAccountNumber,
            preferredPaymentMethod = preferredPaymentMethod,
            attachmentId = attachmentId,
        )
}
