package com.example.android_esp32

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DbMenu:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.db_menu)
        val intent=intent
        val value=intent.getBooleanExtra("IsFirst",false)
        val firstFragment=DbFragmentOne()
        val secondFragment=DbFragmentTwo()
        if(value){
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fLfragment, firstFragment)
                commit()
            }
        }else{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fLfragment, secondFragment)
                commit()
            }
        }
    }
}