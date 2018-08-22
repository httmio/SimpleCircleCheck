package com.example.paulliu.circlecheckanimation

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View


/**
 * Created by paul.liu on 2018/8/9.
 */
class SimpleCircle : View {
    var paint = Paint()
    var roundColor: Int = 0
    var roundWidth: Float = 0f
    var progressColor: Int = 0
    var progressWidth: Float = 0f
    var max: Int = 0 // max progress
    var style: Int = 0
    var startAngle = 0
    val STROKE = 0
    val FILL = 1
    var progress: Int = 0
    var drawLineX: Float = 0.0f
    var drawLineY: Float = 0.0f
    var x1 = 0f
    var y1 = 0f
    var x2 = 0f
    var y2 = 0f
    var x3 = 0f
    var y3 = 0f
    var centerX: Float = 0f
    var radius: Float = 0f
    var animationTime: Long = 1000

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        //Load attr
        var mTypedArray = context?.obtainStyledAttributes(attrs, R.styleable.SimpleRoundProgress)
        roundColor = mTypedArray!!.getColor(R.styleable.SimpleRoundProgress_srp_roundColor, Color.RED)
        roundWidth = mTypedArray.getDimension(R.styleable.SimpleRoundProgress_srp_roundWidth, 5f)
        progressColor = mTypedArray.getColor(R.styleable.SimpleRoundProgress_srp_progressColor, Color.GREEN)
        progressWidth = mTypedArray.getDimension(R.styleable.SimpleRoundProgress_srp_progressWidth, roundWidth)
        max = mTypedArray.getInteger(R.styleable.SimpleRoundProgress_srp_max, 100)
        style = mTypedArray.getInt(R.styleable.SimpleRoundProgress_srp_style, 0)
        startAngle = mTypedArray.getInt(R.styleable.SimpleRoundProgress_srp_startAngle, 0)
        mTypedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = width / 2f
        radius = centerX - roundWidth / 2
        x1 = (centerX - radius / 2f)
        y1 = (centerX - radius / 6f)
        x2 = (centerX - radius / 6f)
        y2 = (centerX + radius / 3f)
        x3 = (centerX + radius / 2f)
        y3 = (centerX - radius / 4f)
        drawLineX = x1
        drawLineY = y1
        doAnimation()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val centerX: Float = width / 2f
        val radius: Float = centerX - roundWidth / 2
        paint.strokeWidth = roundWidth
        paint.color = roundColor
        paint.isAntiAlias = true
        when (style) {
            STROKE -> paint.style = Paint.Style.STROKE
            FILL -> paint.style = Paint.Style.FILL_AND_STROKE
        }
        canvas?.drawCircle(centerX, centerX, radius, paint)
        paint.strokeCap = Paint.Cap.ROUND

        var path = Path()
        path.moveTo(x1, y1)
        path.lineTo(x2, y2)
        path.lineTo(x3, y3)
        canvas?.drawPath(path, paint)

        paint.strokeWidth = progressWidth
        paint.color = progressColor

        val oval = RectF(centerX - radius, centerX - radius, centerX + radius, centerX + radius)
        var path1 = Path()

        val sweepAngle = -360 * progress / max

        when (style) {
            STROKE -> {
                canvas?.drawArc(oval, startAngle.toFloat(), sweepAngle.toFloat(), false, paint)
                if (drawLineX < x2 && drawLineX != x1) {
                    path1.moveTo(x1, y1)
                    path1.lineTo(drawLineX, drawLineY)
                } else if (drawLineX >= x2) {
                    path1.moveTo(x1, y1)
                    path1.lineTo(x2, y2)
                    path1.lineTo(drawLineX, drawLineY)
                }
                canvas?.drawPath(path1, paint)
            }
            FILL -> {
                canvas?.drawArc(oval, startAngle.toFloat(), sweepAngle.toFloat(), true, paint)
            }
        }

        @Synchronized
        fun setMax(max: Int) {
            if (max < 0) {
                throw IllegalArgumentException("max not less than 0")
            }
            this.max = max
        }

    }

    fun doAnimation() {
        x1 = (centerX - radius / 2f)
        y1 = (centerX - radius / 6f)
        x2 = (centerX - radius / 6f)
        y2 = (centerX + radius / 3f)
        x3 = (centerX + radius / 2f)
        y3 = (centerX - radius / 4f)
        drawLineX = x1
        drawLineY = y1

        var mAnimator: ValueAnimator = ValueAnimator.ofInt(0, 100)
        mAnimator.addUpdateListener { anim ->
            progress = anim.animatedValue as Int
            Log.d("TAG", progress.toString())
            invalidate()

        }
        mAnimator.duration = 1000
        var lineXAnimator: ValueAnimator = ValueAnimator.ofFloat(x1, x2, x3)
        lineXAnimator.addUpdateListener { anim ->
            drawLineX = anim.animatedValue as Float
            invalidate()
        }
        lineXAnimator.duration = animationTime
        var lineYAnimator: ValueAnimator = ValueAnimator.ofFloat(y1, y2, y3)
        lineYAnimator.addUpdateListener { anim ->
            drawLineY = anim.animatedValue as Float
            invalidate()
        }
        lineYAnimator.duration = animationTime
        var aniSet = AnimatorSet()
        aniSet.play(mAnimator).before(lineXAnimator)
        aniSet.play(mAnimator).before(lineYAnimator)
        aniSet.start()
    }

}