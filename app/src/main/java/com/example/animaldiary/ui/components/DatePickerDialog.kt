package com.example.animaldiary.ui.components

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.view.isVisible
import com.example.animaldiary.R
import java.util.*

class DatePickerDialog(
    context: Context,
    private val showDay: Boolean = true,
    private val initialYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    private val initialMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    private val initialDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
    private val initialIsSolar: Boolean = true
) : Dialog(context, R.style.DialogTheme) {


    private lateinit var calendarTypeContainer: LinearLayout
    private lateinit var solarChip: DatePickerItemView
    private lateinit var lunarChip: DatePickerItemView
    private lateinit var yearContainer: ScrollView
    private lateinit var monthContainer: ScrollView
    private lateinit var dayContainer: ScrollView
    private lateinit var dayColumn: LinearLayout
    private lateinit var cancelButton: Button
    private lateinit var confirmButton: Button

    private var isSolarCalendar: Boolean = initialIsSolar
    private var selectedYear: Int = initialYear
    private var selectedMonth: Int = initialMonth
    private var selectedDay: Int = initialDay

    private var onDateSelectedListener: ((year: Int, month: Int, day: Int, isSolar: Boolean) -> Unit)? = null
    private var onCancelListener: (() -> Unit)? = null

    // 년,월,일
    private val yearItems = mutableListOf<DatePickerItemView>()
    private val monthItems = mutableListOf<DatePickerItemView>()
    private val dayItems = mutableListOf<DatePickerItemView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_date_picker, null)
        setContentView(view)

        window?.apply {
            val margin = context.resources.getDimensionPixelSize(R.dimen.spacing_4xl) // 20dp
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
            // 다이얼로그 표시
            attributes?.windowAnimations = R.style.DialogAnimation

            decorView.setPadding(margin, 0, margin, 0)
        }

        initViews(view)
        setupCalendarTypeChips()
        setupDateItems()
        setupButtons()
        updateVisibility()
        updateSelectedDate()
    }

    private fun initViews(view: View) {
        calendarTypeContainer = view.findViewById(R.id.dp_calendar_type_container)
        solarChip = view.findViewById(R.id.dp_solar_chip)
        lunarChip = view.findViewById(R.id.dp_lunar_chip)
        yearContainer = view.findViewById(R.id.dp_year_container)
        monthContainer = view.findViewById(R.id.dp_month_container)
        dayContainer = view.findViewById(R.id.dp_day_container)
        dayColumn = view.findViewById(R.id.dp_day_column)
        cancelButton = view.findViewById(R.id.dp_cancel_button)
        confirmButton = view.findViewById(R.id.dp_confirm_button)
    }

    private fun setupCalendarTypeChips() {
        solarChip.setText("양력")
        solarChip.setItemType(DatePickerItemType.CHIP) // 칩용 스타일
        lunarChip.setText("음력")
        lunarChip.setItemType(DatePickerItemType.CHIP) // 칩용 스타일

        solarChip.setOnItemClickListener {
            if (!isSolarCalendar) {
                isSolarCalendar = true
                updateCalendarTypeSelection()
            }
        }

        lunarChip.setOnItemClickListener {
            if (isSolarCalendar) {
                isSolarCalendar = false
                updateCalendarTypeSelection()
            }
        }

        updateCalendarTypeSelection()
    }

    private fun updateCalendarTypeSelection() {
        solarChip.setItemSelected(isSolarCalendar)
        lunarChip.setItemSelected(!isSolarCalendar)
    }

    private fun setupDateItems() {
        val yearScrollContainer = (yearContainer.getChildAt(0) as LinearLayout)
        val monthScrollContainer = (monthContainer.getChildAt(0) as LinearLayout)
        val dayScrollContainer = (dayContainer.getChildAt(0) as LinearLayout)

        //년도 아이템 (현재 년 기준 +-10년)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        for (year in (currentYear - 10)..(currentYear + 10)) {
            val yearItem = DatePickerItemView(context).apply {
                setText("${year}년")
                setItemType(DatePickerItemType.DATE) // 년월일용 스타일
                setOnItemClickListener {
                    selectYear(year)
                }
            }
            yearItems.add(yearItem)
            yearScrollContainer.addView(yearItem)

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.bottomMargin = context.resources.getDimensionPixelSize(R.dimen.spacing_sm)
            yearItem.layoutParams = layoutParams
        }

        //월 아이템(1-12월)
        for (month in 1..12) {
            val monthItem = DatePickerItemView(context).apply {
                setText("${month}월")
                setItemType(DatePickerItemType.DATE)
                setOnItemClickListener {
                    selectMonth(month)
                }
            }
            monthItems.add(monthItem)
            monthScrollContainer.addView(monthItem)

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.bottomMargin = context.resources.getDimensionPixelSize(R.dimen.spacing_sm)
            monthItem.layoutParams = layoutParams
        }

        // 일 아이템 (1-31일)
        for (day in 1..31) {
            val dayItem = DatePickerItemView(context).apply {
                setText("${day}일")
                setItemType(DatePickerItemType.DATE)
                setOnItemClickListener {
                    selectDay(day)
                }
            }
            dayItems.add(dayItem)
            dayScrollContainer.addView(dayItem)

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.bottomMargin = context.resources.getDimensionPixelSize(R.dimen.spacing_sm)
            dayItem.layoutParams = layoutParams
        }
    }

    private fun selectYear(year: Int) {
        selectedYear = year
        updateYearSelection()
        updateDayItems()
    }

    private fun selectMonth(month: Int) {
        selectedMonth = month
        updateMonthSelection()
        updateDayItems()
    }

    private fun selectDay(day: Int) {
        selectedDay = day
        updateDaySelection()
    }

    private fun updateYearSelection() {
        yearItems.forEachIndexed { index, item ->
            val year = Calendar.getInstance().get(Calendar.YEAR) - 10 + index
            val selected = (year == selectedYear)
            item.setItemSelected(selected)
            if (selected) {
                yearContainer.post {
                    val centerY = (item.top + item.height / 2) - (yearContainer.height / 2)
                    yearContainer.smoothScrollTo(0, centerY.coerceAtLeast(0))
                }
            }
        }
    }

    private fun updateMonthSelection() {
        monthItems.forEachIndexed { index, item ->
            val selected = (index + 1 == selectedMonth)
            item.setItemSelected(selected)
            if (selected) {
                monthContainer.post {
                    val centerY = (item.top + item.height / 2) - (monthContainer.height / 2)
                    monthContainer.smoothScrollTo(0, centerY.coerceAtLeast(0))
                }
            }
        }
    }

    private fun updateDaySelection() {
        dayItems.forEachIndexed { index, item ->
            val selected = (index + 1 == selectedDay)
            item.setItemSelected(selected)
            if (selected) {
                dayContainer.post {
                    val centerY = (item.top + item.height / 2) - (dayContainer.height / 2)
                    dayContainer.smoothScrollTo(0, centerY.coerceAtLeast(0))
                }
            }
        }
    }


    private fun updateDayItems() {
        val calendar = Calendar.getInstance().apply {
            set(selectedYear, selectedMonth - 1, 1)
        }
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        if (selectedDay > maxDay) {
            selectedDay = maxDay
        }

        dayItems.forEachIndexed { index, item ->
            val day = index + 1
            if (day <= maxDay) {
                item.visibility = View.VISIBLE
                item.setItemSelected(day == selectedDay)
            } else {
                item.visibility = View.GONE
            }
        }
    }

    private fun setupButtons() {
        cancelButton.setOnClickListener {
            onCancelListener?.invoke()
            dismiss()
        }

        confirmButton.setOnClickListener {
            onDateSelectedListener?.invoke(selectedYear, selectedMonth, selectedDay, isSolarCalendar)
            dismiss()
        }
    }

    private fun updateVisibility() {
        dayColumn.isVisible = showDay
    }

    private fun updateSelectedDate() {
        updateCalendarTypeSelection()
        updateYearSelection()
        updateMonthSelection()
        updateDaySelection()
        updateDayItems()
    }

    fun setOnDateSelectedListener(listener: (year: Int, month: Int, day: Int, isSolar: Boolean) -> Unit) {
        onDateSelectedListener = listener
    }

    fun setOnCancelListener(listener: () -> Unit) {
        onCancelListener = listener
    }

    fun getSelectedDate(): Triple<Int, Int, Int> {
        return Triple(selectedYear, selectedMonth, selectedDay)
    }

    fun isSolarCalendar(): Boolean {
        return isSolarCalendar
    }
}