package it.sephiroth.android.app.app

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AnyRes
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * swisscomtvui
 *
 * @author Alessandro Crugnola on 24.06.20 - 08:34
 */
object ThemeUtil {
    private val TL_TYPED_VALUE = ThreadLocal<TypedValue>()
    const val NOT_DEFINED = 0

    private fun getTypedValue(): TypedValue {
        var typedValue = TL_TYPED_VALUE.get()
        if (typedValue == null) {
            typedValue = TypedValue()
            TL_TYPED_VALUE.set(typedValue)
        }
        return typedValue
    }

    fun getThemeDrawable(context: Context, @AttrRes attrId: Int): Drawable? {
        val resId = ViewUtils.getResourceIdFromAttrForCurrentTheme(context, attrId)
        if (resId > 0) {
            return ContextCompat.getDrawable(context, resId)?.apply {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            }
        }
        return null
    }

    fun getThemeInteger(context: Context, @AttrRes attrId: Int): Int? {
        return getThemeTypedValue(context, attrId)?.data
    }

    fun getThemeInteger(context: Context, @AttrRes attrId: Int, defaultValue: Int): Int {
        return getThemeInteger(context, attrId) ?: defaultValue
    }

    fun getThemeDrawableResource(context: Context, @AttrRes attrId: Int): Int? {
        val resId = getThemeResourceId(context, attrId)
        if (resId > 0) {
            return resId
        }
        return null
    }

    fun getDrawable(context: Context, @DrawableRes resId: Int): Drawable? {
        if (resId != 0) {
            return ContextCompat.getDrawable(context, resId)?.apply {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            }
        }
        return null
    }

    @AnyRes
    fun getThemeResourceId(context: Context, @AttrRes attributeId: Int): Int {
        if (attributeId != NOT_DEFINED) {
            val tv = getTypedValue()
            if (context.theme.resolveAttribute(attributeId, tv, true)) {
                return tv.resourceId
            }
        }
        return NOT_DEFINED
    }

    private fun getThemeTypedValue(context: Context, @AttrRes attributeId: Int): TypedValue? {
        if (attributeId != NOT_DEFINED) {
            val tv = getTypedValue()
            if (context.theme.resolveAttribute(attributeId, tv, true)) {
                return tv
            }
        }
        return null
    }
}
