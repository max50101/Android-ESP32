package com.example.android_esp32

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson


class ListFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val list= logs
        val lv=getView()!!.findViewById<ListView>(R.id.listView1)
        var jsonAdapter=JSONAdapter(this.context, getLogsTwo())
        lv.adapter=jsonAdapter
        lv.setOnItemClickListener(this::onItemClick)
    }
    fun onItemClick(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
        var str=Gson().toJson(logs[position],JSONfile::class.java)
        val intent= Intent(activity,ListGraphActivity::class.java)

        intent.putExtra("Data",str)
        activity!!.startActivity(intent)
    }

}