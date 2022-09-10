package com.example.android_esp32

import android.content.Context
val logs= mutableListOf<JSONfile>()
fun getLogsTwo():MutableList<JSONfile>{
    return logs
}
fun testList2():MutableList<Pair<Double,Double>>{
    val list= mutableListOf<Pair<Double,Double>>()
    for(i in (-5.0..5.0 step 0.2)){
        list.add(Pair(i.toDouble(),i*i))
    }
    return list
}
fun checkElem(list:MutableList<Pair<Double,Double>>,context: Context):String{
    val db=DataBaseAccess.getInstance(context)
    db.open()
    var eps=0.5
    val listDB=db.getDBFirst();
    for(i in 0 until listDB.size){
        val measurments=db.getMeasurmentsFirst(listDB[i].id);
        var size=0
        if(measurments.size>list.size){
            size=list.size
        }else{
            size=measurments.size
        }
        var bool=true
        for(y in 0 until size){
            var voltL=list[y].first
            var voltD=measurments[y].first
            if(Math.abs(voltL-voltD)>1.2){
                continue
            }else{
                var ampereL=list[y].second
                var ampereD=measurments[y].second
                if(Math.pow((ampereL-ampereD),2.0)<eps){
                    continue
                }else{
                    bool=false
                    break
                }
            }
        }
        if(bool) {
            db.close()
            return listDB[i].name
        }
    }
    db.close()
    return ""
}
public infix fun ClosedRange<Double>.step(step: Double): Iterable<Double> {
    require(start.isFinite())
    require(endInclusive.isFinite())
    require(step > 0.0) { "Step must be positive, was: $step." }
    val sequence = generateSequence(start) { previous ->
        if (previous == Double.POSITIVE_INFINITY) return@generateSequence null
        val next = previous + step
        if (next > endInclusive) null else next
    }
    return sequence.asIterable()
}