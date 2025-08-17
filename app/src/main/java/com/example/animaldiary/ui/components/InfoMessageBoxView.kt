package com.example.animaldiary.ui.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.animaldiary.R

enum class InfoAlign { CENTER, LEFT }
enum class InfoStatus { NORMAL, INFORMATION, SUCCESS, WARNING, DANGER }

class InfoMessageBoxView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val container: LinearLayout
    private val desc: TextView

    private var align: InfoAlign = InfoAlign.CENTER
    private var status: InfoStatus = InfoStatus.NORMAL

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.component_info_message_box, this, true)
        container = findViewById(R.id.imb_container)
        desc = findViewById(R.id.imb_text)

        context.obtainStyledAttributes(attrs, R.styleable.InfoMessageBoxView).apply {
            desc.text = getString(R.styleable.InfoMessageBoxView_imb_text)
                ?: "This is the first line of text.\nThis is the second line."
            align = if (getInt(R.styleable.InfoMessageBoxView_imb_align, 0) == 0)
                InfoAlign.CENTER else InfoAlign.LEFT
            status = when (getInt(R.styleable.InfoMessageBoxView_imb_status, 0)) {
                1 -> InfoStatus.INFORMATION
                2 -> InfoStatus.SUCCESS
                3 -> InfoStatus.WARNING
                4 -> InfoStatus.DANGER
                else -> InfoStatus.NORMAL
            }
            val max = getInt(R.styleable.InfoMessageBoxView_imb_maxLines, 0)
            if (max > 0) desc.maxLines = max
            recycle()
        }

        setAlign(align)
        setStatus(status)
    }

    fun setText(text: String) { desc.text = text }

    fun setAlign(a: InfoAlign) {
        align = a
        when (a) {
            InfoAlign.CENTER -> {
                container.gravity = android.view.Gravity.CENTER
                desc.textAlignment = TEXT_ALIGNMENT_CENTER
                desc.gravity = android.view.Gravity.CENTER
            }
            InfoAlign.LEFT -> {
                container.gravity = android.view.Gravity.START
                desc.textAlignment = TEXT_ALIGNMENT_VIEW_START
                desc.gravity = android.view.Gravity.START
            }
        }
    }

    fun setStatus(s: InfoStatus) {
        status = s
        val (bg, fg) = when (s) {
            InfoStatus.NORMAL      -> R.color.bg_neutral_secondary_default to R.color.fg_neutral_primary
            InfoStatus.INFORMATION -> R.color.bg_information_normal         to R.color.fg_accent_blue_normal
            InfoStatus.SUCCESS     -> R.color.bg_success_normal             to R.color.fg_accent_green
            InfoStatus.WARNING     -> R.color.bg_warning_normal             to R.color.fg_accent_yellow
            InfoStatus.DANGER      -> R.color.bg_error_normal               to R.color.fg_accent_red_normal
        }

        val gd: GradientDrawable =
            (container.background as? GradientDrawable)
                ?: (ContextCompat.getDrawable(context, R.drawable.bg_info_message_box) as GradientDrawable)
                    .apply { mutate() }
        gd.setColor(ContextCompat.getColor(context, bg))
        container.background = gd

        val fgColor = ContextCompat.getColor(context, fg)
        desc.setTextColor(fgColor)
        desc.compoundDrawableTintList = ColorStateList.valueOf(fgColor)
    }
}
