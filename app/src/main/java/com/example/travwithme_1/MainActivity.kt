package com.example.travwithme_1
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button


class MainActivity : Activity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonChecklist = findViewById<Button>(R.id.checklistButton)

        buttonChecklist.setOnClickListener{
            val intent = Intent(this, Checklist::class.java)
            startActivity(intent)
        }
    }


}
