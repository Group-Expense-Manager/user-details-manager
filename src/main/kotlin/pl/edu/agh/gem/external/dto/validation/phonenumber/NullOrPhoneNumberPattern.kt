package pl.edu.agh.gem.external.dto.validation.phonenumber

import jakarta.validation.Constraint
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [NullOrPhoneNumberPatternValidator::class])
annotation class NullOrPhoneNumberPattern(
    val message: String = "{javax.validation.constraints.NullOrPhoneNumberPattern.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = [],
)
