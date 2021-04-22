package it.sephiroth.android.app.app

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.TextView
import it.sephiroth.android.library.material.drawable.graphics.MaterialBackgroundDrawable
import it.sephiroth.android.library.material.drawable.graphics.MaterialShape
import it.sephiroth.android.library.material.drawable.graphics.MaterialShapeDrawable

class MainActivity : AppCompatActivity() {
    private lateinit var textView1: View
    private lateinit var textView2: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onContentChanged() {
        super.onContentChanged()
        textView1 = findViewById(R.id.textView1)
        textView2 = findViewById(R.id.textView2)

        textView1.background = MaterialBackgroundDrawable.Builder(MaterialShape.Type.ALL) {
            focused(MaterialShapeDrawable.Style {
                color(Color.YELLOW)
            })
            pressed(MaterialShapeDrawable.Style {
                color(Color.GREEN)
            })
            normal(MaterialShapeDrawable.Style {
                color(Color.BLUE)
            })
        }.build()

        textView1.setOnClickListener { Log.v("this", "onclick") }
        textView2.setOnClickListener { Log.v("this", "onclick") }
    }

    class CustomView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : View(context, attrs, defStyleAttr) {
        val background2 = MaterialBackgroundDrawable.Builder(MaterialShape.Type.START) {
            normal(MaterialShapeDrawable.Style {
                color(Color.LTGRAY)
            })
        }.build()

        val debugPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.RED
            strokeWidth = 6f
            style = Paint.Style.STROKE
        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)

            background2.setBounds(100, 0, 200, height)
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            background2.draw(canvas)
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), debugPaint)
        }

    }
}
