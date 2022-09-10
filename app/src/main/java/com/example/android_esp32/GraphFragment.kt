package com.example.android_esp32

import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat


fun String.getUrlText(): String {
    return URL(this).run {
        openConnection().run {
            this as HttpURLConnection
            inputStream.bufferedReader().readText()
        }
    }
}


// fun testList():MutableList<Pair<Double,Double>>{
//    val list= mutableListOf<Pair<Double,Double>>()
//    for(i in (-5.0..5.0 step 0.2)){
//        list.add(Pair(i.toDouble(),i*i*1.01))
//    }
//    return list
//}
class GraphFragment :Fragment(){
    var ip="192.168.4.1"
    var inP="1"
    var raise="3"
    var from="-3"
    var to="3"
    var step="0.1"
    var resultName=""
    var name:String=""
     lateinit var jsoNfile:JSONfile
    var jsonString = """
    {
       "data":[
          {
             "volt":-12,
             "ampere":"-1.4"
          },
          {
             "volt":8.2,
             "ampere":"1.6"
          },
          {
           "volt":-11.3,
            "ampere":"-10"
          },
           {
             "volt":12,
             "ampere":"15"
          },{
            "volt":-4.1,
            "ampere":-3.6
          },
          {
            "volt":0,
            "ampere":-7
          },
          {
          "volt":-1,
          "ampere":-3
          }
       ]
    }        
"""
    class MyModel(json: String) : JSONObject(json) {
        val ampere:Double = this.optDouble("ampere")
        val volt:Double = this.optDouble("volt")
    }
     class Response(json:String){
        var model=JSONArray(json)
         fun getList():List<Pair<Double,Double>>{
             var list= mutableListOf<Pair<Double,Double>>()
             for(i in 0 until model.length()){
                 val jsonObject=model.getJSONObject(i)
                 list.add(Pair(jsonObject.getDouble("volt"),jsonObject.getDouble("ampere")))
             }
             return list
         }

     }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun fillMapAndSetDesign():Array<DataPoint>{
        var string="http://$ip/metering?in=$inP&raise=$raise&from=$from&to=$to&step=$step"
        jsonString=""
        Thread {
            // Do network action in this function
            jsonString=string.getUrlText()

        }.start()
        while(true){
            if(jsonString!=""){
                break
            }
        }

        val myModel=Response(jsonString)
        var voltMax=Double.MIN_VALUE
        var voltMin=Double.MAX_VALUE
        var ameprMax=Double.MIN_VALUE
        var amperMin=Double.MIN_VALUE
        var list= mutableListOf<Pair<Double,Double>>()
        list.addAll(myModel.getList())
        val c=Calendar.getInstance()
        var Date=c.time
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        //jsoNfile=JSONfile(name = "Empty",list = myModel.getList(),c = dateFormat.format(Date))
        jsoNfile=JSONfile(name = name,list = myModel.getList(),c = dateFormat.format(Date))
        list.sortBy {
            it.first
        }
        voltMin=list.minBy {
            it.first
        }!!.first
        voltMax=list.maxBy {
            it.first
        }!!.first
        amperMin=list.minBy {
            it.second
        }!!.second
        ameprMax=list.maxBy {
            it.second
        }!!.second

        var lastList= mutableListOf<DataPoint>()
        list.forEach{
            lastList.add(DataPoint(it.first,it.second))
        }

        return lastList.toTypedArray()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_graph,container,false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val but2=getView()!!.findViewById<Button>(R.id.save)
        val but=getView()!!.findViewById<Button>(R.id.request)
        val but3=getView()!!.findViewById<Button>(R.id.addToDB)
        val txt=getView()!!.findViewById<TextView>(R.id.textView2)
        but.isClickable=false
        but3.visibility=View.INVISIBLE
        but.setOnClickListener{
            val graphView=getView()!!.findViewById<GraphView>(R.id.graph)
            val lineGraphSeries=LineGraphSeries<DataPoint>(fillMapAndSetDesign())
            lineGraphSeries.isDrawDataPoints=true
            val gridLabel = graphView.gridLabelRenderer
            gridLabel.horizontalAxisTitle = "volt"
            gridLabel.verticalAxisTitle="ampere"
            graphView.viewport.isScrollable=true
            graphView.viewport.isScalable=true
            lineGraphSeries.setOnDataPointTapListener { series, dataPoint ->
            val msg="X: "+dataPoint.x+"\nY: "+ String.format("%.6f",dataPoint.y)
            Toast.makeText(activity, msg,Toast.LENGTH_SHORT).show()}
            //jsoNfile.name=name
            logs.add(jsoNfile)
            graphView.addSeries(lineGraphSeries)
            val m=checkElem(jsoNfile.list as MutableList<Pair<Double, Double>>,context!!);
            txt.setText(m);
            if(m==""){

            }else{
                resultName=m
                but3.visibility=View.VISIBLE
            }
        }

        but2.setOnClickListener{
            but.isClickable=true
            val intent= Intent(activity,SettingActivity::class.java)
            startActivityForResult(intent,1)
        }
        but3.setOnClickListener{
            var db=DataBaseAccess.getInstance(context)
            db.open()
            db.insertIntoSecondTable(jsoNfile,ip,resultName)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(data==null) return
        var f=data.getStringExtra("Settings")!!.split(" ")
        if(f.size!=7){
            return
        }else{
            var f=data.getStringExtra("Settings")!!.split(" ")
            ip=f[0]
            inP=f[1]
            raise=f[2]
            from=f[3]
            to=f[4]
            step=f[5]
       }
    }


    //graphView.viewport.isXAxisBoundsManual=true
    // graphView.viewport.isYAxisBoundsManual=true
//            graphView.viewport.setMinX(lineGraphSeries.lowestValueX-1)
//            graphView.viewport.setMinY(lineGraphSeries.lowestValueX-1)
//            if(lineGraphSeries.highestValueX<0){
//                graphView.viewport.setMaxX(lineGraphSeries.highestValueX+1)
//            }else{
//                graphView.viewport.setMaxX(lineGraphSeries.highestValueX+1)
//            }
//            graphView.viewport.setMaxY(lineGraphSeries.highestValueY+1)
    //  viewport.isXAxisBoundsManual=true
    //viewport.isYAxisBoundsManual=true
//        viewport.setMinX(voltMin-10)
//        viewport.setMinY(amperMin-10)
//        if(voltMax<0){
//            viewport.setMaxX(voltMax-10)
//        }else{
//            viewport.setMaxX(voltMax+10)
//        }
//        if(ameprMax<0){
//            viewport.setMaxY(ameprMax-10)
//        }else{
//            viewport.setMaxY(ameprMax+10)
//        }
    //viewport.isScalable=true
    //viewport.isScrollable=true
}