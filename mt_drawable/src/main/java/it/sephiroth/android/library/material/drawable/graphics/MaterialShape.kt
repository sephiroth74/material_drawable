package it.sephiroth.android.library.material.drawable.graphics

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.shapes.Shape

class MaterialShape(private val type: Type) : Shape() {

    constructor() : this(Type.START)

    var strokeWidth: Float = 0f
    private val path = Path()
    private val bounds = RectF()

    override fun clone(): Shape {
        return return MaterialShape(type).also { it.strokeWidth = strokeWidth }
    }

    private fun invalidatePath() {
        path.reset()

        when (type) {
            Type.ALL -> invalidatePathAll()
            Type.START -> invalidatePathStart()
            Type.END -> invalidatePathEnd()
        }
    }

    private fun invalidatePathStart() {
        val s = strokeWidth / 2
        val startX = bounds.right - (bounds.height() / 2) - s
        path.moveTo(s, s)
        path.lineTo(startX, s)


        path.arcTo(
            RectF(
                (bounds.right - bounds.height() - s),
                s,
                bounds.right - s,
                bounds.height() - s
            ),
            270f,
            180f,
            false
        )

        path.lineTo(s, bounds.height() - s)
        path.close()
    }

    private fun invalidatePathAll() {
        val s = strokeWidth / 2
        val h = bounds.height()
        val w = bounds.width()
        val r = bounds.right

        if (w < h) {
            path.moveTo(w / 2 + s, s)
            path.arcTo(RectF(w / 2 - s, s, r - s, h - s), 270f, 180f)
            path.lineTo(w / 2 + s, h - s)
            path.arcTo(RectF(s, s, w / 2 + s, h - 2), 90f, 180f)
        } else {
            path.moveTo(h / 2 + s, s)
            path.lineTo(w - h / 2 - s, s)
            path.arcTo(RectF(r - h - s, s, r - s, h - s), 270f, 180f)
            path.lineTo(h / 2 - s, h - s)
            path.arcTo(RectF(s, s, h - s, h - s), 90f, 180f)
        }
        path.close()
    }

    private fun invalidatePathEnd() {
        val s = strokeWidth / 2
        path.moveTo(bounds.height() / 2 + s, s)
        path.lineTo(bounds.right - s, s)
        path.lineTo(bounds.right - s, bounds.height() - s)
        path.lineTo(bounds.height() / 2 + s, bounds.height() - s)
        path.arcTo(RectF(s, s, bounds.height() - s, bounds.height() - s), 90f, 180f)
        path.close()
    }

    override fun onResize(width: Float, height: Float) {
        super.onResize(width, height)
        bounds.set(0f, 0f, width, height)
        invalidatePath()
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.saveLayer(bounds, null)
        canvas.drawPath(path, paint)
        canvas.restore()

    }

    enum class Type {
        ALL, START, END
    }

    companion object {
        const val TAG = "MaterialShape"
    }

}
