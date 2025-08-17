package com.example.animaldiary.ui.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.animaldiary.R

enum class TagType { GRAY, RED, GREEN, BLUE, YELLOW, PINK, WHITE, ORANGE }

class TagView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val container: LinearLayout
    private val plusIcon: ImageView
    private val textView: TextView

    private var type: TagType = TagType.GRAY
    private var showPlus: Boolean = true

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.component_tag, this, true)
        container = findViewById(R.id.tag_container)
        plusIcon = findViewById(R.id.tag_plus)
        textView = findViewById(R.id.tag_text)

        context.obtainStyledAttributes(attrs, R.styleable.TagView).apply {
            val t = getInt(R.styleable.TagView_tag_type, 0)
            type = TagType.values()[t.coerceIn(0, TagType.values().lastIndex)]
            val txt = getString(R.styleable.TagView_tag_text) ?: "Text"
            showPlus = getBoolean(R.styleable.TagView_tag_showPlus, true)
            recycle()
            setText(txt)
        }

        setShowPlus(showPlus)
        applyStyle(type)
    }

    fun setText(text: String) {
        textView.text = text
    }

    fun setType(tagType: TagType) {
        type = tagType
        applyStyle(type)
    }

    fun setShowPlus(show: Boolean) {
        showPlus = show
        plusIcon.visibility = if (show) VISIBLE else GONE
    }

    private fun applyStyle(t: TagType) {
        val (bg, /*stroke*/ _, fg) = when (t) {
            TagType.GRAY   -> Triple(R.color.bg_neutral_primary_default, 0, R.color.fg_neutral_secondary)
            TagType.RED    -> Triple(R.color.bg_error_normal,               0, R.color.fg_accent_red_normal)
            TagType.GREEN  -> Triple(R.color.bg_success_normal,             0, R.color.fg_accent_green)
            TagType.BLUE   -> Triple(R.color.bg_information_normal,         0, R.color.fg_accent_blue_normal)
            TagType.YELLOW -> Triple(R.color.bg_warning_normal,             0, R.color.fg_accent_yellow)
            TagType.PINK   -> Triple(R.color.bg_pink_normal,                0, R.color.fg_accent_pink_normal)
            TagType.WHITE  -> Triple(R.color.bg_base,                       0, R.color.fg_neutral_tertiary)
            TagType.ORANGE -> Triple(R.color.bg_brand_secondary_default,    0, R.color.fg_brand_default)
        }

        val bgDrawable: GradientDrawable =
            (container.background as? GradientDrawable)
                ?: (ContextCompat.getDrawable(context, R.drawable.bg_tag) as GradientDrawable)
                    .apply { mutate() }

        container.background = bgDrawable
        bgDrawable.setColor(ContextCompat.getColor(context, bg))
//        bgDrawable.setStroke(
//            resources.getDimensionPixelSize(R.dimen.border_width_weak),
//            ContextCompat.getColor(context, stroke)
//        )

        val fgColor = ContextCompat.getColor(context, fg)
        textView.setTextColor(fgColor)
        plusIcon.imageTintList = ColorStateList.valueOf(fgColor)
    }
}
