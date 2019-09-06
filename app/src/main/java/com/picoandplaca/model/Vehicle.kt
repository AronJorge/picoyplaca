package com.picoandplaca.model

import android.content.Context
import com.picoandplaca.R


class Vehicle(var licensePlateNumber: String?) {

    fun licensePlateNumberLengthVerify(): Boolean {
        return licensePlateNumber!!.length in 7..8
    }

    fun verifyRestrictedDayByLicensePlate(context: Context): String {
        val lastDigit = Integer.parseInt(licensePlateNumber!!.substring(licensePlateNumber!!.length - 1))

        var day = ""
        if (lastDigit == 1 || lastDigit == 2) {
            day = context.getString(R.string.monday)
        } else if (lastDigit == 3 || lastDigit == 4) {
            day = context.getString(R.string.tuesday)
        } else if (lastDigit == 5 || lastDigit == 6) {
            day = context.getString(R.string.wednesday)
        } else if (lastDigit == 7 || lastDigit == 8) {
            day = context.getString(R.string.thursday)
        } else if (lastDigit == 9 || lastDigit == 0) {
            day = context.getString(R.string.friday)
        }
        return day
    }
}
