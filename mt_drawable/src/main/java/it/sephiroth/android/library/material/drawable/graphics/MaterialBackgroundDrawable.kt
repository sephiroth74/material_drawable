package it.sephiroth.android.library.material.drawable.graphics

import android.animation.ArgbEvaluator
import android.content.res.ColorStateList
import android.graphics.drawable.AnimatedStateListDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

@Suppress("unused")
class MaterialBackgroundDrawable {

    class Builder(type: MaterialShape.Type) {
        private var ripple: RippleDrawable? = null
        private val selector = AnimatedStateListDrawable()
        private val evaluator = ArgbEvaluator()
        private val states = hashMapOf<Int, Int>()
        private val shape = MaterialShapeDrawable.Builder(type)

        constructor(type: MaterialShape.Type, body: Builder.() -> Unit) : this(type) {
            this.body()
        }

        private fun state(stateSet: IntArray, drawable: ShapeDrawable): Builder {
            selector.addState(stateSet, drawable)
            return this
        }

        fun state(stateArray: IntArray, drawable: MaterialShapeDrawable.Builder): Builder {
            return state(stateArray, drawable.build())
        }

        fun state(@AttrRes state: Int, drawable: MaterialShapeDrawable.Builder): Builder {
            return state(intArrayOf(state), drawable)
        }

        fun disabled(style: MaterialShapeDrawable.Style): Builder {
            return state(-android.R.attr.state_enabled, shape.clone().of(style))
        }

        fun focused(style: MaterialShapeDrawable.Style): Builder {
            return state(android.R.attr.state_focused, shape.clone().of(style))
        }

        fun pressed(style: MaterialShapeDrawable.Style): Builder {
            return state(android.R.attr.state_pressed, shape.clone().of(style))
        }

        fun checked(style: MaterialShapeDrawable.Style): Builder {
            return state(android.R.attr.state_checked, shape.clone().of(style))
        }

        fun unchecked(style: MaterialShapeDrawable.Style): Builder {
            return state(-android.R.attr.state_checked, shape.clone().of(style))
        }

        fun selected(style: MaterialShapeDrawable.Style): Builder {
            return state(android.R.attr.state_selected, shape.clone().of(style))
        }

        fun normal(style: MaterialShapeDrawable.Style): Builder {
            return state(intArrayOf(), shape.clone().of(style))
        }

        fun ripple(@ColorInt color: Int, style: MaterialShapeDrawable.Style): Builder {
            ripple = RippleDrawable(
                ColorStateList.valueOf(color),
                selector,
                shape.clone().of(style).build()
            )
            return this
        }

        fun ripple(@ColorInt color: Int): Builder {
            ripple = RippleDrawable(
                ColorStateList.valueOf(color),
                selector,
                shape.clone().build()
            )
            return this
        }

        fun disabled(drawable: MaterialShapeDrawable.Builder): Builder {
            return state(-android.R.attr.state_enabled, drawable)
        }

        fun focused(drawable: MaterialShapeDrawable.Builder): Builder {
            return state(android.R.attr.state_focused, drawable)
        }

        fun pressed(drawable: MaterialShapeDrawable.Builder): Builder {
            return state(android.R.attr.state_pressed, drawable)
        }

        fun checked(drawable: MaterialShapeDrawable.Builder): Builder {
            return state(android.R.attr.state_checked, drawable)
        }

        fun unchecked(drawable: MaterialShapeDrawable.Builder): Builder {
            return state(-android.R.attr.state_checked, drawable)
        }

        fun selected(drawable: MaterialShapeDrawable.Builder): Builder {
            return state(android.R.attr.state_selected, drawable)
        }

        fun normal(drawable: MaterialShapeDrawable.Builder): Builder {
            return state(intArrayOf(), drawable)
        }

        fun ripple(@ColorInt color: Int, drawable: MaterialShapeDrawable.Builder): Drawable {
            ripple = RippleDrawable(ColorStateList.valueOf(color), selector, drawable.build())
            return this.build()
        }

        fun build(): Drawable {
            ripple?.let { return it } ?: run { return selector }
        }
    }
}

