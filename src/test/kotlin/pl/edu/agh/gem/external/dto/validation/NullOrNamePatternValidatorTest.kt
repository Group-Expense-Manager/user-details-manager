package pl.edu.agh.gem.external.dto.validation

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import jakarta.validation.ConstraintValidatorContext
import org.mockito.kotlin.mock
import pl.edu.agh.gem.external.dto.validation.bankaccountnumber.NullOrBankAccountNumberPatternValidator
import pl.edu.agh.gem.external.dto.validation.name.NullOrNamePatternValidator

class NullOrNamePatternValidatorTest : ShouldSpec({
    should("accept null value") {
        // given
        val nullString: String? = null
        val validator = NullOrNamePatternValidator()
        val constraintValidatorContext = mock<ConstraintValidatorContext>()

        // given
        val result = validator.isValid(nullString, constraintValidatorContext)

        // then
        result shouldBe true
    }

    should("accept string with pattern") {
        // given
        val string = "Jojo"
        val validator = NullOrNamePatternValidator()
        val constraintValidatorContext = mock<ConstraintValidatorContext>()

        // given
        val result = validator.isValid(string, constraintValidatorContext)

        // then
        result shouldBe true
    }

    context("reject string cause:") {
        withData(
            nameFn = { it.first },
            Pair("size is to small", "Jo"),
            Pair("size is to big", "Jojojojojojojojojojojojojojojojojojojojoj"),
            Pair("contains not allowed signs", "Jojo$"),
            Pair("doesn't start with capital letter", "jojo"),
        ) { (name, string) ->
            // when
            val validator = NullOrBankAccountNumberPatternValidator()
            val constraintValidatorContext = mock<ConstraintValidatorContext>()

            // given
            val result = validator.isValid(string, constraintValidatorContext)

            // then
            result shouldBe false
        }
    }
},)
