package com.picoandplaca.utils

import android.annotation.SuppressLint
import com.picoandplaca.model.DataHistory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.threeten.bp.LocalDate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object ConsFun {

    //fun next days of contravention
    fun plusDay(year:Int,month:Int,day:Int,plusDay:Int) : String{
        var date = LocalDate.of(year,month,day)
        date = date.plusDays(plusDay.toLong())
        return "${date.dayOfMonth}/${date.monthValue}/${date.year}"
    }

    //fun dateFormat of dd/MM/yyyy to dd MMM yyyy
    fun formatDate(dateString:String) : String{
        @SuppressLint("SimpleDateFormat") val parse = SimpleDateFormat("dd/MM/yyyy")
        @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("dd MMM yyyy")
        return try {
            val date = parse.parse(dateString)!!
            format.format(date)
        } catch (e: ParseException) {
            dateString
        }
    }

    fun dateTimeNow() : String{
        val calendar = Calendar.getInstance()
        return "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)}/${calendar.get(Calendar.YEAR)} " +
                "${calendar.get(Calendar.HOUR)}:${calendar.get(Calendar.MINUTE)}"
    }

    fun listToStringData(list: MutableList<DataHistory>): String =
        Gson().toJson(list, object : TypeToken<MutableList<DataHistory>>(){}.type)

    fun stringToListData(string: String): MutableList<DataHistory> =
        Gson().fromJson(string, object : TypeToken<MutableList<DataHistory>>(){}.type)
}