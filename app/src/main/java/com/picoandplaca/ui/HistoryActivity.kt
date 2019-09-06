package com.picoandplaca.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.picoandplaca.R
import com.picoandplaca.data.DataPreference.listHistory
import com.picoandplaca.ui.adapter.HistoryAdapter
import com.picoandplaca.utils.ConsFun.stringToListData
import kotterknife.bindView

class HistoryActivity : AppCompatActivity() {

    private val recycler : RecyclerView by bindView(R.id.list_history)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        initializeRecycler()
    }

    private fun initializeRecycler(){
        val adapterHistory = HistoryAdapter(stringToListData(listHistory))
        val layout = LinearLayoutManager(this)
        layout.stackFromEnd = true
        layout.reverseLayout = true
        recycler.apply {
            layoutManager = layout
            adapter = adapterHistory
            recycledViewPool.clear()
        }
        adapterHistory.notifyDataSetChanged()
    }



}