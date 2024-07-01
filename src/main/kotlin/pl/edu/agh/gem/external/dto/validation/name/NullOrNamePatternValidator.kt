package pl.edu.agh.gem.external.dto.validation.name

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.regex.Pattern

class NullOrNamePatternValidator : ConstraintValidator<NullOrNamePattern?, String?> {
    private val pattern: Pattern = Pattern.compile("^[A-Z][a-zA-Z' -]{1,19}$")
    override fun isValid(value: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        return value == null || pattern.matcher(value).matches()
    }
}
