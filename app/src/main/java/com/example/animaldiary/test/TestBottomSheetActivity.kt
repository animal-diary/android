package com.example.animaldiary.test

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.animaldiary.R
import com.example.animaldiary.ui.components.ActionButtonView
import com.example.animaldiary.ui.components.BottomSheetView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class TestBottomSheetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_bottom_sheet)

        setupBottomSheetTests()
    }

    private fun setupBottomSheetTests() {
        findViewById<ActionButtonView>(R.id.btn_basic_bottom_sheet).setOnClickListener {
            showBasicBottomSheet()
        }

        findViewById<ActionButtonView>(R.id.btn_no_navigation_bottom_sheet).setOnClickListener {
            showNoNavigationBottomSheet()
        }
    }

    private fun showBasicBottomSheet() {
        val dialog = BottomSheetDialog(this)

        val sheet = BottomSheetView(this).apply {
            setTitle("기본 바텀시트")
            setLeftIcon(R.drawable.ic_chevron_left) { dialog.dismiss() }
        }
        val contentView = layoutInflater.inflate(R.layout.bottom_sheet_content_basic, sheet, false)
        sheet.setContent(contentView)

        dialog.setContentView(sheet)

        // dp 변환 함수
        fun Context.dp(v: Int) = (v * resources.displayMetrics.density).toInt()
        val topGap = dp(64)
        val minHeight = dp(120)

        val windowHeight = resources.displayMetrics.heightPixels

        // Behavior 설정 - 중요한 부분!
        dialog.behavior.apply {
            isDraggable = false  // 기본 드래그 비활성화
            isHideable = true
            skipCollapsed = true  // collapsed 상태 스킵

            // expandedOffset을 topGap으로 설정 (위쪽 40dp 여백)
            expandedOffset = topGap

            // peekHeight를 큰 값으로 설정하여 초기에 많이 올라오게 함
            peekHeight = windowHeight - topGap - dp(100) // 초기 높이를 크게 설정

            // 상태를 EXPANDED로 설정
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        fun computeMaxHeight(): Int {
            val decor = dialog.window?.decorView ?: sheet
            val fullH = decor.height.takeIf { it > 0 }
                ?: resources.displayMetrics.heightPixels
            val insets = ViewCompat.getRootWindowInsets(decor)
            val ime = insets?.getInsets(WindowInsetsCompat.Type.ime()) ?: Insets.NONE
            val sys = insets?.getInsets(WindowInsetsCompat.Type.systemBars()) ?: Insets.NONE
            val reservedBottom = maxOf(ime.bottom, sys.bottom)
            return (fullH - topGap - reservedBottom).coerceAtLeast(minHeight + 1)
        }

        // 다이얼로그가 표시된 후에 설정
        dialog.setOnShowListener {
            // 초기 높이를 최대 높이의 60%로 설정
            val initialH = ((windowHeight - topGap) * 0.6f).toInt()
                .coerceIn(minHeight, computeMaxHeight())

            sheet.rootContainerView.layoutParams =
                sheet.rootContainerView.layoutParams.apply { height = initialH }
            sheet.rootContainerView.requestLayout()
        }

        // 커스텀 드래그 핸들러
        var startY = 0f
        var startH = 0
        sheet.handleContainerView.setOnTouchListener { _, e ->
            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    startY = e.rawY
                    startH = sheet.rootContainerView.height
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dy = (e.rawY - startY).toInt()
                    val newH = (startH - dy).coerceIn(minHeight, computeMaxHeight())
                    sheet.rootContainerView.layoutParams =
                        sheet.rootContainerView.layoutParams.apply { height = newH }
                    sheet.rootContainerView.requestLayout()
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    val clamped = sheet.rootContainerView.height
                        .coerceIn(minHeight, computeMaxHeight())
                    sheet.rootContainerView.layoutParams =
                        sheet.rootContainerView.layoutParams.apply { height = clamped }
                    sheet.rootContainerView.requestLayout()
                    true
                }
                else -> false
            }
        }

        dialog.show()
    }

    private fun showNoNavigationBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = BottomSheetView(this).apply {
            setNavigationVisible(false)
            setHandleVisible(false) // 핸들 바를 숨김
        }

        // 간단한 컨텐츠
        val contentView = TextView(this).apply {
            text = "네비게이션 없는 바텀시트입니다."
            setPadding(40, 40, 40, 40)
            textSize = 16f
        }
        bottomSheetView.setContent(contentView)

        bottomSheetDialog.setContentView(bottomSheetView)

        // 동일한 설정 적용
        fun Context.dp(v: Int) = (v * resources.displayMetrics.density).toInt()
        val topGap = dp(40)

        bottomSheetDialog.behavior.apply {
            expandedOffset = topGap
            peekHeight = resources.displayMetrics.heightPixels - topGap - dp(100)
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
        }

        bottomSheetDialog.show()
    }

}