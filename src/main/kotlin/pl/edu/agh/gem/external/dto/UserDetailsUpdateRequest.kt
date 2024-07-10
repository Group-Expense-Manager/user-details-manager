package pl.edu.agh.gem.external.dto

import jakarta.validation.constraints.Pattern
import pl.edu.agh.gem.annotation.nullorpattern.NullOrPattern
import pl.edu.agh.gem.internal.model.PaymentMethod
import pl.edu.agh.gem.internal.model.UserDetailsUpdate
import pl.edu.agh.gem.validation.ValidationMessage.BANK_ACCOUNT_NUMBER_PATTERN_MESSAGE
import pl.edu.agh.gem.validation.ValidationMessage.NAME_PATTERN_MESSAGE
import pl.edu.agh.gem.validation.ValidationMessage.PHONE_NUMBER_PATTERN_MESSAGE
import pl.edu.agh.gem.validation.ValidationMessage.USERNAME_PATTERN_MESSAGE

data class UserDetailsUpdateRequest(
    @field:Pattern(message = USERNAME_PATTERN_MESSAGE, regexp = "^[a-zA-Z0-9_.+-]{3,20}$")
    val username: String,
    @field:NullOrPattern(message = NAME_PATTERN_MESSAGE, pattern = "^[A-Z][a-zA-Z' -]{1,19}$")
    val firstName: String? = null,
    @field:NullOrPattern(message = NAME_PATTERN_MESSAGE, pattern = "^[A-Z][a-zA-Z' -]{1,19}$")
    val lastName: String? = null,
    @field:NullOrPattern(message = PHONE_NUMBER_PATTERN_MESSAGE, pattern = "^\\d{9,12}$")
    val phoneNumber: String? = null,
    @field:NullOrPattern(message = BANK_ACCOUNT_NUMBER_PATTERN_MESSAGE, pattern = "^\\d{15,34}$")
    val bankAccountNumber: String? = null,
    val preferredPaymentMethod: PaymentMethod,
) {
    fun toDomain(userId: String) = UserDetailsUpdate(
        userId = userId,
        username = username,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        bankAccountNumber = bankAccountNumber,
        preferredPaymentMethod = preferredPaymentMethod,
    )
}
