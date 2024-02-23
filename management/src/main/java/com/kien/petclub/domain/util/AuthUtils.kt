package com.kien.petclub.domain.util

import androidx.core.util.PatternsCompat

object AuthUtils {

    fun isValidEmail(email: String) = PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()

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