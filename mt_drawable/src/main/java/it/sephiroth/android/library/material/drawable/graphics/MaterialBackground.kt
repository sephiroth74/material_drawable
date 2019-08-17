package it.sephiroth.android.library.material.drawable.graphics

import android.animation.ArgbEvaluator
import android.content.res.ColorStateList
import android.graphics.drawable.AnimatedStateListDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import androidx.annotation.ColorInt

@Suppress("unused")
class MaterialBackgroundDrawable {

    class Builder {
        private var ripple: RippleDrawable? = null
        private val selector = AnimatedStateListDrawable()
        private val evaluator = ArgbEvaluator()
        private val states = hashMapOf<Int, Int>()

        constructor(body : Builder.() -> Unit) {
            this.body()
        }

        fun addState(stateSet: IntArray, drawable: ShapeDrawable): Builder {
            selector.addState(stateSet, drawable)
            return this
        }

        fun addPressed(drawable: MaterialShapeDrawable.Builder): Builder {
            return addState(intArrayOf(android.R.attr.state_pressed), drawable.build())
        }

        fun addChecked(drawable: MaterialShapeDrawable.Builder): Builder {
            return addState(intArrayOf(android.R.attr.state_checked), drawable.build())
        }

        fun addSelected(drawable: MaterialShapeDrawable.Builder): Builder {
            return addState(intArrayOf(android.R.attr.state_selected), drawable.build())
        }

        fun addNormal(drawable: MaterialShapeDrawable.Builder): Builder {
            return addState(intArrayOf(), drawable.build())
        }

        fun ripple(@ColorInt color: Int, drawable: MaterialShapeDrawable.Builder): Builder {
            ripple = RippleDrawable(
                    ColorStateList.valueOf(color), selector, drawable.build()
                                   )
            return this
        }

        fun build(): Drawable {
            ripple?.let { return it } ?: run { return selector }
        }
    }
}

