package pl.edu.agh.gem.external.dto.validation.bankaccountnumber

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.regex.Pattern

class NullOrBankAccountNumberPatternValidator : ConstraintValidator<NullOrBankAccountNumberPattern?, String?> {
    private val pattern: Pattern = Pattern.compile("^\\d{15,34}$")
    override fun isValid(value: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        return value == null || pattern.matcher(value).matches()
    }
}
