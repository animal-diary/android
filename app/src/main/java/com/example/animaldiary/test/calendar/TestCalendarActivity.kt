package com.example.animaldiary.test.calendar

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animaldiary.databinding.ActivityTestCalendarBinding
import java.util.Calendar

class TestCalendarActivity : AppCompatActivity(), CalendarAdapter.OnItemClickListener {

    private lateinit var binding: ActivityTestCalendarBinding
    private lateinit var calendarAdapter: CalendarAdapter
    private val calendar = Calendar.getInstance()

    private lateinit var calendarView: RecyclerView
    private lateinit var prevButton: ImageView
    private lateinit var nextButton: ImageView
    private lateinit var monthYearTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // binding 초기화 후 뷰 변수들 초기화
        calendarView = binding.calendarComponent.rvCalendarDates
        prevButton = binding.calendarComponent.ivPreviousMonth
        nextButton = binding.calendarComponent.ivNextMonth
        monthYearTextView = binding.calendarComponent.tvMonthYear

        setupCalendar()
    }

    private fun setupCalendar() {


        calendarView.layoutManager = GridLayoutManager(this, 7)
        updateCalendar()

        prevButton.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateCalendar()
        }
        nextButton.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateCalendar()
        }
    }

    private fun updateCalendar() {
        val daysList = generateCalendarDays(calendar)
        calendarAdapter = CalendarAdapter(daysList, this)
        calendarView.adapter = calendarAdapter

        // 월/년도 텍스트뷰 업데이트
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        monthYearTextView.text = "${year}년 ${month}월"
    }

    // 캘린더에 표시될 날짜 목록을 생성 - 항상 42개(6주 * 7일)의 날짜
    private fun generateCalendarDays(cal: Calendar): List<CalendarDay> {
        val daysList = mutableListOf<CalendarDay>()
        val today = Calendar.getInstance()
        val currentMonth = cal.clone() as Calendar

        currentMonth.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = currentMonth.get(Calendar.DAY_OF_WEEK) - 1

        val prevMonth = cal.clone() as Calendar
        prevMonth.add(Calendar.MONTH, -1)
        val prevMonthDays = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in (firstDayOfWeek - 1) downTo 0) {
            daysList.add(CalendarDay(prevMonthDays - i, isCurrentMonth = false))
        }

        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..daysInMonth) {
            val isToday = today.get(Calendar.DAY_OF_MONTH) == i &&
                    today.get(Calendar.MONTH) == cal.get(Calendar.MONTH) &&
                    today.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
            daysList.add(CalendarDay(i, isCurrentMonth = true, isToday = isToday))
        }

        val totalDays = daysList.size
        val nextMonthDays = 42 - totalDays
        for (i in 1..nextMonthDays) {
            daysList.add(CalendarDay(i, isCurrentMonth = false))
        }
        return daysList
    }

    override fun onItemClick(positions: Set<Int>) {
        // 선택된 날짜들의 정보를 처리하는 로직
//        val selectedDays = positions.map { pos ->
//            val day = (binding.calendarComponent.rvCalendarDates.adapter as CalendarAdapter).days[pos]
//            day.day
//        }
//        val message = if (selectedDays.isEmpty()) {
//            "선택된 날짜 없음"
//        } else {
//            "선택된 날짜: ${selectedDays.joinToString()}"
//        }
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}