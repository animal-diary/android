package com.example.animaldiary.test

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import com.example.animaldiary.R


class TestPrimaryIconButtonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_primary_icon_btn)

        val btnDefault  = findViewById<AppCompatImageButton>(R.id.btnPrimary32Default)
        val btnPressed  = findViewById<AppCompatImageButton>(R.id.btnPrimary32Pressed)
        // val btnDisabled = findViewById<AppCompatImageButton>(R.id.btnPrimary32Disabled)

        btnPressed.isPressed = true
        btnPressed.refreshDrawableState()

        // 클릭 데모
        btnDefault.setOnClickListener {
            Toast.makeText(this, "Default clicked", Toast.LENGTH_SHORT).show()
        }
        btnPressed.setOnClickListener {
            Toast.makeText(this, "Pressed sample clicked", Toast.LENGTH_SHORT).show()
        }

    }
}
