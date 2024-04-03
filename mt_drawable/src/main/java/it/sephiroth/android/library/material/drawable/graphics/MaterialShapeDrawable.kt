package it.sephiroth.android.library.material.drawable.graphics

import android.graphics.BlendMode
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import android.os.Build

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
        private var paintFlags: Int? = null
        private var strokeCap: Paint.Cap? = null
        private var strokeJoin: Paint.Join? = null
        private var blendMode: BlendMode? = null
        private var colorFilter: ColorFilter? = null
        private var strokeMiter: Float? = null
        private var isSubpixelText: Boolean? = null
        private var isDither: Boolean? = null
        private var style: Paint.Style? = null
        private var color: Int? = null
        private var tint: Int? = null
        private var alpha: Int? = null
        private var strokeWidth: Float? = null

        constructor(func: Style.() -> Unit) : this() {
            this.func()
        }

        fun flags(flags: Int) = apply { paintFlags = flags }

        fun strokeCap(cap: Paint.Cap) = apply { strokeCap = cap }

        fun strokeJoin(value: Paint.Join) = apply { strokeJoin = value }

        fun blendMode(value: BlendMode?) = apply { blendMode = value }

        fun colorFilter(value: ColorFilter?) = apply { colorFilter = value }

        fun strokeMiter(value: Float?) = apply { strokeMiter = value }

        fun isSubpixelText(value: Boolean) = apply { isSubpixelText = value }

        fun isSubpixeisDitherlText(value: Boolean) = apply { isDither = value }

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
            paintFlags?.let { builder.paintFlags(it) }
            strokeCap?.let { builder.strokeCap(it) }
            strokeJoin?.let { builder.strokeJoin(it) }
            blendMode?.let { builder.blendMode(it) }
            colorFilter?.let { builder.colorFilter(it) }
            strokeMiter?.let { builder.strokeMiter(it) }
            isSubpixelText?.let { builder.isSubpixelText(it) }
            isDither?.let { builder.isDither(it) }
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

        fun paint(paint: Paint) = apply { drawable.paint.set(paint) }

        fun paintFlags(value: Int) = apply { drawable.paint.flags = value }

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

        fun flags(flags: Int) = apply { drawable.paint.flags = flags }

        fun strokeCap(cap: Paint.Cap) = apply { drawable.paint.strokeCap = cap }

        fun strokeJoin(value: Paint.Join) = apply { drawable.paint.strokeJoin = value }

        fun blendMode(value: BlendMode?) = apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                drawable.paint.blendMode = value
            }
        }

        fun colorFilter(value: ColorFilter?) = apply { drawable.paint.colorFilter = value }

        fun strokeMiter(value: Float) = apply { drawable.paint.strokeMiter = value }

        fun isSubpixelText(value: Boolean) = apply { drawable.paint.isSubpixelText = value }

        fun isDither(value: Boolean) = apply { drawable.paint.isDither = value }

        fun build(func: Builder.() -> Unit): MaterialShapeDrawable {
            this.func().also { return build() }
        }

        fun build() = drawable

    }
}
