package com.example.animaldiary.test

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.animaldiary.R
import com.example.animaldiary.ui.components.DatePickerDialog
import java.util.*

class TestDatePickerDialogActivity : AppCompatActivity() {

    private lateinit var showDatePickerButton: Button
    private lateinit var showDatePickerNoDayButton: Button
    private lateinit var selectedDateText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_date_picker_dialog)

        showDatePickerButton = findViewById(R.id.btn_show_date_picker)
        showDatePickerNoDayButton = findViewById(R.id.btn_show_date_picker_no_day)
        selectedDateText = findViewById(R.id.tv_selected_date)

        setupButtons()
    }

    private fun setupButtons() {
        // 전체 날짜 선택 (년월일)
        showDatePickerButton.setOnClickListener {
            showDatePickerDialog(showDay = true)
        }

        // 년월만 선택
        showDatePickerNoDayButton.setOnClickListener {
            showDatePickerDialog(showDay = false)
        }
    }

    private fun showDatePickerDialog(showDay: Boolean) {
        val currentCalendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            context = this,
            showDay = showDay,
            initialYear = currentCalendar.get(Calendar.YEAR),
            initialMonth = currentCalendar.get(Calendar.MONTH) + 1,
            initialDay = currentCalendar.get(Calendar.DAY_OF_MONTH),
            initialIsSolar = true
        )

        // 날짜 선택 완료 리스너
        datePickerDialog.setOnDateSelectedListener { year, month, day, isSolar ->
            val calendarType = if (isSolar) "양력" else "음력"
            val dateStr = if (showDay) {
                "${year}년 ${month}월 ${day}일 ($calendarType)"
            } else {
                "${year}년 ${month}월 ($calendarType)"
            }

            selectedDateText.text = "선택된 날짜: $dateStr"
            Toast.makeText(this, "날짜 선택: $dateStr", Toast.LENGTH_SHORT).show()
        }

        // 취소 리스너
        datePickerDialog.setOnCancelListener {
            Toast.makeText(this, "날짜 선택 취소", Toast.LENGTH_SHORT).show()
        }

        // 다이얼로그 표시
        datePickerDialog.show()
    }
}