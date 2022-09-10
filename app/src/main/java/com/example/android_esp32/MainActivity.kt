package com.example.android_esp32


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), PackageManager.PERMISSION_GRANTED
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btEx = findViewById<Button>(R.id.exBtn)
        btEx.setOnClickListener { finish() }
        val btStart = findViewById<Button>(R.id.launch_graph)
        val btDB=findViewById<Button>(R.id.DB)
        val popupMenu = androidx.appcompat.widget.PopupMenu(this, btStart)
        popupMenu.inflate(R.menu.popup)
        
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu1 -> {
                    val intent= Intent(this,GraphActivity::class.java)
                    intent.putExtra("IsGraph",true)
                    startActivity(intent)
                    true
                }
                R.id.menu2 -> {
                    val intent= Intent(this,GraphActivity::class.java)
                    intent.putExtra("IsGraph",false)
                    startActivity(intent)
                    true
                }
                R.id.menu3 -> {
                    closeContextMenu()
                   // btEx.visibility=View.VISIBLE
                    true
                }
                else -> false
            }
        }
        popupMenu.setOnDismissListener{btEx.visibility=View.VISIBLE
            btDB.visibility=View.VISIBLE
        }
        val popupMenu2=androidx.appcompat.widget.PopupMenu(this, btDB)
        popupMenu2.inflate(R.menu.db_menu)
        popupMenu2.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu1->{
                    val intent=Intent(this,DbMenu::class.java)
                    intent.putExtra("IsFirst",true)
                    startActivity(intent)
                    true
                }
                R.id.menu2->{
                    val intent=Intent(this,DbMenu::class.java)
                    intent.putExtra("IsFirst",false)
                    startActivity(intent)
                    true
                }
                R.id.menu3 -> {
                    closeContextMenu()
                    // btEx.visibility=View.VISIBLE
                    true
                }
                else->false
            }
        }
        popupMenu2.setOnDismissListener{btEx.visibility=View.VISIBLE
            btStart.visibility=View.VISIBLE
        }
        btStart.setOnClickListener{
            btEx.visibility=View.INVISIBLE
            btDB.visibility=View.INVISIBLE
            popupMenu.show()
        }
        btDB.setOnClickListener{
            btStart.visibility=View.INVISIBLE
            btEx.visibility=View.INVISIBLE
            popupMenu2.show()
        }
    }





}