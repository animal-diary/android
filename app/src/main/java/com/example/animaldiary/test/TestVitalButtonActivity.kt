package com.example.animaldiary.test

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.animaldiary.R

class TestVitalButtonActivity : AppCompatActivity() {

    private lateinit var vitalButton: FrameLayout
    private lateinit var vitalIcon: ImageView

    private var isHolding = false
    private var isEnabledState = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_vital_button)

        vitalButton = findViewById(R.id.vitalButton)
        vitalIcon = findViewById(R.id.vitalIcon)

        setDefaultState()

        vitalButton.setOnTouchListener { v, event ->
            if (!isEnabledState) return@setOnTouchListener true

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isHolding = true
                    v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).start()
                    setIcon(R.drawable.ic_vital_button)
                }

                MotionEvent.ACTION_MOVE -> {
                    // 유지 중
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isHolding = false
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                    setIcon(R.drawable.ic_vital_button)
                }
            }
            true
        }

        vitalButton.postDelayed({
            disableVitalButton()
        }, 5000)
    }

    private fun setIcon(resId: Int) {
        val icon: Drawable? = ContextCompat.getDrawable(this, resId)
        vitalIcon.setImageDrawable(icon)
    }

    private fun setDefaultState() {
        isEnabledState = true
        vitalButton.isEnabled = true
        vitalButton.alpha = 1.0f
        setIcon(R.drawable.ic_vital_button)
    }

    private fun disableVitalButton() {
        isEnabledState = false
        vitalButton.isEnabled = false
        vitalButton.alpha = 0.5f
        setIcon(R.drawable.ic_vital_button_disabled)
    }
}
