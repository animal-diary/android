package com.example.animaldiary.test.tabs

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.animaldiary.R
import com.example.animaldiary.ui.components.TabsView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TestTabsActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private val tabTitles = listOf("전체", "구토", "호흡수", "체중", "피부", "구토", "식욕", "특이사항", "체중", "물섭취량")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_tabs)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)

        // ViewPager2에 어댑터 설정
        viewPager.adapter = TestPagerAdapter(this, tabTitles)

        // TabLayout과 ViewPager2를 연결하고 탭 디자인을 커스터마이징
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // 각 탭에 커스텀 뷰(TabsView) 설정
            val customTabView = TabsView(tabLayout.context)
            customTabView.setTabTitle(tabTitles[position])
            tab.customView = customTabView
        }.attach()

        tabLayout.post {
            setTabMargins()
        }

        // 탭 선택 리스너를 추가하여 커스텀 뷰의 상태를 제어
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
        val tabCount = tabLayout.tabCount
        val slidingTabStrip = tabLayout.getChildAt(0) as ViewGroup
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


}

