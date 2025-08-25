package com.example.animaldiary.ui.toast

import android.app.Activity
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import com.example.animaldiary.R
import java.util.ArrayDeque
import java.util.WeakHashMap
import kotlin.math.max

class ToastHost private constructor(
    private val activity: Activity,
    private val container: LinearLayout
) {
    private val handler = Handler(Looper.getMainLooper())
    private val visible = ArrayDeque<Item>() // 화면에 보이는 알림 최대 3개
    private val spacing = activity.resources.getDimensionPixelSize(R.dimen.spacing_sm)
    private val hMargin = activity.resources.getDimensionPixelSize(R.dimen.padding_lg)
    private var anchorView: View? = null

    data class Item(val view: ToastMessageView, val removeRunnable: Runnable)

    fun setAnchor(anchor: View?) {
        anchorView = anchor
        updateBottomMargin()
        anchor?.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> updateBottomMargin() }
    }

    private fun updateBottomMargin() {
        val insetBottom = getNavInset()
        val base = activity.resources.getDimensionPixelSize(R.dimen.spacing_lg) // BottomNav 기준 16
        val extra = anchorView?.height ?: 0
        container.updateLayoutParams<FrameLayout.LayoutParams> {
            bottomMargin = base + extra + insetBottom
        }
    }

    private fun getNavInset(): Int {
        var inset = 0
        ViewCompat.setOnApplyWindowInsetsListener(container) { v, insets ->
            val b = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars()).bottom
            inset = b
            insets
        }
        return inset
    }

    fun show(message: String, style: ToastStyle, durationMs: Long = 4000L) {
        // 새 뷰 생성
        val v = ToastMessageView(activity, hMargin).apply { bind(message, style) }

        // 컨테이너 맨 위(=index 0)에 추가
        container.addView(v, 0)
        // 바로 위 아이템 간격
        if (container.childCount > 1) {
            val prev = container.getChildAt(1)
            (prev.layoutParams as LinearLayout.LayoutParams).topMargin = spacing
        }
        // 새 뷰는 마진 0
        (v.layoutParams as LinearLayout.LayoutParams).topMargin = 0

        v.appear()

        // 제거 예약
        val remove = Runnable { hide(v) }
        handler.postDelayed(remove, durationMs)
        visible.addFirst(Item(v, remove))

        // 3개 초과 시 가장 오래된 것부터 제거
        while (visible.size > 3) {
            val oldest = visible.pollLast() ?: break
            handler.removeCallbacks(oldest.removeRunnable)
            oldest.view.disappear {
                container.removeView(oldest.view)
            }
        }
    }

    private fun hide(v: ToastMessageView) {
        // 큐에서 제거
        val itr = visible.iterator()
        while (itr.hasNext()) {
            val it = itr.next()
            if (it.view == v) {
                itr.remove()
                break
            }
        }
        v.disappear {
            container.removeView(v)
        }
    }

    companion object {
        private val map = WeakHashMap<Activity, ToastHost>()

        fun attach(activity: Activity, anchor: View? = null): ToastHost {
            map[activity]?.let {
                it.setAnchor(anchor)
                return it
            }
            val root = activity.findViewById<ViewGroup>(android.R.id.content)
            val container = LinearLayout(activity).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM
                )
            }
            (root as ViewGroup).addView(container)
            return ToastHost(activity, container).apply {
                setAnchor(anchor)
                map[activity] = this
            }
        }
    }
}
