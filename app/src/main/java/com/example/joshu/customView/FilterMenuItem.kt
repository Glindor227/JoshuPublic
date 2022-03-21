package com.example.joshu.customView

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.joshu.R
import com.example.joshu.utils.ViewsUtil
import com.example.joshu.utils.ViewsUtil.dpToPx
import com.example.joshu.viewUtils.StatusUiUtils
import com.google.android.flexbox.FlexboxLayout

class FilterMenuItem(context: Context) : CardView(context) {

    private var listener: OnClickListener? = null
    private var callback: OnClickCallback? = null
    var index: Int = 0
    var isChecked = false
    private var title: String? = null
    private var score: Int? = 0
    private var titleView: TextView
    private var iconView: ImageView
    private var scoreView: TextView
    private val linearLayout = LinearLayout(context)

    init {
        val screenWidth = ViewsUtil.screenWidthPx()
        val cardViewParams = ViewGroup.LayoutParams(0, LayoutParams.MATCH_PARENT)
        val linearLayoutParams =
            LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(58), 0.0F)
        linearLayout.layoutParams = linearLayoutParams
        val flexboxLayoutParams = FlexboxLayout.LayoutParams(cardViewParams)
        flexboxLayoutParams.minWidth = (screenWidth / 2) - dpToPx(32)
        flexboxLayoutParams.setMargins(0, dpToPx(8), 0, dpToPx(8))
        super.setLayoutParams(flexboxLayoutParams)
        linearLayout.orientation = HORIZONTAL
        super.setCardElevation(dpToPx(8).toFloat())
        super.setRadius(dpToPx(8).toFloat())
        val attrs = intArrayOf(R.attr.selectableItemBackground)
        val typedArray = context.obtainStyledAttributes(attrs)
        val selectableItemBackground = typedArray.getResourceId(0, 0)
        typedArray.recycle()
        super.setForeground(context.getDrawable(selectableItemBackground))
        super.setClickable(true)

        val iconLayoutParams =
            LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0F)
        iconLayoutParams.gravity = Gravity.CENTER_VERTICAL
        iconView = ImageView(context)
        iconView.setPadding(dpToPx(8), 0, 0, 0)
        iconView.layoutParams = iconLayoutParams

        val titleLayoutParams =
            LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 4.0F)
        titleView = TextView(context)
        titleView.setPadding(dpToPx(8), 0, 0, 0)
        titleView.gravity = Gravity.CENTER_VERTICAL
        titleView.layoutParams = titleLayoutParams
        titleView.setTextColor(resources.getColor(R.color.colorPrimary))

        val scoreLayoutParams =
            LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 0.0F)

        scoreView = TextView(context)
        scoreView.setPadding(0, 0, dpToPx(16), 0)
        scoreView.gravity = Gravity.CENTER_VERTICAL
        scoreView.layoutParams = scoreLayoutParams
        scoreView.setTextColor(resources.getColor(R.color.colorPrimary))

        linearLayout.addView(iconView)
        linearLayout.addView(titleView)
        linearLayout.addView(scoreView)
        super.addView(linearLayout)
        super.setClickable(true)

        super.setOnClickListener {
            callback?.onClickCallback(it)
            listener?.onClick(it)

        }
    }

    fun setOnItemClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    fun setTitle(id: Int) {
        this.titleView.text = resources.getText(id)
        this.title = resources.getText(id).toString()
    }

    fun setTitle(text: String) {
        this.titleView.text = text
        this.title = text
    }

    fun getTitle(): String? {
        return this.title
    }

    fun setIcon(id: Int) {
        this.iconView.setImageResource(id)
    }

    fun setScore(score: Int) {
        this.scoreView.text = formatScore(score)
    }

    fun setOnClickCallback(callback: OnClickCallback) {
        this.callback = callback
    }

    fun changeCheckedStatus(status: Boolean) {
        isChecked = status
        iconView.setImageDrawable(
            resources.getDrawable(StatusUiUtils.getActivityIcon(index, isChecked))
        )
        val textColor = resources.getColor(StatusUiUtils.getTextColor(isChecked))
        scoreView.setTextColor(textColor)
        titleView.setTextColor(textColor)
        linearLayout.background = context.getDrawable(StatusUiUtils.getBackground(isChecked))
    }

    fun changeCheckedStatus() {
        changeCheckedStatus(isChecked.not())
    }

    interface OnClickListener {
        fun onClick(view: View)
    }

    interface OnClickCallback {
        fun onClickCallback(view: View)
    }
}

fun formatScore(score: Int): String {
    val stringBuilder = StringBuilder()

    if ((score / 10) > 0) {
        stringBuilder.append(score)
    } else if (score > 99) {
        stringBuilder.append("99+")
    } else {
        stringBuilder.append(0).append(score)
    }

    return stringBuilder.toString()
}