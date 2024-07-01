package pl.edu.agh.gem.external.dto.validation.username

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.regex.Pattern

class NullOrUsernamePatternValidator : ConstraintValidator<NullOrUsernamePattern?, String?> {
    private val pattern: Pattern = Pattern.compile("^[a-zA-Z0-9_-]{3,20}$")
    override fun isValid(value: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        return value == null || pattern.matcher(value).matches()
    }
}
