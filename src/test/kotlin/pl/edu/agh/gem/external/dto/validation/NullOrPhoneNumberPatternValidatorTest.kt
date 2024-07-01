package pl.edu.agh.gem.external.dto.validation

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import jakarta.validation.ConstraintValidatorContext
import org.mockito.kotlin.mock
import pl.edu.agh.gem.external.dto.validation.bankaccountnumber.NullOrBankAccountNumberPatternValidator
import pl.edu.agh.gem.external.dto.validation.phonenumber.NullOrPhoneNumberPatternValidator

class NullOrPhoneNumberPatternValidatorTest : ShouldSpec({
    should("accept null value") {
        // given
        val nullString: String? = null
        val validator = NullOrPhoneNumberPatternValidator()
        val constraintValidatorContext = mock<ConstraintValidatorContext>()

        // given
        val result = validator.isValid(nullString, constraintValidatorContext)

        // then
        result shouldBe true
    }

    should("accept string with pattern") {
        // given
        val string = "123123123"
        val validator = NullOrPhoneNumberPatternValidator()
        val constraintValidatorContext = mock<ConstraintValidatorContext>()

        // given
        val result = validator.isValid(string, constraintValidatorContext)

        // then
        result shouldBe true
    }

    should("reject string when size is to small") {
        // given
        val string = "12312312"
        val validator = NullOrPhoneNumberPatternValidator()
        val constraintValidatorContext = mock<ConstraintValidatorContext>()

        // given
        val result = validator.isValid(string, constraintValidatorContext)

        // then
        result shouldBe false
    }
    context("reject string cause:") {
        withData(
            nameFn = { it.first },
            Pair("size is to small", "12312312"),
            Pair("size is to big", "1231231231231"),
            Pair("contains not allowed signs", "123123213f"),
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
