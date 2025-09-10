package com.example.animaldiary.ui.healthNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.animaldiary.databinding.FragmentHealthRecordDetailBinding
import com.example.animaldiary.ui.components.TabsView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HealthRecordDetailFragment : Fragment() {

    private var _binding: FragmentHealthRecordDetailBinding? = null
    private val binding get() = _binding!!

    // 탭 제목 목록 (더미 데이터)
    private val tabTitles = listOf(
        "전체", "식욕", "기력상태", "특이사항", "체중", "물섭취량", "구토", "심박수", "호흡수",
        "콧물상태", "기절", "이상소리", "걷는 모습", "피부", "배변", "소변", "경련")

    companion object {
        private const val ARG_YEAR = "selected_year"
        private const val ARG_MONTH = "selected_month"
        private const val ARG_DAY = "selected_day"

        fun newInstance(year: Int, month: Int, day: Int): HealthRecordDetailFragment {
            return HealthRecordDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_YEAR, year)
                    putInt(ARG_MONTH, month)
                    putInt(ARG_DAY, day)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHealthRecordDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTopNavigationView()
        displayRecordDate()
        setupTabsWithViewPager()

        // 시스템 뒤로가기 대응
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    (parentFragment as? HealthNoteFragment)?.showParentViews()
                    parentFragmentManager.popBackStack()
                }
            }
        )
    }

    private fun setupTopNavigationView() {
        binding.tnvRecordDetail.leftIcon.setOnClickListener {
            // 부모 프래그먼트의 뷰를 다시 보이게 함
            (parentFragment as? HealthNoteFragment)?.showParentViews()

            // 프래그먼트에서 뒤로가기
            parentFragmentManager.popBackStack()
        }
    }

    private fun displayRecordDate() {
        // arguments에서 날짜 정보를 가져옵니다.
        val year = arguments?.getInt(ARG_YEAR, -1) ?: -1
        val month = arguments?.getInt(ARG_MONTH, -1) ?: -1
        val day = arguments?.getInt(ARG_DAY, -1) ?: -1

        if (year != -1 && month != -1 && day != -1) {
            val calendar = Calendar.getInstance().apply {
                set(year, month - 1, day)
            }
            val dateFormat = SimpleDateFormat("yyyy년 M월 dd일", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)

            binding.tnvRecordDetail.setTitle(formattedDate)
            binding.tvRecordDate.text = formattedDate
        }
    }

    private fun setupTabsWithViewPager() {
        // ViewPager2에 어댑터 설정
        binding.viewPager.adapter = HealthRecordDetailPagerAdapter(this, tabTitles)

        // TabLayout과 ViewPager2를 연결하고 탭 디자인을 커스터마이징
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val customTabView = TabsView(binding.tabLayout.context)
            customTabView.setTabTitle(tabTitles[position])
            tab.customView = customTabView
        }.attach()

        binding.tabLayout.post {
            setTabMargins()
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                (tab?.customView as? TabsView)?.isSelected = true
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                (tab?.customView as? TabsView)?.isSelected = false
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
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
                    layoutParams.setMargins(0, 0, marginPx / 2, 0)
                }
                tabCount - 1 -> {
                    layoutParams.setMargins(marginPx / 2, 0, 0, 0)
                }
                else -> {
                    layoutParams.setMargins(marginPx / 2, 0, marginPx / 2, 0)
                }
            }
            tab.layoutParams = layoutParams
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}