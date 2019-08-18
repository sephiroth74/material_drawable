package it.sephiroth.android.library.material.drawable.graphics

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import kotlin.math.roundToInt

@Suppress("unused")
class MaterialShapeDrawable(private val s: Shape?) : ShapeDrawable(s) {

    @Suppress("unused")
    constructor() : this(null)

    init {
        paint.flags = Paint.ANTI_ALIAS_FLAG
        paint.strokeCap = Paint.Cap.SQUARE
        paint.strokeJoin = Paint.Join.ROUND
    }

    fun clone() = MaterialShapeDrawable(s)

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

    class Style {
        private var style: Paint.Style? = null
        private var color: Int? = null
        private var tint: Int? = null
        private var alpha: Int? = null
        private var strokeWidth: Float? = null

        fun style(style: Paint.Style): Style {
            this.style = style
            return this
        }

        fun color(color: Int): Style {
            this.color = color
            return this
        }

        fun tint(color: Int): Style {
            this.tint = color
            return this
        }

        fun alpha(alpha: Int): Style {
            this.alpha = alpha
            return this
        }

        fun alpha(alpha: Float): Style {
            this.alpha = (alpha * 255).toInt()
            return this
        }

        fun strokeWidth(width: Float): Style {
            this.strokeWidth = width
            return this
        }

        fun copyTo(builder: Builder) {
            style?.let { builder.style(it) }
            color?.let { builder.color(it) }
            tint?.let { builder.tint(it) }
            alpha?.let { builder.alpha(it) }
            strokeWidth?.let { builder.strokeWidth(it) }
        }
    }

    class Builder(private var type: MaterialShape.Type) {

        private var drawable = MaterialShapeDrawable(MaterialShape(type))

        fun of(style: Style): Builder {
            style.copyTo(this)
            return this
        }

        fun clone(): Builder {
            val builder = Builder(type)
            builder.drawable = drawable.clone()
            return builder
        }

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