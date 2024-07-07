package pl.edu.agh.gem.internal.model

data class UserDetailsUpdate(
    val userId: String,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val bankAccountNumber: String? = null,
    val preferredPaymentMethod: PaymentMethod? = null,
)
