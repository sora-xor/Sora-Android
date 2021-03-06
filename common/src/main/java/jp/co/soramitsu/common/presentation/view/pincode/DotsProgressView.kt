package jp.co.soramitsu.common.presentation.view.pincode

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import jp.co.soramitsu.common.R

class DotsProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    companion object {
        const val MAX_PROGRESS = 4
    }

    private var circles: Array<View?>

    private var emptyDrawable: Drawable
    private var filledDrawable: Drawable

    private val completeListener: () -> Unit = {}

    init {
        val itemSize = context.resources.getDimensionPixelSize(R.dimen.uikit_dot_progress_view_dot_size_default)
        val itemMargin = context.resources.getDimensionPixelOffset(R.dimen.uikit_dot_progress_view_dot_margin_default)

        emptyDrawable = ContextCompat.getDrawable(context, R.drawable.uikit_pincode_indicator_empty)!!
        filledDrawable = ContextCompat.getDrawable(context, R.drawable.uikit_pincode_indicator_filled)!!

        circles = arrayOfNulls(MAX_PROGRESS)

        for (i in 0 until MAX_PROGRESS) {
            val circle = View(context)
            val params = LayoutParams(itemSize, itemSize)
            params.setMargins(itemMargin, 0, itemMargin, 0)
            circle.layoutParams = params
            addView(circle)
            circles[i] = circle
        }

        setProgress(0)
    }

    fun setProgress(currentProgress: Int) {
        for (circle in circles) {
            circle?.background = emptyDrawable
        }
        if (currentProgress == 0) {
            return
        }
        for (i in 0 until currentProgress) {
            circles[i]?.background = filledDrawable
        }
        if (currentProgress >= MAX_PROGRESS) {
            completeListener()
        }
    }
}
