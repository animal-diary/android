package com.example.animaldiary.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.animaldiary.R

class EmptyStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val container: LinearLayout
    private val illust: View
    private val titleView: TextView
    private val descView: TextView
    private val actionBtn: Button

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.component_empty_state, this, true)
        container = findViewById(R.id.es_container)
        illust = findViewById(R.id.es_illust)
        titleView = findViewById(R.id.es_title)
        descView = findViewById(R.id.es_description)
        actionBtn = findViewById(R.id.es_button)

        context.obtainStyledAttributes(attrs, R.styleable.EmptyStateView).apply {
            getString(R.styleable.EmptyStateView_es_title)?.let { setTitle(it) }
            getString(R.styleable.EmptyStateView_es_description)?.let { setDescription(it) }
            getString(R.styleable.EmptyStateView_es_buttonText)?.let { setButtonText(it) }

            // show/hide
            setShowIllust(getBoolean(R.styleable.EmptyStateView_es_showIllust, true))
            setShowDescription(getBoolean(R.styleable.EmptyStateView_es_showDescription, true))
            setShowButton(getBoolean(R.styleable.EmptyStateView_es_showButton, true))
            recycle()
        }
    }

    fun setTitle(text: String) { titleView.text = text }
    fun setDescription(text: String) { descView.text = text }
    fun setButtonText(text: String) { actionBtn.text = text }

    fun setShowIllust(show: Boolean) { illust.visibility = if (show) View.VISIBLE else View.GONE }
    fun setShowDescription(show: Boolean) { descView.visibility = if (show) View.VISIBLE else View.GONE }
    fun setShowButton(show: Boolean) { actionBtn.visibility = if (show) View.VISIBLE else View.GONE }

    fun setOnActionClickListener(listener: (View) -> Unit) { actionBtn.setOnClickListener(listener) }
}
