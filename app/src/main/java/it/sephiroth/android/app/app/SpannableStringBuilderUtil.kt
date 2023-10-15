package it.sephiroth.android.app.app

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import kotlin.math.max

/**
 */
object SpannableStringBuilderUtil {

    // ----------------------------------------------------
    // region Private Methods

    private const val DRAWABLE_TEXT_SIZE = 15

    fun appendDrawable(context: Context, builder: SpannableStringBuilder, drawable: Drawable) {
        val width = drawable.bounds.width()
        val size = max(width.coerceAtLeast(1) / DRAWABLE_TEXT_SIZE, 2)
        for (i in 0 until size) {
            builder.append("W")
        }
        val start = builder.length - size
        val end = start + size
        val span1 = VerticalCenteredImageSpan(context, drawable)
        builder.setSpan(span1, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    // endregion

    // ----------------------------------------------------
    // region Public Methods

    fun appendDrawable(context: Context, builder: SpannableStringBuilder, @DrawableRes resId: Int): Boolean {
        ThemeUtil.getDrawable(context, resId)?.let { drawable ->
            appendDrawable(context, builder, drawable)
            return true
        }
        return false
    }

    fun appendThemeIcon(context: Context, builder: SpannableStringBuilder, @AttrRes attrId: Int): Boolean {
        ThemeUtil.getThemeDrawable(context, attrId)?.let { drawable ->
            appendDrawable(context, builder, drawable)
            return true
        }
        return false
    }

    // endregion
}
