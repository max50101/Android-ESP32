package com.example.android_esp32

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView

class DBTableTwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.db_table_two_activity)
        val value = intent.getIntExtra("Data", 0)
        var db = DataBaseAccess.getInstance(applicationContext)
        db.open()
        var dbClassTwo = db.getDBSecondTable(value)
        val name = findViewById<TextView>(R.id.Name)
        val date=findViewById<TextView>(R.id.Date)
        val result=findViewById<TextView>(R.id.result)
        name.text = dbClassTwo.name
        val sb=StringBuilder()
        sb.append("Measurments was done on: ")
        sb.append(dbClassTwo.date)
        date.text=sb.toString()
        result.text = "The measurments was done from this ip: "+dbClassTwo.ip+"\n Type is: "+db.getName(dbClassTwo.element_Id)
        val graphView = findViewById<GraphView>(R.id.graph5)
        val list = db.getMeasurmentsSecond(value)
        var listg = mutableListOf<Pair<Double, Double>>()
        listg.addAll(list)
        graphView.addList(listg)
    }
}