package pl.edu.agh.gem.external.dto.validation.phonenumber

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.regex.Pattern

class NullOrPhoneNumberPatternValidator : ConstraintValidator<NullOrPhoneNumberPattern?, String?> {
    private val pattern: Pattern = Pattern.compile("^\\d{9,12}$")
    override fun isValid(value: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        return value == null || pattern.matcher(value).matches()
    }
}
