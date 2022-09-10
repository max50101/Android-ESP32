package com.example.android_esp32

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.util.*


//

fun GraphView.addListone(list: List<Pair<Double, Double>>){
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
class DBTableOneActivity:AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.db_table_one_activity)
        val value=intent.getIntExtra("Data",0)
        var db=DataBaseAccess.getInstance(applicationContext)
        db.open()
        var dbClassOne=db.getDBFirstTable(value)
        val tv=findViewById<TextView>(R.id.Name)
        tv.text = dbClassOne.name
        val imageView=findViewById<ImageView>(R.id.imageView)
        val bm = BitmapFactory.decodeByteArray(dbClassOne.img, 0, dbClassOne.img.size)
        imageView.setImageBitmap(bm)
        val graphView=findViewById<GraphView>(R.id.graph5)
        val list=db.getMeasurmentsFirst(value)
        var listg= mutableListOf<Pair<Double,Double>>()
        listg.addAll(list)
        graphView.addListone(listg)
    }
}