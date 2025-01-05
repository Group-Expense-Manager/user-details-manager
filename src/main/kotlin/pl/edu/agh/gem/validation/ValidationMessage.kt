package pl.edu.agh.gem.validation

object ValidationMessage {
    const val USERNAME_PATTERN_MESSAGE = "Username must be 3 to 20 characters long and contain only letters, digits, underscores, or hyphens"
    const val NAME_PATTERN_MESSAGE =
        "Name must start with a capital letter and be 2 to 20 characters long, " +
            "containing only letters, apostrophes, spaces, or hyphens"
    const val PHONE_NUMBER_PATTERN_MESSAGE = "Phone number must contain only digits and be 9 to 12 characters long"
    const val BANK_ACCOUNT_NUMBER_PATTERN_MESSAGE =
        "Bank account number must consist of up to 34 alphanumeric characters, " +
            "starting with a two-letter country code then two check digits"
}
