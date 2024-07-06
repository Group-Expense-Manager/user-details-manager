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
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val bankAccountNumber: String? = null,
    val preferredPaymentMethod: PaymentMethod = PaymentMethod.NONE,
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
