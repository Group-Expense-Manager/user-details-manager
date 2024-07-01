package pl.edu.agh.gem.external.dto.validation

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import jakarta.validation.ConstraintValidatorContext
import org.mockito.kotlin.mock
import pl.edu.agh.gem.external.dto.validation.bankaccountnumber.NullOrBankAccountNumberPatternValidator

class NullOrBankAccountNumberValidatorTest : ShouldSpec({
    should("accept null value") {
        // given
        val nullString: String? = null
        val validator = NullOrBankAccountNumberPatternValidator()
        val constraintValidatorContext = mock<ConstraintValidatorContext>()

        // given
        val result = validator.isValid(nullString, constraintValidatorContext)

        // then
        result shouldBe true
    }

    should("accept string with pattern") {
        // given
        val string = "123123123123123"
        val validator = NullOrBankAccountNumberPatternValidator()
        val constraintValidatorContext = mock<ConstraintValidatorContext>()

        // given
        val result = validator.isValid(string, constraintValidatorContext)

        // then
        result shouldBe true
    }

    context("reject string cause:") {
        withData(
            nameFn = { it.first },
            Pair("size is to small", "12312312312312"),
            Pair("size is to big", "12312312312312312312312312312312312"),
            Pair("contains not allowed signs", "12312312312312"),
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
