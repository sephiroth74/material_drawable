package it.sephiroth.android.library.material.drawable.graphics

import android.graphics.BlendMode
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.PixelFormat
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.util.Size
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlin.math.absoluteValue

class TextDrawable(builder: Builder.() -> Unit) : Drawable() {
    private var compoundDrawables: Array<Drawable?>
    private val compoundPadding: Int
    private var background: Drawable? = null
    private val debugPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val text: String
    private val textPaint: Paint
    private var intrinsicWidth: Int = 0
    private var intrinsicHeight: Int = 0

    private val textPaddingHorizontal: Int
    private val textPaddingVertical: Int

    private val paddingStart: Int
    private val paddingTop: Int
    private val paddingBottom: Int
    private val paddingEnd: Int

    private val textBounds = Rect()
    private val textOrigin = PointF(0f, 0f)
    private var fontMetrics: FontMetricsInt? = null

    private val textAlign: Paint.Align

    init {
        with(debugPaint) {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 0.5f
        }

        val builder = Builder().apply(builder)
        text = builder.text
        textPaint = Paint(builder.textPaint)

        textPaddingHorizontal = builder.textPadding.width
        textPaddingVertical = builder.textPadding.height

        paddingStart = builder.backgroundPadding.left
        paddingEnd = builder.backgroundPadding.right
        paddingTop = builder.backgroundPadding.top
        paddingBottom = builder.backgroundPadding.bottom

        background = builder.background
        compoundDrawables = builder.compoundDrawables
        compoundPadding = builder.compoundPadding

        textAlign = builder.textAlign

        onTextSizeChanged()
        bounds = Rect(0, 0, intrinsicWidth, intrinsicHeight)
    }

    private fun applyNewTextSize(maxHeight: Int) {
        if (DEBUG_LOG) {
            Log.d(TAG, "applyNewTextSize(maxHeight=$maxHeight)")
        }
        var size = Size(intrinsicWidth, intrinsicHeight)

        if (DEBUG_LOG) {
            Log.v(TAG, "current=${size.height}, maxHeight=$maxHeight")
        }

        var maxValue = 0f
        var minValue = 0f

        if (size.height > maxHeight) {
            maxValue = textPaint.textSize
        } else {
            minValue = textPaint.textSize
        }

        while ((size.height - maxHeight).absoluteValue > 1) {
            if (DEBUG_LOG) {
                Log.d(TAG, "while(size.height=${size.height}, maxHeight=${maxHeight})")
                Log.d(TAG, "minValue=$minValue, maxValue=$maxValue")
            }

            if (size.height > maxHeight) {
                if (maxValue != 0f && minValue != 0f) {
                    textPaint.textSize = maxValue - (maxValue - minValue) / 2
                } else {
                    textPaint.textSize /= 2
                }
            } else if (size.height < maxHeight) {
                if (maxValue != 0f && minValue != 0f) {
                    textPaint.textSize = minValue + (maxValue - minValue) / 2
                } else {
                    textPaint.textSize *= 2
                }
            }

            fontMetrics = null

            size = Size(
                intrinsicWidth,
                getLineHeight() + (textPaddingVertical + paddingTop + paddingBottom)
            )

            if (size.height > maxHeight) {
                maxValue = textPaint.textSize
            } else {
                minValue = textPaint.textSize
            }
        }
    }

    private fun measureText(): Size {
        val w = (textPaint.measureText(text, 0, text.length) + .5).toInt()
        val h = getLineHeight()

        if (DEBUG_LOG) {
            Log.d(TAG, "lineHeight: $h")
        }

        return Size(
            w + totalHorizontalPadding(),
            h + totalVerticalPadding()
        )
    }

    private fun getFontMetricsInt(): FontMetricsInt {
        return fontMetrics ?: run {
            fontMetrics = textPaint.fontMetricsInt
            fontMetrics!!
        }
    }

    private fun getLineHeight(): Int {
        val fontMetrics = getFontMetricsInt()
        return fontMetrics.top.absoluteValue + fontMetrics.bottom
    }

    private fun totalVerticalPadding(): Int {
        return textPaddingVertical + paddingTop + paddingBottom
    }

    private fun totalHorizontalPadding(): Int {
        var total = textPaddingHorizontal + paddingStart + paddingEnd
        compoundDrawables.firstOrNull()?.bounds?.let { total += it.width() + compoundPadding }
        compoundDrawables.lastOrNull()?.bounds?.let { total += it.width() + compoundPadding }
        return total
    }

    private fun onTextSizeChanged() {
        val size = measureText()

        intrinsicWidth = size.width
        intrinsicHeight = size.height

        if (DEBUG_LOG) {
            Log.v(TAG, "textSize=${textPaint.textSize}, intrinsicWidth=$intrinsicWidth, intrinsicHeight=$intrinsicHeight")
        }
    }

    override fun getIntrinsicWidth(): Int {
        return intrinsicWidth
    }

    override fun getIntrinsicHeight(): Int {
        return intrinsicHeight
    }

    override fun setAlpha(alpha: Int) {
        textPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        textPaint.colorFilter = colorFilter
        compoundDrawables.firstOrNull()?.let { it.colorFilter = colorFilter }
        compoundDrawables.lastOrNull()?.let { it.colorFilter = colorFilter }
    }

    fun setBackgroundColorFilter(colorFilter: ColorFilter?) {
        background?.colorFilter = colorFilter
        invalidateSelf()
    }

    fun getBackgroundColorFilter(): ColorFilter? {
        return background?.colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setBounds(bounds: Rect) {
        super.setBounds(bounds)
    }

    override fun onBoundsChange(bounds: Rect) {
        if (DEBUG_LOG) {
            Log.i(TAG, "onBoundsChange($bounds, intrinsicHeight=$intrinsicHeight)")
        }

        if (bounds.height() < intrinsicHeight) {
            applyNewTextSize(bounds.height() - totalVerticalPadding())
            onTextSizeChanged()
            invalidateSelf()
        }

        textPaint.getTextBounds(text, 0, text.length, textBounds)
        val fontMetrics = getFontMetricsInt()

        if (DEBUG_LOG) {
            Log.v(TAG, "textBounds.height=${textBounds.height()}")
            Log.v(TAG, "leading=${fontMetrics.leading}, top=${fontMetrics.top}, bottom=${fontMetrics.bottom}, ascent=${fontMetrics.ascent}, descent=${fontMetrics.descent}")
        }

        background?.let { bg ->
            val boundsCopy = Rect(bounds)
            boundsCopy.left += paddingStart
            boundsCopy.right -= paddingEnd
            boundsCopy.top += paddingTop
            boundsCopy.bottom -= paddingBottom
            bg.bounds = boundsCopy
        }

        var totalWidth: Float = textBounds.width().toFloat()
        compoundDrawables.firstOrNull()?.let {
            totalWidth += it.bounds.width() + compoundPadding
        }
        compoundDrawables.lastOrNull()?.let {
            totalWidth += it.bounds.width() + compoundPadding
        }

        if (textAlign == Paint.Align.CENTER) {
            textOrigin.x = bounds.centerX().toFloat() - totalWidth / 2
        } else if (textAlign == Paint.Align.RIGHT) {
            textOrigin.x = (bounds.right - textBounds.width() - paddingEnd - (textPaddingHorizontal / 2)).toFloat()
        } else {
            textOrigin.x = (paddingStart + (textPaddingHorizontal / 2)).toFloat()
        }

        if (DEBUG_LOG) {
            Log.d(TAG, "textBounds: $textBounds")
            Log.d(TAG, "textAlign: $textAlign")
        }

        textOrigin.y = ((bounds.height() / 2 + (fontMetrics.top.absoluteValue - fontMetrics.bottom) / 2) + paddingTop / 2 - paddingBottom / 2).toFloat()

        compoundDrawables.firstOrNull()?.let { first ->
            val b = first.bounds
            if (textAlign == Paint.Align.CENTER) {
                b.offsetTo(textOrigin.x.toInt(), bounds.centerY() - (b.height() / 2))
                textOrigin.x += b.width() + compoundPadding
            } else if (textAlign == Paint.Align.LEFT) {
                b.offsetTo(paddingStart + textPaddingHorizontal / 2, bounds.centerY() - (b.height() / 2))
                textOrigin.x += b.width() + compoundPadding
            } else {
                b.offsetTo((bounds.right - totalWidth - paddingEnd - textPaddingHorizontal / 2).toInt(), bounds.centerY() - (b.height() / 2))
            }
        }

        compoundDrawables.lastOrNull()?.let { last ->
            val b = last.bounds
            if (textAlign == Paint.Align.CENTER) {
                b.offsetTo((bounds.centerX() + totalWidth / 2 - b.width()).toInt(), bounds.centerY() - b.height() / 2)
            } else if (textAlign == Paint.Align.LEFT) {
                b.offsetTo((textOrigin.x + textBounds.width() + compoundPadding).toInt(), bounds.centerY() - (b.height() / 2))
            } else {
                b.offsetTo(bounds.width() - paddingEnd - textPaddingHorizontal / 2 - b.width(), bounds.centerY() - (b.height() / 2))
                textOrigin.x -= (b.width() + compoundPadding)
            }
        }

        super.onBoundsChange(bounds)
    }


    override fun draw(canvas: Canvas) {
        val bounds = this.bounds

        if (DEBUG_LOG) {
            Log.v(TAG, "draw(bounds=$bounds)")
            with(debugPaint) {
                color = Color.RED
                alpha = 127
                style = Paint.Style.STROKE
            }
            canvas.drawRect(bounds, debugPaint)
        }

        background?.draw(canvas)

        compoundDrawables.firstOrNull()?.let { first ->
            first.draw(canvas)
        }

        compoundDrawables.lastOrNull()?.let { last ->
            last.draw(canvas)
        }

        canvas.drawText(text, textOrigin.x, textOrigin.y, textPaint)

        if (DEBUG_LOG) {
            val fontMetrics = getFontMetricsInt()
            canvas.drawRect(textOrigin.x, textOrigin.y, (textOrigin.x + textBounds.width()), textOrigin.y + fontMetrics.ascent, debugPaint)
            canvas.drawLine(bounds.left.toFloat(), bounds.centerY().toFloat(), bounds.right.toFloat(), bounds.centerY().toFloat(), debugPaint)
            canvas.drawLine(bounds.centerX().toFloat(), bounds.top.toFloat(), bounds.centerX().toFloat(), bounds.bottom.toFloat(), debugPaint)

            with(debugPaint) {
                color = Color.BLUE
                alpha = 51
                style = Paint.Style.FILL
            }

            canvas.drawRect(Rect(paddingStart, bounds.centerY() - textBounds.height() / 2, textPaddingHorizontal / 2, bounds.centerY() + textBounds.height() / 2), debugPaint)
            canvas.drawRect(
                Rect(
                    bounds.right - paddingEnd,
                    bounds.centerY() - textBounds.height() / 2,
                    bounds.right - paddingEnd - textPaddingHorizontal / 2,
                    bounds.centerY() + textBounds.height() / 2
                ), debugPaint
            )
        }
    }


    companion object {
        const val TAG = "TextDrawable"
        var DEBUG_LOG: Boolean = false

        class Builder {
            internal var textPadding: Size = Size(0, 0)
            internal val backgroundPadding: Rect = Rect()
            internal val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            internal var text: String = ""
            internal var background: Drawable? = null
            internal var compoundDrawables: Array<Drawable?> = arrayOf(null, null)
            internal var compoundPadding: Int = 0
            internal var textAlign: Paint.Align = Paint.Align.CENTER

            init {
                textPaint.hinting = Paint.HINTING_ON
                textPaint.isElegantTextHeight = true
            }

            fun fromTextView(textView: TextView) = apply {
                textSize(textView.textSize)
                typeface(textView.typeface)
                textColor(textView.paint.color)
                isFakeBoldText(textView.paint.isFakeBoldText)
                isDitherText(textView.paint.isDither)
                isAntiAliasText(textView.paint.isAntiAlias)
                isElegantTextHeight(textView.paint.isElegantTextHeight)
                isLinearText(textView.paint.isLinearText)
                isSubpixelText(textView.paint.isSubpixelText)
                isUnderlineText(textView.paint.isUnderlineText)
                textAlpha(textView.paint.alpha)
                textAlign(textView.paint.textAlign)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    textBlendMode(textView.paint.blendMode)
                }

                textFlags(textView.paint.flags)
            }

            fun text(value: String) = apply {
                text = value
            }

            fun textSize(value: Float) = apply {
                textPaint.textSize = value
            }

            fun textColor(color: Long) = apply {
                textPaint.color = color.toInt()
            }

            fun textColor(color: Int) = apply {
                textPaint.color = color
            }

            fun textPadding(horizontal: Int, vertical: Int) = apply {
                textPadding = Size(horizontal, vertical)
            }

            fun padding(left: Int, top: Int, right: Int, bottom: Int) = apply {
                backgroundPadding.set(left, top, right, bottom)
            }

            fun typeface(value: Typeface) = apply {
                textPaint.typeface = value
            }

            fun isFakeBoldText(value: Boolean) = apply {
                textPaint.isFakeBoldText = value
            }

            fun textAlpha(value: Int) = apply { textPaint.alpha = value }

            @RequiresApi(Build.VERSION_CODES.Q)
            fun textBlendMode(blendMode: BlendMode?) = apply { textPaint.blendMode = blendMode }

            fun textFlags(flags: Int) = apply { textPaint.flags = flags }

            fun isDitherText(value: Boolean) = apply { textPaint.isDither = value }

            fun isAntiAliasText(value: Boolean) = apply { textPaint.isAntiAlias = value }

            fun isElegantTextHeight(value: Boolean) = apply { textPaint.isElegantTextHeight = value }

            fun isLinearText(value: Boolean) = apply { textPaint.isLinearText = value }

            fun isSubpixelText(value: Boolean) = apply { textPaint.isSubpixelText = value }

            fun isUnderlineText(value: Boolean) = apply { textPaint.isUnderlineText }

            fun background(value: Drawable) = apply {
                background = value
            }

            fun compoundDrawables(left: Drawable?, right: Drawable?) = apply {
                left?.let { check(!it.bounds.isEmpty) { "left drawable must have bounds set" } }
                right?.let { check(!it.bounds.isEmpty) { "right drawable must have bounds set" } }
                compoundDrawables[0] = left
                compoundDrawables[1] = right
            }

            fun compoundPadding(value: Int) = apply {
                compoundPadding = value
            }

            fun textAlign(value: Paint.Align) = apply {
                textAlign = value
            }
        }
    }
}
