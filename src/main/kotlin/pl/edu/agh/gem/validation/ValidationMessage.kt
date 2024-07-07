package pl.edu.agh.gem.validation

object ValidationMessage {
    const val USERNAME_PATTERN_MESSAGE = "Username must be 3 to 20 characters long and contain only letters, digits, underscores, or hyphens"
    const val NAME_PATTERN_MESSAGE = "Name must start with a capital letter and be 2 to 20 characters long, " +
        "containing only letters, apostrophes, spaces, or hyphens"
    const val PHONE_NUMBER_PATTERN_MESSAGE = "Phone number must contain only digits and be 9 to 12 characters long"
    const val BANK_ACCOUNT_NUMBER_PATTERN_MESSAGE = "Bank account number must contain only digits and be 15 to 34 characters long"
}
