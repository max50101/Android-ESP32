package com.example.android_esp32

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SettingActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)
        val wiFiBut=findViewById<Button>(R.id.button)
        wiFiBut.setOnClickListener{
            AlertDialog.Builder(it.context)
                .setTitle("Android ESP-32")
                .setMessage("Please turn choose WiFi on the following screen.")
                .setPositiveButton("OK") { dialog, id ->
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
                .setNegativeButton("Cancel", null)
                .show();
        }
        val idF=findViewById<EditText>(R.id.editTextTextPersonName4)
        val inF=findViewById<EditText>(R.id.editTextTextPersonName5)
        val raiseF=findViewById<EditText>(R.id.editTextTextPersonName6)
        val fromF=findViewById<EditText>(R.id.editTextTextPersonName7)
        val toF=findViewById<EditText>(R.id.editTextTextPersonName8)
        val stepF=findViewById<EditText>(R.id.editTextTextPersonName9)
        val nextBt=findViewById<Button>(R.id.button3)
        nextBt.setOnClickListener{
            var sb=StringBuilder()
            sb.append(idF.text).append(" ").append(inF.text).append(" ").append(raiseF.text).append(" ").append(fromF.text).append(" ").append(toF.text).append(" ").append(stepF.text).append(" ")
            val intent = Intent()
            intent.putExtra("Settings",sb.toString())
            setResult(RESULT_OK, intent)
            finish()
        }

    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CANCELED)
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}