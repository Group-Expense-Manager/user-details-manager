package pl.edu.agh.gem.internal.model

data class UserDetailsUpdate(
    val userId: String,
    val username: String?,
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?,
    val bankAccountNumber: String?,
    val preferredPaymentMethod: PaymentMethod?,
    val attachmentId: String?,
)
