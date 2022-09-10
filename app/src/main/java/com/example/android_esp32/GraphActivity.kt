package com.example.android_esp32

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

var firstFragment=GraphFragment()
class GraphActivity : AppCompatActivity(),DialogClass.ExampleDialogListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        val actionBar=actionBar
//        actionBar!!.title="Graphics"
       // actionBar.setDisplayHomeAsUpEnabled(true)
        val intent=intent
        val value=intent.getBooleanExtra("IsGraph",false)
        //val firstFragment=GraphFragment()
        val secondFragment=ListFragment()
        if(value) {

            openDialog()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fLfragment, firstFragment)
                commit()
            }
        }else{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fLfragment,secondFragment)
                commit()
            }
        }

    }
    fun openDialog() {
        val exampleDialog = DialogClass()
        exampleDialog.show(supportFragmentManager, "example dialog")
    }
    override fun applyTexts(username: String?) {
        firstFragment.name=username!!
    }
}