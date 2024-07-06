package pl.edu.agh.gem.internal.model

data class UserDetails(
    val id: String,
    val username: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val bankAccountNumber: String? = null,
    val preferredPaymentMethod: PaymentMethod = PaymentMethod.NONE,
    val attachmentId: String,
)
