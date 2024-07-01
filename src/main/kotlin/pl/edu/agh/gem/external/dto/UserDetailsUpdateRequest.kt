package pl.edu.agh.gem.external.dto

import pl.edu.agh.gem.annotation.nullorblank.NullOrNotBlank
import pl.edu.agh.gem.external.dto.validation.bankaccountnumber.NullOrBankAccountNumberPattern
import pl.edu.agh.gem.external.dto.validation.name.NullOrNamePattern
import pl.edu.agh.gem.external.dto.validation.phonenumber.NullOrPhoneNumberPattern
import pl.edu.agh.gem.external.dto.validation.username.NullOrUsernamePattern
import pl.edu.agh.gem.internal.model.PaymentMethod
import pl.edu.agh.gem.internal.model.UserDetailsUpdate
import pl.edu.agh.gem.validation.ValidationMessage.ATTACHMENT_ID_NOT_BLANK
import pl.edu.agh.gem.validation.ValidationMessage.BANK_ACCOUNT_NUMBER_PATTERN_MESSAGE
import pl.edu.agh.gem.validation.ValidationMessage.NAME_PATTERN_MESSAGE
import pl.edu.agh.gem.validation.ValidationMessage.PHONE_NUMBER_PATTERN_MESSAGE
import pl.edu.agh.gem.validation.ValidationMessage.USERNAME_PATTERN_MESSAGE

data class UserDetailsUpdateRequest(
    @field:NullOrUsernamePattern(USERNAME_PATTERN_MESSAGE)
    val username: String?,
    @field:NullOrNamePattern(NAME_PATTERN_MESSAGE)
    val firstName: String?,
    @field:NullOrNamePattern(NAME_PATTERN_MESSAGE)
    val lastName: String?,
    @field:NullOrPhoneNumberPattern(PHONE_NUMBER_PATTERN_MESSAGE)
    val phoneNumber: String?,
    @field:NullOrBankAccountNumberPattern(BANK_ACCOUNT_NUMBER_PATTERN_MESSAGE)
    val bankAccountNumber: String?,
    val preferredPaymentMethod: PaymentMethod?,
    @field:NullOrNotBlank(ATTACHMENT_ID_NOT_BLANK)
    val attachmentId: String?,
) {
    fun toDomain(userId: String) = UserDetailsUpdate(
        userId = userId,
        username = username,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        bankAccountNumber = bankAccountNumber,
        preferredPaymentMethod = preferredPaymentMethod,
        attachmentId = attachmentId,
    )
}
