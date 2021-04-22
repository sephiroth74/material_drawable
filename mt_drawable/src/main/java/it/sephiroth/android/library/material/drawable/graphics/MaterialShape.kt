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

    private fun invalidatePath() {
        path.reset()

        when (type) {
            Type.ALL -> invalidatePathAll()
            Type.START -> invalidatePathStart()
            Type.END -> invalidatePathEnd()
        }
    }

    private fun invalidatePathStart() {
        val start = bounds.right - bounds.height() / 2
        path.moveTo(0f, 0f)
        path.lineTo(start, 0f)
        path.arcTo(
            RectF((bounds.right - bounds.height()), 0f, bounds.right, bounds.height()),
            270f,
            180f
        )
        path.lineTo(0f, bounds.height())
        path.close()
    }

    private fun invalidatePathAll() {
        val h = bounds.height()
        val w = bounds.width()
        val r = bounds.right


        if (w < h) {
            path.moveTo(w / 2, 0f)
            path.arcTo(RectF(w / 2, 0f, r, h), 270f, 180f)
            path.lineTo(w / 2, h)
            path.arcTo(RectF(0f, 0f, w / 2, h), 90f, 180f)
        } else {
            path.moveTo(h / 2, 0f)
            path.lineTo(w - h / 2, 0f)
            path.arcTo(RectF(r - h, 0f, r, h), 270f, 180f)
            path.lineTo(h / 2, h)
            path.arcTo(RectF(0f, 0f, h, h), 90f, 180f)
        }
        path.close()
    }

    private fun invalidatePathEnd() {
        path.moveTo(bounds.height() / 2, 0f)
        path.lineTo(bounds.right, 0f)
        path.lineTo(bounds.right, bounds.height())
        path.lineTo(bounds.height() / 2, bounds.height())
        path.arcTo(RectF(0f, 0f, bounds.height(), bounds.height()), 90f, 180f)
        path.close()
    }

    override fun onResize(width: Float, height: Float) {
        super.onResize(width, height)
        bounds.set(0f, 0f, width, height)
        invalidatePath()
    }

//    val debugPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
//        setColor(Color.YELLOW)
//        setStyle(Paint.Style.STROKE)
//        strokeWidth = 4f
//    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.saveLayer(bounds, null)
        canvas.drawPath(path, paint)
        canvas.restore()
//        canvas.drawRect(bounds, debugPaint)

    }

    enum class Type {
        ALL, START, END
    }

}
