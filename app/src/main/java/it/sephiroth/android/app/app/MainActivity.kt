package it.sephiroth.android.app.app

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import it.sephiroth.android.library.material.drawable.graphics.MaterialBackgroundDrawable
import it.sephiroth.android.library.material.drawable.graphics.MaterialShape
import it.sephiroth.android.library.material.drawable.graphics.MaterialShapeDrawable
import it.sephiroth.android.library.material.drawable.graphics.TextDrawable

class MainActivity : AppCompatActivity() {
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onContentChanged() {
        super.onContentChanged()
        TextDrawable.DEBUG_LOG = false

        textView1 = findViewById(R.id.textView1)
        textView2 = findViewById(R.id.textView2)

        val textSize = textView1.textSize
//
//        textView1.background = MaterialBackgroundDrawable.Builder(MaterialShape.Type.ALL) {
//            focused(MaterialShapeDrawable.Style {
//                color(0xff0A75B0.toInt())
//            })
//            pressed(MaterialShapeDrawable.Style {
//                color(0xff0A75B0.toInt())
//            })
//            normal(MaterialShapeDrawable.Style {
//                flags(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG or Paint.SUBPIXEL_TEXT_FLAG or Paint.DITHER_FLAG)
//                color(Color.WHITE)
//                strokeWidth(2f)
//                style(Paint.Style.STROKE)
//                strokeCap(Paint.Cap.ROUND)
//                isDither(true)
//                isSubpixelText(true)
//            })
//        }.build()

        val icon3 = resources.getDrawable(R.drawable.shopping).apply {
            setBounds(0, 0, (textSize.toInt() * 0.9).toInt(), (textSize.toInt() * 0.9).toInt())
        }

        textView2.background = MaterialBackgroundDrawable.Builder(MaterialShape.Type.ALL) {

            focused(MaterialShapeDrawable.Style {
                flags(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG or Paint.SUBPIXEL_TEXT_FLAG or Paint.DITHER_FLAG)
                color(0xff0A75B0.toInt())
                strokeWidth(20f)
                style(Paint.Style.STROKE)
                strokeCap(Paint.Cap.SQUARE)
                strokeJoin(Paint.Join.MITER)
                strokeMiter(20.0f)
            })

            normal(MaterialShapeDrawable.Style {
                flags(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG or Paint.SUBPIXEL_TEXT_FLAG or Paint.DITHER_FLAG)
                color(Color.WHITE)
                strokeWidth(2f)
                style(Paint.Style.STROKE)
                strokeCap(Paint.Cap.ROUND)
            })
        }.build()

        textView2.setCompoundDrawables(icon3, null, null, null)
        textView1.setCompoundDrawables(icon3, null, null, null)

        Log.d(TAG, "Drawable added...")
    }

    class CustomView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : View(context, attrs, defStyleAttr) {
        private val background2 = MaterialBackgroundDrawable.Builder(MaterialShape.Type.START) {
            normal(MaterialShapeDrawable.Style {
                color(Color.LTGRAY)
            })
        }.build()

        private val debugPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.RED
            strokeWidth = 6f
            style = Paint.Style.STROKE
        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)
            background2.setBounds(paddingLeft, paddingTop, w - paddingRight, h - paddingBottom)
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            background2.draw(canvas)
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), debugPaint)
        }

    }

    companion object {
        const val TAG = "MainActivity"
    }
}
