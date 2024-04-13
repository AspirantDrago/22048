package com.example.myapplication.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.example.myapplication.R.attr
import com.example.myapplication.model.GameModel
import com.example.myapplication.model.InfInt
import kotlin.math.min


class GameView : View {
    private val paint = Paint()
    private var _model: GameModel? = null
    var typedValueBackColor = TypedValue()
    var typedValueTextColor = TypedValue()
    var typedValueBorderColor = TypedValue()

    private val EMPTY_CELL = InfInt()

    public var model: GameModel?
        get() = _model
        set(value) {
            _model = value
        }

    public var size: Float = 0f
        get() {
            return min(width, height).toFloat()
        }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        context.theme.resolveAttribute(attr.colorGame, typedValueBackColor, true)
        context.theme.resolveAttribute(attr.colorText, typedValueTextColor, true)
        context.theme.resolveAttribute(attr.colorBorderGame, typedValueBorderColor, true)

        if (_model == null) {
            paint.color = Color.GRAY
            canvas.drawRect(0f, 0f, size, size, paint)
            return
        }
        val cellSize = (size - 1) / _model!!.size
        val textSize = 0.6f * cellSize
        paint.color = typedValueBackColor.data
        canvas.drawRect(0f, 0f, size, size, paint)
        paint.color = typedValueBorderColor.data
        paint.setStrokeWidth((size * 0.05 / _model!!.size).toFloat());
        for (i in 0.._model!!.size) {
            val y = i * cellSize
            canvas.drawLine(0f, y, size, y, paint)
            canvas.drawLine(y, 0f, y, size, paint)
        }
        paint.color = typedValueTextColor.data
        paint.setTextSize(textSize)
        for (row in 0 until _model!!.size) {
            for (col in 0 until _model!!.size) {
                if (_model!!.get(row, col) != EMPTY_CELL) {
                    val text = _model!!.get(row, col).toString()
                    val x = col * cellSize + (cellSize - paint.measureText(text)) / 2
                    val y = row * cellSize + (cellSize + textSize) / 2
                    canvas.drawText(text, x, y, paint)
                }
            }
        }
    }
}
