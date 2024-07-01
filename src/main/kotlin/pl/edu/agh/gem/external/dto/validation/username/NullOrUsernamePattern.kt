package pl.edu.agh.gem.external.dto.validation.username

import jakarta.validation.Constraint
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [NullOrUsernamePatternValidator::class])
annotation class NullOrUsernamePattern(
    val message: String = "{javax.validation.constraints.NullOrUsernamePattern.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = [],
)
