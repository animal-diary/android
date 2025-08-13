package com.example.animaldiary.ui.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Checkable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.animaldiary.R

class FilterChipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Checkable {

    private val container: LinearLayout
    private val label: TextView
    private val icon: ImageView

    private var checked = false
    private var showIcon = false
    private var onCheckedChange: ((Boolean) -> Unit)? = null

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.component_filter_chip, this, true)
        container = findViewById(R.id.fc_container)
        label = findViewById(R.id.fc_text)
        icon = findViewById(R.id.fc_icon)

        // xml 속성 읽기
        context.obtainStyledAttributes(attrs, R.styleable.FilterChipView).apply {
            label.text = getString(R.styleable.FilterChipView_fcv_text) ?: "option"
            checked = getBoolean(R.styleable.FilterChipView_fcv_selected, false)
            showIcon = getBoolean(R.styleable.FilterChipView_fcv_showIcon, false)
            recycle()
        }

        setIconVisible(showIcon)
        applyState()

        // 토글
        setOnClickListener {
            if (isEnabled) toggle()
        }
    }

    fun setText(text: String) { label.text = text }
    fun setIconVisible(visible: Boolean) {
        showIcon = visible
        icon.visibility = if (visible) VISIBLE else GONE
    }
    fun setOnCheckedChangeListener(listener: (Boolean) -> Unit) { onCheckedChange = listener }

    // Checkable 구현
    override fun isChecked() = checked
    override fun toggle() = setChecked(!checked)
    override fun setChecked(checked: Boolean) {
        if (this.checked == checked) return
        this.checked = checked
        applyState()
        onCheckedChange?.invoke(this.checked)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        container.isEnabled = enabled
        isClickable = enabled
        isFocusable = enabled
        applyState()
    }

    private fun applyState() {
        val (bg, stroke, fg) = when {
            !isEnabled -> Triple(
                R.color.bg_disabled,
                R.color.outline_disabled,
                R.color.fg_disabled
            )
            checked -> Triple(
                R.color.bg_selected_pressed,
                R.color.outline_selected,
                R.color.fg_brand_pressed
            )
            else -> Triple(
                R.color.bg_neutral_secondary_default,
                R.color.outline_neutral,
                R.color.fg_neutral_tertiary
            )
        }

        val gd: GradientDrawable =
            (container.background as? GradientDrawable)
                ?: (ContextCompat.getDrawable(context, R.drawable.bg_filter_chip) as GradientDrawable).apply { mutate() }

        gd.setColor(ContextCompat.getColor(context, bg))
        gd.setStroke(resources.getDimensionPixelSize(R.dimen.border_width_weak),
            ContextCompat.getColor(context, stroke))
        container.background = gd

        val fgColor = ContextCompat.getColor(context, fg)
        label.setTextColor(fgColor)
        icon.imageTintList = ColorStateList.valueOf(fgColor)
        alpha = if (isEnabled) 1f else 0.6f
    }
}
