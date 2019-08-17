package it.sephiroth.android.library.material.drawable.graphics

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import kotlin.math.roundToInt

class MaterialShapeDrawable(s: Shape?) : ShapeDrawable(s) {

    @Suppress("unused")
    constructor() : this(null)

    init {
        paint.flags = Paint.ANTI_ALIAS_FLAG
        paint.strokeCap = Paint.Cap.SQUARE
        paint.strokeJoin = Paint.Join.ROUND
    }

    override fun onBoundsChange(bounds: Rect?) {
        if (paint.style != Paint.Style.FILL && paint.strokeWidth > 0) {
            bounds?.inset(paint.strokeWidth.roundToInt(), paint.strokeWidth.roundToInt())
        }
        super.onBoundsChange(bounds)
    }

    override fun getMinimumWidth(): Int {
        return super.getMinimumWidth()
    }

    override fun getMinimumHeight(): Int {
        return super.getMinimumHeight()
    }

    class Builder(type: MaterialShape.Type) {
        val shape: MaterialShape =
                MaterialShape(type)
        val drawable = MaterialShapeDrawable(shape)

        fun style(style: Paint.Style): Builder {
            drawable.paint.style = style
            return this
        }

        fun color(color: Int): Builder {
            drawable.paint.color = color
            return this
        }

        fun tint(color: Int): Builder {
            drawable.setTint(color)
            return this
        }

        fun alpha(alpha: Int): Builder {
            drawable.alpha = alpha
            return this
        }

        fun alpha(alpha: Float): Builder {
            drawable.alpha = (alpha * 255).toInt()
            return this
        }

        fun strokeWidth(width: Float): Builder {
            drawable.paint.strokeWidth = width
            return this
        }

        fun build(func: Builder.() -> Unit): MaterialShapeDrawable {
            this.func().also { return build() }
        }

        fun build() = drawable

    }
}