package com.picoandplaca.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.picoandplaca.R
import com.picoandplaca.data.DataPreference.listHistory
import com.picoandplaca.model.DataHistory
import com.picoandplaca.model.DateTime
import com.picoandplaca.model.Vehicle
import com.picoandplaca.utils.ConsFun.dateTimeNow
import com.picoandplaca.utils.ConsFun.formatDate
import com.picoandplaca.utils.ConsFun.listToStringData
import com.picoandplaca.utils.ConsFun.plusDay
import com.picoandplaca.utils.ConsFun.stringToListData
import com.jakewharton.threetenabp.AndroidThreeTen
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.inflateLayout
import com.pawegio.kandroid.show
import com.pawegio.kandroid.startActivity
import kotterknife.bindView
import java.util.*




class MainActivity : AppCompatActivity() {

    private var list : MutableList<DataHistory> = mutableListOf()

    //Variables to get the date
    private val c = Calendar.getInstance()
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)
    private val year = c.get(Calendar.YEAR)
    private val hour = c.get(Calendar.HOUR_OF_DAY)
    private val minute = c.get(Calendar.MINUTE)

    private var dateString = ""
    private var timeString = ""
    private var dayNow = 0
    private var monthNow = 0
    private var yearNow = 0

    private val txtResult : TextView by bindView(R.id.text_result)
    private val editPlate : EditText by bindView(R.id.edit_plate)
    private val editDate : EditText by bindView(R.id.edit_date)
    private val editHour : EditText by bindView(R.id.edit_hour)
    private val btnDate : ImageButton by bindView(R.id.btn_date)
    private val btnHour : ImageButton by bindView(R.id.btn_hour)
    private val btnSee : Button by bindView(R.id.btn_see)
    private val btnSeeHistory : Button by bindView(R.id.btn_history)
    private val checkDisc : CheckBox by bindView(R.id.check_disc)
    private val checkTerE : CheckBox by bindView(R.id.check_third_age)
    private val linearNext : LinearLayout by bindView(R.id.linear_next)
    private val txtPlate : TextView by bindView(R.id.text_placa)
    private val linearNextAdapter : LinearLayout by bindView(R.id.linear_next_adapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidThreeTen.init(this)
        onClick()
        showBtnHistory()
    }

    private fun onClick(){
        btnDate.setOnClickListener { datePicker() }
        btnHour.setOnClickListener { timePicker() }
        btnSee.setOnClickListener { predict() }
        btnSeeHistory.setOnClickListener { startActivity<HistoryActivity>() }
    }

    private fun showBtnHistory(){
        list = stringToListData(listHistory)
        if (list.size>0) btnSeeHistory.show()
    }

    //dialog date
    private fun datePicker() = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            monthNow = month + 1
            dayNow = day
            yearNow = year
            val dayFormat = if (dayNow < 10) "0$dayNow" else "$dayNow"
            val monthFormat = if (monthNow < 10) "0$monthNow" else "$monthNow"
            dateString = "$dayFormat/$monthFormat/$year"
            editDate.setText(dateString)
        },year,month,day).show()


    //dialog hour
    private fun timePicker() = TimePickerDialog(this,TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            val hourFormat = if (hour < 10) "0$hour" else "$hour"
            val minuteFormat = if (minute < 10) "0$minute" else "$minute"
            timeString = "$hourFormat:$minuteFormat"
            editHour.setText(timeString)
        },hour,minute,true).show()

    //calculate contravention
    private fun predict(){
        if (editPlate.text.isNotEmpty() && editDate.text.isNotEmpty() && editHour.text.isNotEmpty()){
            val vehicle = Vehicle(editPlate.text.toString())
            val dateTime = DateTime(dateString,timeString)

            txtResult.text = seeContravention(dateTime,vehicle)
            showBtnHistory()

            if (dateTime.dayOfDate(this)==vehicle.verifyRestrictedDayByLicensePlate(this) && !checkDisc.isChecked && !checkTerE.isChecked)
                nextContravention(vehicle)
            else linearNext.hide()
        }else txtResult.text = getString(R.string.enter_all_data)
    }

    //func next contraventions
    @SuppressLint("SetTextI18n")
    private fun nextContravention(vehicle: Vehicle){
        linearNext.show()
        txtPlate.text = "${getString(R.string.vehicle)} ${editPlate.text}"
        linearNextAdapter.removeAllViews()
        var day = 0
        for (i in 0 until 60){
            day++
            val plusDay = plusDay(yearNow,monthNow,dayNow,day)
            val dateTime = DateTime(plusDay,timeString)
            if (dateTime.dayOfDate(this) == vehicle.verifyRestrictedDayByLicensePlate(this)){
                val v = inflateLayout(R.layout.item_next_contravenciones,null,false)
                val text = v.findViewById<TextView>(R.id.text_next_adapter)
                text.text = "${dateTime.dayOfDate(this)}, ${formatDate(dateTime.date!!)}"
                linearNextAdapter.addView(v)
            }
        }
    }

    //func verify contravention
    private fun Context.seeContravention(dateTime:DateTime, vehicle: Vehicle) : String{
        if (vehicle.licensePlateNumberLengthVerify() && dateTime.dateLengthVerify() && dateTime.timeLengthVerify()){
            if (!checkDisc.isChecked)
                if (!checkTerE.isChecked)
                    if((dateTime.dayOfDate(this)!=getString(R.string.saturday))&&(dateTime.dayOfDate(this)!=getString(R.string.sunday))){
                        return if(dateTime.dayOfDate(this)==vehicle.verifyRestrictedDayByLicensePlate(this)){
                            if(dateTime.verifyRestrictedTime()) {
                                list.add(DataHistory(dateTimeNow(),dateTime.date!!,dateTime.time!!,vehicle.licensePlateNumber!!,true,false,false))
                                listHistory = listToStringData(list)
                                "${getString(R.string.your_vehicle)} ${vehicle.licensePlateNumber} ${getString(R.string.have_contravention)} ${dateTime.dayOfDate(this)}, ${dateTime.date} ${getString(R.string.at)} ${dateTime.time}"
                            } else {
                                list.add(DataHistory(dateTimeNow(),dateTime.date!!,dateTime.time!!,vehicle.licensePlateNumber!!,false,false,false))
                                listHistory = listToStringData(list)
                                "${getString(R.string.your_vehicle)} ${vehicle.licensePlateNumber} ${getString(R.string.no_have_contravention)} ${dateTime.dayOfDate(this)}, ${dateTime.date} ${getString(R.string.at)} ${dateTime.time}"
                            }
                        }else {
                            list.add(DataHistory(dateTimeNow(),dateTime.date!!,dateTime.time!!,vehicle.licensePlateNumber!!,false,false,false))
                            listHistory = listToStringData(list)
                            "${getString(R.string.your_vehicle)} ${vehicle.licensePlateNumber} ${getString(R.string.no_have_contravention)} ${dateTime.dayOfDate(this)}, ${dateTime.date} ${getString(R.string.at)} ${dateTime.time}"
                        }
                    }else {
                        list.add(DataHistory(dateTimeNow(),dateTime.date!!,dateTime.time!!,vehicle.licensePlateNumber!!,false,false,false))
                        listHistory = listToStringData(list)
                        return getString(R.string.no_have_contravention_weekend)
                    }
                else {
                    list.add(DataHistory(dateTimeNow(),dateTime.date!!,dateTime.time!!,vehicle.licensePlateNumber!!,false,false,true))
                    listHistory = listToStringData(list)
                    return getString(R.string.no_have_contravention_senior_citizens)
                }
            else {
                list.add(DataHistory(dateTimeNow(),dateTime.date!!,dateTime.time!!,vehicle.licensePlateNumber!!,false,true,false))
                listHistory = listToStringData(list)
                return getString(R.string.no_have_contravention_disability)
            }
        }else return getString(R.string.incorrect_data)
    }
}
