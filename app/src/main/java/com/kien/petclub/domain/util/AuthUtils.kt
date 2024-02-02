package com.kien.petclub.domain.util

object AuthUtils {

    fun isValidEmail(email: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    /**
     * Password must be at least 6 characters long and contain at least one uppercase letter
     *
     * @param password
     * @return
     */
    fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Z]).{6,}$"
        return password.matches(passwordPattern.toRegex())
    }
}