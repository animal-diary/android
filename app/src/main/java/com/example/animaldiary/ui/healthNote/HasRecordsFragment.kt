package com.example.animaldiary.ui.healthNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.animaldiary.databinding.FragmentHasRecordsBinding
import com.example.animaldiary.test.calendar.CalendarAdapter
import com.example.animaldiary.test.calendar.CalendarDay
import com.example.animaldiary.ui.components.DatePickerDialog
import com.example.animaldiary.ui.components.TabsView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Calendar

class HasRecordsFragment : Fragment(), CalendarAdapter.OnItemClickListener {

    private var _binding: FragmentHasRecordsBinding? = null
    private val binding get() = _binding!!

    //탭 제목 목록 (더미 데이터)
    private val tabTitles = listOf(
        "식욕", "기력상태", "특이사항", "체중", "물섭취량", "구토", "심박수", "호흡수",
        "콧물상태", "기절", "이상소리", "걷는 모습", "피부", "배변", "소변", "경련")

    private lateinit var calendarAdapter: CalendarAdapter
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHasRecordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 캘린더 설정
        setupCalendar()

        // 탭과 뷰페이저 설정
        setupTabsWithViewPager()

    }

    private fun setupTabsWithViewPager() {
        // ViewPager2에 어댑터 설정
        binding.viewPager.adapter = HasRecordsPagerAdapter(this, tabTitles)

        // TabLayout과 ViewPager2를 연결하고 탭 디자인을 커스터마이징
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            // 각 탭에 커스텀 뷰(TabsView) 설정
            val customTabView = TabsView(binding.tabLayout.context)
            customTabView.setTabTitle(tabTitles[position])
            tab.customView = customTabView
        }.attach()

        binding.tabLayout.post {
            setTabMargins()
        }

        // 탭 선택 리스너를 추가하여 커스텀 뷰의 상태를 제어
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            // 탭이 선택되었을 때
            override fun onTabSelected(tab: TabLayout.Tab?) {
                (tab?.customView as? TabsView)?.isSelected = true
            }

            // 탭이 선택 해제되었을 때
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                (tab?.customView as? TabsView)?.isSelected = false
            }

            // 탭이 다시 선택되었을 때
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 필요하다면 추가 동작 구현
            }
        })
    }

    private fun setTabMargins() {
        val tabCount = binding.tabLayout.tabCount
        val slidingTabStrip = binding.tabLayout.getChildAt(0) as ViewGroup
        val marginPx = (8 * resources.displayMetrics.density).toInt()

        for (i in 0 until tabCount) {
            val tab = slidingTabStrip.getChildAt(i)
            val layoutParams = tab.layoutParams as ViewGroup.MarginLayoutParams

            when (i) {
                0 -> {
                    // 첫 번째 탭: 오른쪽에만 마진
                    layoutParams.setMargins(0, 0, marginPx / 2, 0)
                }
                tabCount - 1 -> {
                    // 마지막 탭: 왼쪽에만 마진
                    layoutParams.setMargins(marginPx / 2, 0, 0, 0)
                }
                else -> {
                    // 중간 탭들: 양쪽에 마진의 절반씩
                    layoutParams.setMargins(marginPx / 2, 0, marginPx / 2, 0)
                }
            }
            tab.layoutParams = layoutParams
        }
    }

    // 캘린더 설정
    private fun setupCalendar() {
        // 캘린더 RecyclerView 초기화
        binding.calendarComponent.rvCalendarDates.layoutManager = GridLayoutManager(context, 7)

        // 이전/다음 달 버튼 및 월/년도 텍스트뷰에 클릭 리스너 설정
        binding.calendarComponent.ivPreviousMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateCalendar()
        }
        binding.calendarComponent.ivNextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateCalendar()
        }

        binding.calendarComponent.tvMonthYear.setOnClickListener {
            // DatePicker 다이얼로그를 표시합니다.
            val datePickerDialog = DatePickerDialog(
                context = requireContext(),
                showDay = false,
                initialYear = calendar.get(Calendar.YEAR),
                initialMonth = calendar.get(Calendar.MONTH) + 1,
                initialDay = calendar.get(Calendar.DAY_OF_MONTH),
                isFragment = true
            )

            datePickerDialog.setOnDateSelectedListener { year, month, _, _ ->
                // 선택된 년도와 월로 캘린더를 업데이트합니다.
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month - 1)
                updateCalendar()
            }

            datePickerDialog.show()
        }

        // 캘린더 초기 상태 업데이트
        updateCalendar()
    }

    private fun updateCalendar() {
        val daysList = generateCalendarDays(calendar, 6)
        calendarAdapter = CalendarAdapter(daysList, this)
        binding.calendarComponent.rvCalendarDates.adapter = calendarAdapter

        // 월/년도 텍스트뷰 업데이트
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        binding.calendarComponent.tvMonthYear.text = "${year}년 ${month}월"
    }

    // 캘린더에 표시될 날짜 목록을 생성
    private fun generateCalendarDays(cal: Calendar, numberOfWeeks: Int): List<CalendarDay> {
        val daysList = mutableListOf<CalendarDay>()
        val today = Calendar.getInstance()
        val currentMonth = cal.clone() as Calendar

        currentMonth.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = currentMonth.get(Calendar.DAY_OF_WEEK) - 1 // 일요일 = 0

        // 이전 달 날짜 추가
        val prevMonth = cal.clone() as Calendar
        prevMonth.add(Calendar.MONTH, -1)
        val prevMonthDays = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in (firstDayOfWeek - 1) downTo 0) {
            daysList.add(CalendarDay(prevMonthDays - i, isCurrentMonth = false))
        }

        // 현재 달 날짜 추가
        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..daysInMonth) {
            val isToday = today.get(Calendar.DAY_OF_MONTH) == i &&
                    today.get(Calendar.MONTH) == cal.get(Calendar.MONTH) &&
                    today.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
            daysList.add(CalendarDay(i, isCurrentMonth = true, isToday = isToday))
        }

        // 다음 달 날짜 추가
        val totalCalendarDays = numberOfWeeks * 7
        while (daysList.size > totalCalendarDays) {
            daysList.removeAt(daysList.lastIndex)
        }
        val nextMonthDays = totalCalendarDays - daysList.size
        for (i in 1..nextMonthDays) {
            daysList.add(CalendarDay(i, isCurrentMonth = false))
        }

        return daysList
    }

    // 날짜 클릭 시 동작
    override fun onItemClick(positions: Set<Int>) {
        // TODO: 선택된 날짜들의 정보를 처리하는 로직을 여기에 구현
        // 예: `positions`를 사용하여 선택된 날짜에 대한 데이터를 로드하고 뷰를 업데이트
        // `positions`는 선택된 아이템들의 어댑터 위치(Int)를 담고 있는 Set입니다.
        // `calendarAdapter.days`를 사용하여 해당 위치의 CalendarDay 객체에 접근할 수 있습니다.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}