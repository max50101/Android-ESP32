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

class DbFragmentTwo:Fragment() {
    var secondDB= mutableListOf<DBTableTwo>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_db,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val db=DataBaseAccess.getInstance(context)
         val lv=getView()!!.findViewById<ListView>(R.id.listView3)
        val c=DataBaseAccess.getInstance(this.context);
        c.open()
        secondDB=c.secondDB
        var dbAdapter=DBAdapterTableTwo(this.context,secondDB);
        c.close()
        lv.adapter=dbAdapter
        lv.setOnItemClickListener(this::onItemClick)
    }
    fun onItemClick(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
       // var str= Gson().toJson(firstDB[position],DbClassOne::class.java)
        val db=DataBaseAccess.getInstance(context)
        val intent= Intent(activity,DBTableTwoActivity::class.java)
        intent.putExtra("Data",secondDB[position].id)
        activity!!.startActivity(intent)
    }

}