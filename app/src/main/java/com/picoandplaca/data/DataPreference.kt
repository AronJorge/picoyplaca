package com.picoandplaca.data

import android.content.Context
import androidx.core.content.edit

object DataPreference {

    var Context.listHistory : String
        get() = getSharedPreferences("listHistory",Context.MODE_PRIVATE).getString("list","[]")!!
        set(list) = getSharedPreferences("listHistory",Context.MODE_PRIVATE).edit { putString("list",list) }

}