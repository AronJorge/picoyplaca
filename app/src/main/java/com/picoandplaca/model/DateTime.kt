package com.picoandplaca.model

import android.annotation.SuppressLint
import android.content.Context
import com.picoandplaca.R
import java.text.SimpleDateFormat

class DateTime(var date: String?, var time: String?) {

    fun dateLengthVerify(): Boolean {
        return date!!.length in 8..10
    }

    fun timeLengthVerify(): Boolean {
        return time!!.length in 4..5
    }

    fun verifyRestrictedTime(): Boolean {
        val timeSplit = time!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val hour = Integer.parseInt(timeSplit[0])//try
        val minutes = Integer.parseInt(timeSplit[1])
        return if (hour in 7..9 || hour in 16..19) {
            if (hour == 9 || hour == 19) {
                minutes in 1..30
            } else {
                true
            }
        } else {
            false
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun dayOfDate(context: Context): String {
        val format = SimpleDateFormat("dd/MM/yyyy")
        var dayOfDate = ""
        try {
            val dateTransform = format.parse(date!!)
            when (dateTransform!!.day) {
                1 -> dayOfDate = context.getString(R.string.monday)
                2 -> dayOfDate = context.getString(R.string.tuesday)
                3 -> dayOfDate = context.getString(R.string.wednesday)
                4 -> dayOfDate = context.getString(R.string.thursday)
                5 -> dayOfDate = context.getString(R.string.friday)
                6 -> dayOfDate = context.getString(R.string.saturday)
                0 -> dayOfDate = context.getString(R.string.sunday)
            }
        } catch (e: Exception) {
            println(e.toString())
        }

        return dayOfDate
    }

}

