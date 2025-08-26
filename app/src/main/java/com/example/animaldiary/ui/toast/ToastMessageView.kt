package com.example.animaldiary.ui.toast

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.animaldiary.R

enum class ToastStyle { NORMAL, INFORMATION, SUCCESS, WARNING, DANGER }

class ToastMessageView(
    context: Context
) : LinearLayout(context) {

    private val root: LinearLayout
    private val text: TextView
    private val badge: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.toast_message_item, this, true)
        root = findViewById(R.id.toast_item_root)
        text = findViewById(R.id.toast_text)
        badge = findViewById(R.id.toast_badge)

        //토스트 배경 색
        val bgColor = ContextCompat.getColor(context, R.color.surface_smoke_default)
        val bg = GradientDrawable().apply {
            cornerRadius = dp(12f)
            setColor(bgColor)
        }
        root.background = bg

        // 좌우 마진
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            setMargins(parentWidthMargin, 0, parentWidthMargin, 0)
        }
        elevation = dp(2f)
        alpha = 0f
        translationY = dp(12f)
        elevation = dp(2f)
        orientation = HORIZONTAL
    }

    // 내용 및 상태 바인딩
    fun bind(message: String, style: ToastStyle) {
        text.text = message

        if (style == ToastStyle.NORMAL) {
            // NORMAL: 아이콘x
            badge.setImageDrawable(null)
            badge.background = null
            badge.visibility = GONE
            return
        }
        badge.visibility = VISIBLE
        badge.background = null

        // 아이콘 리소스
        badge.setImageResource(badgeIconRes(style))
        badge.imageTintList = ColorStateList.valueOf(
            ContextCompat.getColor(context, iconTintColorRes(style))
        )
    }

    private fun iconTintColorRes(style: ToastStyle): Int = when (style) {
        ToastStyle.INFORMATION -> R.color.bg_information_strong
        ToastStyle.SUCCESS -> R.color.bg_success_strong
        ToastStyle.WARNING -> R.color.bg_warning_strong
        ToastStyle.DANGER -> R.color.bg_error_strong
        ToastStyle.NORMAL -> android.R.color.transparent // 사용 안 함
    }

    private fun badgeIconRes(style: ToastStyle): Int = when (style) {
        ToastStyle.INFORMATION -> R.drawable.ic_alert_circle_solid
        ToastStyle.SUCCESS -> R.drawable.ic_check
        ToastStyle.WARNING -> R.drawable.ic_alert_circle_solid
        ToastStyle.DANGER -> R.drawable.ic_alert_circle_solid
        ToastStyle.NORMAL -> 0
    }

    private fun dp(v: Float) = (v * resources.displayMetrics.density)
    fun appear() {
        animate().alpha(1f).translationY(0f).setDuration(180).start()
    }

    fun disappear(onEnd: () -> Unit) {
        animate().alpha(0f).translationY(-dp(8f)).setDuration(150).withEndAction(onEnd).start()
    }
}
