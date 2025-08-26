package com.example.animaldiary.ui.toast

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
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
    private var anchorLayoutChangeListener: View.OnLayoutChangeListener? = null

    data class Item(val view: ToastMessageView, val removeRunnable: Runnable)

    // 앵커(바텀내비) 지정 및 변경
    fun setAnchor(anchor: View?) {
        val old = anchorView
        if (old === anchor) {
            updateBottomMargin()
            return
        }
        // 이전 리스너 제거
        old?.let { a ->
            anchorLayoutChangeListener?.let { a.removeOnLayoutChangeListener(it) }
            anchorLayoutChangeListener = null
        }

        anchorView = anchor
        if (anchor != null) {
            val listener = View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                updateBottomMargin()
            }
            anchorLayoutChangeListener = listener
            anchor.addOnLayoutChangeListener(listener)
        }
        updateBottomMargin()
    }

    // 컨테이너의 하단 마진 = 16dp + 앵커높이 + 시스템 인셋
    fun updateBottomMargin() {
        val base = activity.resources.getDimensionPixelSize(R.dimen.spacing_lg) // 16dp
        val extra = anchorView?.height ?: 0
        val insetBottom = getNavInset()
        val lp = container.layoutParams as FrameLayout.LayoutParams
        lp.bottomMargin = base + extra + insetBottom
        container.layoutParams = lp
    }

    private fun getNavInset(): Int {
        return ViewCompat.getRootWindowInsets(container)
            ?.getInsets(WindowInsetsCompat.Type.systemBars())
            ?.bottom ?: 0
    }

    // 토스트 표시
    fun show(message: String, style: ToastStyle, durationMs: Long = 4000L) {
        val v = ToastMessageView(activity).apply { bind(message, style) }

        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(hMargin, 0, hMargin, 0) }

        // 최상단(인덱스 0)에 추가
        container.addView(v, 0, lp)
        reflowSpacing() // 맨 위는 0, 나머지는 spacing

        v.appear()

        // 제거 예약
        val remove = Runnable { hide(v) }
        handler.postDelayed(remove, durationMs)
        visible.addFirst(Item(v, remove))

        // 3개 초과 시 가장 오래된 것부터 제거
        while (visible.size > 3) {
            val oldest = visible.removeLast()
            handler.removeCallbacks(oldest.removeRunnable)
            oldest.view.disappear {
                container.removeView(oldest.view)
                reflowSpacing()
            }
        }
    }

    // 토스트 즉시 제거
    private fun hide(v: ToastMessageView) {
        // 큐에서 제거
        val it = visible.iterator()
        while (it.hasNext()) {
            val item = it.next()
            if (item.view == v) {
                it.remove()
                break
            }
        }
        v.disappear {
            container.removeView(v)
            reflowSpacing()
        }
    }

    // 아이템 간 간격: 맨 위 0, 이후 spacing
    private fun reflowSpacing() {
        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i)
            val lp = child.layoutParams as LinearLayout.LayoutParams
            lp.topMargin = if (i == 0) 0 else spacing
            child.layoutParams = lp
        }
    }

    // 자원 정리 (액티비티 종료 시 호출)
    private fun dispose() {
        handler.removeCallbacksAndMessages(null)
        anchorView?.let { a ->
            anchorLayoutChangeListener?.let { a.removeOnLayoutChangeListener(it) }
            anchorLayoutChangeListener = null
        }
        visible.clear()
        (container.parent as? ViewGroup)?.removeView(container)
    }

    companion object {
        private val map = WeakHashMap<Activity, ToastHost>()

        // 액티비티에 호스트를 부착 (한번)
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

            val host = ToastHost(activity, container).apply {
                setAnchor(anchor)
                map[activity] = this
            }

            // 시스템 인셋 변경 시 하단 마진 재계산
            ViewCompat.setOnApplyWindowInsetsListener(container) { _, insets ->
                host.updateBottomMargin()
                insets
            }
            ViewCompat.requestApplyInsets(container)

            // 라이프사이클과 연결해 누수 방지
            if (activity is LifecycleOwner) {
                activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
                    override fun onDestroy(owner: LifecycleOwner) {
                        map.remove(activity)?.dispose()
                        activity.lifecycle.removeObserver(this)
                    }
                })
            }
            return host
        }
    }
}
