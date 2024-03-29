package it.sephiroth.android.app.app

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
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
    private lateinit var textView2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onContentChanged() {
        super.onContentChanged()
        TextDrawable.DEBUG_LOG = false

        textView1 = findViewById(R.id.textView1)
        textView2 = findViewById(R.id.textView2)

        val textSize = textView1.textSize
        Log.v(TAG, "textSize: $textSize")

        val icon1 = resources.getDrawable(R.drawable.shopping).apply {
            setBounds(0, 0, (textSize.toInt() * 0.9).toInt(), (textSize.toInt() * 0.9).toInt())
        }

        val drawable = TextDrawable {
            fromTextView(textView1)
            text("Not subscription")
            textPadding(60, 10)
            background(MaterialShapeDrawable.Builder(MaterialShape.Type.ALL).strokeWidth(4f).color(0xff25B252).style(Paint.Style.FILL).build())
            compoundDrawables(icon1, null)
            compoundPadding(24)
        }

//        drawable.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        drawable.setBackgroundColorFilter(PorterDuffColorFilter(0x25B252, PorterDuff.Mode.SRC_ATOP))

//        val builder = SpannableStringBuilder()
//        builder.append("The Avengers:  ")
//        SpannableStringBuilderUtil.appendDrawable(this, builder, drawable)
//        textView1.text = builder.toSpannable()

        textView1.background = drawable

        val icon3 = resources.getDrawable(R.drawable.shopping).apply {
            setBounds(0, 0, (textSize.toInt() * 0.9).toInt(), (textSize.toInt() * 0.9).toInt())
        }

        textView2.background = MaterialShapeDrawable.Builder(MaterialShape.Type.ALL).color(0xff25B252).style(Paint.Style.FILL).build()
        textView2.setCompoundDrawables(icon3, null, null, null)

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
