package com.picoandplaca.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.picoandplaca.R
import com.picoandplaca.model.DataHistory
import com.pawegio.kandroid.inflateLayout
import kotterknife.bindView

class HistoryAdapter(private val list : MutableList<DataHistory>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.data(list[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.context.inflateLayout(R.layout.item_history,parent,false))

    class ViewHolder(v:View) : RecyclerView.ViewHolder(v){

        private val vehicle : TextView by bindView(R.id.txt_vehicle_history)
        private val dateCont : TextView by bindView(R.id.txt_date_history)
        private val dateRegistry : TextView by bindView(R.id.txt_date_registry_history)
        private val cont : TextView by bindView(R.id.txt_contravention_history)

        @SuppressLint("SetTextI18n")
        fun data(data: DataHistory){
            vehicle.text = "${itemView.context.getString(R.string.vehicle)} ${data.vehicle}"
            dateCont.text = "${itemView.context.getString(R.string.date_contravention)} ${data.dateCont} ${data.timeCont}"
            dateRegistry.text = data.dateTimeRegistry
            val yes = itemView.context.getString(R.string.yes)
            val no = itemView.context.getString(R.string.no)
            when {
                data.disability -> cont.text = "${itemView.context.getString(R.string.contravention)} ${if (data.contraventions) yes else no} ${itemView.context.getString(R.string.by_disability)}"
                data.sr -> cont.text = "${itemView.context.getString(R.string.contravention)} ${if (data.contraventions) yes else no} ${itemView.context.getString(R.string.by_senior_citizens)}"
                else -> cont.text = "${itemView.context.getString(R.string.contravention)} ${if (data.contraventions) yes else no}"
            }
        }

    }

}