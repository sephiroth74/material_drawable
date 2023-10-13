package it.sephiroth.android.library.material.drawable.graphics

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.Size
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

    init {
        with(debugPaint) {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 1f
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

        onTextSizeChanged()
    }

    private fun applyNewTextSize(maxHeight: Int) {
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

            size = Size(
                intrinsicWidth,
                textPaint.getFontMetricsInt(null) + (textPaddingVertical + paddingTop + paddingBottom)
            )

            if (size.height > maxHeight) {
                maxValue = textPaint.textSize
            } else {
                minValue = textPaint.textSize
            }

            Thread.sleep(500)
        }
    }

    private fun measureText(): Size {
        val w = (textPaint.measureText(text, 0, text.length) + .5).toInt()
        val h = textPaint.getFontMetricsInt(null)
        return Size(
            w + totalHorizontalPadding(),
            h + totalVerticalPadding()
        )
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

        background?.let { bg ->
            val boundsCopy = Rect(bounds)
            boundsCopy.left += paddingStart
            boundsCopy.right -= paddingEnd
            boundsCopy.top += paddingTop
            boundsCopy.bottom -= paddingBottom
            bg.bounds = boundsCopy
        }

        compoundDrawables.firstOrNull()?.let { first ->
            val b = first.bounds
            b.offsetTo(paddingStart + textPaddingHorizontal / 2, bounds.centerY() - (b.height() / 2))
        }

        compoundDrawables.lastOrNull()?.let { last ->
            val b = last.bounds
            b.offsetTo(bounds.width() - paddingEnd - textPaddingHorizontal / 2 - b.width(), bounds.centerY() - (b.height() / 2))
        }

        super.onBoundsChange(bounds)
    }


    override fun draw(canvas: Canvas) {

        val bounds = this.bounds

        background?.draw(canvas)

        var x = paddingStart + (textPaddingHorizontal / 2)
        var y = (bounds.height() / 2 - textBounds.exactCenterY()) + paddingTop / 2 - paddingBottom / 2

        compoundDrawables.firstOrNull()?.let { first ->
            if (DEBUG_LOG) {
                canvas.drawRect(first.bounds, debugPaint)
            }
            first.draw(canvas)
            x += first.bounds.width() + compoundPadding
        }

        compoundDrawables.lastOrNull()?.let { last ->
            if (DEBUG_LOG) {
                canvas.drawRect(last.bounds, debugPaint)
            }
            last.draw(canvas)
        }

        canvas.drawText(text, x.toFloat(), y, textPaint)

        if (DEBUG_LOG) {
            canvas.drawRect(x.toFloat(), y, (x + textBounds.width()).toFloat(), y - textBounds.height(), debugPaint)
        }
    }


    companion object {
        const val TAG = "TextDrawable"
        var DEBUG_LOG: Boolean = true

        class Builder {
            internal var textPadding: Size = Size(0, 0)
            internal val backgroundPadding: Rect = Rect()
            internal val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            internal var text: String = ""
            internal var background: Drawable? = null
            internal var compoundDrawables: Array<Drawable?> = arrayOf(null, null)
            internal var compoundPadding: Int = 0

            init {
                textPaint.textAlign = Paint.Align.LEFT
                textPaint.hinting = Paint.HINTING_ON
            }

            fun text(value: String) = apply {
                text = value
            }

            fun textSize(value: Float) = apply {
                textPaint.textSize = value
            }

            fun color(color: Long) = apply {
                textPaint.color = color.toInt()
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

            fun bold(value: Boolean) = apply {
                textPaint.isFakeBoldText = value
            }

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
        }
    }
}
