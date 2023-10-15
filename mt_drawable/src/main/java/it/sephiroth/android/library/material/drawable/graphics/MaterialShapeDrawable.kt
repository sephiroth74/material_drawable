package it.sephiroth.android.library.material.drawable.graphics

import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape

@Suppress("unused")
class MaterialShapeDrawable(private val s: Shape?) : ShapeDrawable(s) {

    @Suppress("unused")
    constructor() : this(null)

    init {
        paint.flags = Paint.SUBPIXEL_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
        paint.strokeCap = Paint.Cap.BUTT
        paint.strokeJoin = Paint.Join.MITER
    }

    fun clone(): MaterialShapeDrawable {
        val cloned = MaterialShapeDrawable(s)
        cloned.paint.set(paint)
        return cloned
    }

    companion object {
        const val TAG = "MaterialShapeDrawable"
    }

    class Style() {
        private var style: Paint.Style? = null
        private var color: Int? = null
        private var tint: Int? = null
        private var alpha: Int? = null
        private var strokeWidth: Float? = null

        constructor(func: Style.() -> Unit) : this() {
            this.func()
        }

        fun style(style: Paint.Style) = apply {
            this.style = style
        }

        fun color(color: Int) = apply {
            this.color = color
        }

        fun tint(color: Int) = apply {
            this.tint = color
        }

        fun alpha(alpha: Int) = apply {
            this.alpha = alpha
        }

        fun alpha(alpha: Float) = apply {
            this.alpha = (alpha * 255).toInt()
        }

        fun strokeWidth(width: Float) = apply {
            this.strokeWidth = width
        }

        fun copyTo(builder: Builder) {
            style?.let { builder.style(it) }
            color?.let { builder.color(it.toLong()) }
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

        fun style(style: Paint.Style) = apply {
            drawable.paint.style = style
        }

        fun color(color: Long) = apply {
            color(color.toInt())
        }

        fun color(color: Int) = apply {
            drawable.paint.color = color
        }

        fun tint(color: Int) = apply {
            drawable.setTint(color)
        }

        fun alpha(alpha: Int) = apply {
            drawable.alpha = alpha
        }

        fun alpha(alpha: Float) = apply {
            drawable.alpha = (alpha * 255).toInt()
        }

        fun strokeWidth(width: Float) = apply {
            drawable.paint.strokeWidth = width
            (drawable.shape as MaterialShape).strokeWidth = width
        }

        fun build(func: Builder.() -> Unit): MaterialShapeDrawable {
            this.func().also { return build() }
        }

        fun build() = drawable

    }
}
