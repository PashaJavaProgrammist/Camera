package com.haretskiy.pavel.magiccamera.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import com.haretskiy.pavel.magiccamera.R

class BorderTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : android.support.v7.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    private var strokeWidth: Float = 0.toFloat()
    private var strokeColor: Int = 0
    private var strokeJoin: Paint.Join? = null
    private var strokeMiter: Float = 0.toFloat()


    init {
        attrs?.let {
            context.obtainStyledAttributes(attrs, R.styleable.BorderTextView).also { typedArray ->
                try {
                    if (typedArray.hasValue(R.styleable.BorderTextView_strokeColor)) {
                        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.BorderTextView_strokeWidth, 1).toFloat()
                        strokeColor = typedArray.getColor(R.styleable.BorderTextView_strokeColor, -0x1000000)
                        strokeMiter = typedArray.getDimensionPixelSize(R.styleable.BorderTextView_strokeMiter, 10).toFloat()
                        var strokeJoin: Paint.Join? = null
                        when (typedArray.getInt(R.styleable.BorderTextView_strokeJoinStyle, 0)) {
                            0 -> strokeJoin = Paint.Join.MITER
                            1 -> strokeJoin = Paint.Join.BEVEL
                            2 -> strokeJoin = Paint.Join.ROUND
                        }
                        this.setStroke(strokeWidth, strokeColor, strokeJoin, strokeMiter)
                    }
                } finally {
                    typedArray.recycle()
                }
            }
        }
    }

    private fun setStroke(width: Float, color: Int, join: Paint.Join?, miter: Float) {
        strokeWidth = width
        strokeColor = color
        strokeJoin = join
        strokeMiter = miter
    }

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val restoreColor = this.currentTextColor
        val paint = this.paint
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = strokeJoin
        paint.strokeMiter = strokeMiter
        this.setTextColor(strokeColor)
        paint.strokeWidth = strokeWidth
        super.onDraw(canvas)
        paint.style = Paint.Style.FILL
        this.setTextColor(restoreColor)
    }
}