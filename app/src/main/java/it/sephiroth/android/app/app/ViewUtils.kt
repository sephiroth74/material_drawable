package it.sephiroth.android.app.app

import android.content.Context
import android.util.TypedValue
import android.widget.RelativeLayout
import androidx.annotation.AnyRes
import androidx.annotation.AttrRes
import androidx.core.view.updateLayoutParams

object ViewUtils {
    const val NOT_DEFINED = -1

    fun setParamsToSpecificSettingsPageContainer(contentContainer: RelativeLayout, leftMargin: Int = 0, rightMargin: Int = 0) {
        contentContainer.updateLayoutParams<RelativeLayout.LayoutParams> {
            this.leftMargin = leftMargin
            this.rightMargin = rightMargin
        }
    }

    @AnyRes
    fun getResourceIdFromAttrForCurrentTheme(context: Context?, @AttrRes attr: Int): Int {
        if (context == null || attr == NOT_DEFINED) {
            return NOT_DEFINED
        }

        return try {
            val a = TypedValue()
            context.theme.resolveAttribute(attr, a, true)
            a.resourceId
        } catch (e: Exception) {
            e.printStackTrace()
            NOT_DEFINED
        }
    }
}
