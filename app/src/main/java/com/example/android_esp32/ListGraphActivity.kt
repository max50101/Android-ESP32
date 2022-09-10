package com.example.android_esp32

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

import org.w3c.dom.Text

fun GraphView.addList(list: List<Pair<Double, Double>>){
    var lastList= mutableListOf<DataPoint>()
    list.sortedBy {
        it.first
    }
    list.forEach{
        lastList.add(DataPoint(it.first,it.second))
    }
    val lineGraphSeries= LineGraphSeries<DataPoint>(lastList.toTypedArray())
    lineGraphSeries.isDrawDataPoints=true
//    this.viewport.isXAxisBoundsManual=true
//    this.viewport.isYAxisBoundsManual=true
//    this.viewport.setMinX(lineGraphSeries.lowestValueX-1)
//    this.viewport.setMinY(lineGraphSeries.lowestValueX-1)
//    if(lineGraphSeries.highestValueX<0){
//        this.viewport.setMaxX(lineGraphSeries.highestValueX+1)
//    }else{
//        this.viewport.setMaxX(lineGraphSeries.highestValueX+1)
//    }
//    this.viewport.setMaxY(lineGraphSeries.highestValueY+1)
    this.viewport.isScrollable=true
    this.viewport.isScalable=true
    val gridLabel = this.gridLabelRenderer
    gridLabel.horizontalAxisTitle = "volt"
    gridLabel.verticalAxisTitle="ampere"
    lineGraphSeries.setOnDataPointTapListener { series, dataPoint ->
        val msg="X: "+dataPoint.x+"\nY: "+ String.format("%.6f",dataPoint.y)
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()}
    this.addSeries(lineGraphSeries)

}
class ListGraphActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listgraphactivity)
        val value=intent.getStringExtra("Data")
        val jsonObject=Gson().fromJson<JSONfile>(value, JSONfile::class.java)
        val graphView=findViewById<GraphView>(R.id.graph5)
        val tv=findViewById<TextView>(R.id.textView4)
        graphView.addList(jsonObject.list)
        val bt=findViewById<Button>(R.id.addToDB)

        val m=checkElem(jsonObject.list as MutableList<Pair<Double, Double>>,applicationContext)
        if(m==""){
            tv.text="This element has no type in DataBase"
            bt.visibility= View.INVISIBLE
        }else{
            tv.text=jsonObject.name+"has type of:"+m
        }
        bt.setOnClickListener{
            val db=DataBaseAccess.getInstance(applicationContext)
            db.open()
            db.insertIntoSecondTable(jsonObject,"192.168.4.1",m)
        }
        val dataTV=findViewById<TextView>(R.id.date)
        val sb=StringBuilder()
        sb.append("Measurments was done on: ")
        sb.append(jsonObject.c)
        dataTV.setText(sb.toString())

    }
}