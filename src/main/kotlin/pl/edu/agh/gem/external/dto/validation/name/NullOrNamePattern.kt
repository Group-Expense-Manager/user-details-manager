package pl.edu.agh.gem.external.dto.validation.name

import jakarta.validation.Constraint
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [NullOrNamePatternValidator::class])
annotation class NullOrNamePattern(
    val message: String = "{javax.validation.constraints.NullOrUsernamePattern.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = [],
)
