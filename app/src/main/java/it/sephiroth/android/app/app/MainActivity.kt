package it.sephiroth.android.app.app

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import it.sephiroth.android.library.material.drawable.graphics.MaterialBackgroundDrawable
import it.sephiroth.android.library.material.drawable.graphics.MaterialShape
import it.sephiroth.android.library.material.drawable.graphics.MaterialShapeDrawable
import it.sephiroth.android.library.material.drawable.graphics.TextDrawable

class MainActivity : AppCompatActivity() {
    private lateinit var textView1: TextView
    private lateinit var textView2: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onContentChanged() {
        super.onContentChanged()
        textView1 = findViewById(R.id.textView1)

        val icon1 = resources.getDrawable(R.drawable.shopping)
        icon1.setBounds(0, 0, 30, 30)

        val drawable = TextDrawable {
            text("Hello World")
            textSize(32f)
            color(0x99000000)
            textPadding(50, 10)
            padding(0, 0, 0, 0)
            typeface(Typeface.MONOSPACE)
            bold(true)
            background(MaterialShapeDrawable.Builder(MaterialShape.Type.ALL).color(0xff25B252).build())
            compoundDrawables(icon1, null)
            compoundPadding(20)
        }


        textView1.background = drawable

//        drawable.callback = textView1

//        Log.d(TAG, "density: " + resources.displayMetrics.density)
//        Log.d(TAG, "densityDpi: " + resources.displayMetrics.densityDpi)
//        Log.d(TAG, "heightPixels: " + resources.displayMetrics.heightPixels)
//        Log.d(TAG, "scaledDensity: " + resources.displayMetrics.scaledDensity)
//        Log.d(TAG, "ydpi: " + resources.displayMetrics.ydpi)


//        textView1.setOnClickListener { Log.v("this", "onclick") }

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
