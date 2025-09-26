package com.example.ledtextapp

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var button: Button
    private lateinit var ledTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.editText)
        button = findViewById(R.id.button)
        ledTextView = findViewById(R.id.ledTextView)

        button.setOnClickListener {
            val inputText = editText.text.toString()
            if (inputText.isNotBlank()) {
                ledTextView.text = inputText
                ledTextView.post {
                    startLEDScrollingText(inputText)
                }
            }
        }
    }

    private fun startLEDScrollingText(text: String) {
        val displayWidth = resources.displayMetrics.widthPixels
        val textWidth = ledTextView.paint.measureText(text)

        val animator = ValueAnimator.ofFloat(displayWidth.toFloat(), -textWidth)
        animator.duration = (text.length * 150L).coerceAtLeast(3000L)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener { animation ->
            val scrollX = animation.animatedValue as Float
            ledTextView.translationX = scrollX
            applyLEDShader(ledTextView, text)
        }
        animator.start()
    }

    private fun applyLEDShader(textView: TextView, text: String) {
        val paint = textView.paint
        val width = paint.measureText(text).toInt().coerceAtLeast(1)

        val colors = intArrayOf(
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.YELLOW,
            Color.MAGENTA,
            Color.CYAN,
            Color.RED
        )
        val positions = floatArrayOf(0f, 0.2f, 0.4f, 0.6f, 0.8f, 0.9f, 1f)

        val shader = LinearGradient(
            0f, 0f,
            width.toFloat(), 0f,
            colors,
            positions,
            Shader.TileMode.CLAMP
        )
        paint.shader = shader
        textView.invalidate()
    }
}
