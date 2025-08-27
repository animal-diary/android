package com.example.animaldiary

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.animaldiary.ui.manage.ManageFragment
import com.example.animaldiary.ui.setting.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private fun switchFragment(f: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, f)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // 최초 진입 시 기본 화면 (관리 탭)
        if (savedInstanceState == null) {
            switchFragment(ManageFragment())
            bottomNav.selectedItemId = R.id.navigation_manage
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_health_note -> { // 건강 노트
                    switchFragment(SettingFragment())
                    true
                }
                R.id.navigation_manage -> { // 관리하기
                    switchFragment(ManageFragment())
                    true
                }
                R.id.navigation_setting -> { // 세팅
                    switchFragment(SettingFragment())
                    true
                }
                else -> false
            }
        }
    }
}
