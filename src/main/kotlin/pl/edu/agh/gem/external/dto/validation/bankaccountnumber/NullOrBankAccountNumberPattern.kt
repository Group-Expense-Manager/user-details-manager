package pl.edu.agh.gem.external.dto.validation.bankaccountnumber

import jakarta.validation.Constraint
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [NullOrBankAccountNumberPatternValidator::class])
annotation class NullOrBankAccountNumberPattern(
    val message: String = "{javax.validation.constraints.NullOrBankAccountNumberPattern.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = [],
)
