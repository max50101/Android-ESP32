package com.example.android_esp32

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.gson.Gson

class DbFragmentOne: Fragment() {
    var firstDB= mutableListOf<DbClassOne>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_db,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val db=DataBaseAccess.getInstance(context)
//        db.open()
//        db.insertIntoMainTable(activity,"f(x)=x*x")
//        db.close()
        val lv=getView()!!.findViewById<ListView>(R.id.listView3)
        val c=DataBaseAccess.getInstance(this.context);
        c.open()
        firstDB=c.dbFirst
        var dbAdapter=DBAdapter(this.context,firstDB);
        c.close()
        lv.adapter=dbAdapter
        lv.setOnItemClickListener(this::onItemClick)
    }
    fun onItemClick(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
        var str= Gson().toJson(firstDB[position],DbClassOne::class.java)
        val db=DataBaseAccess.getInstance(context)
//        db.open()
//        db.insertMainMeasurments(firstDB[position].id)
//        db.close()
        val intent= Intent(activity,DBTableOneActivity::class.java)
          intent.putExtra("Data",firstDB[position].id)
        activity!!.startActivity(intent)
    }


}