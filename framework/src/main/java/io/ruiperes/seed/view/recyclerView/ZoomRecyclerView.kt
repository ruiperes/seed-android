package io.ruiperes.seed.view.recyclerView

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.animation.DecelerateInterpolator
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import io.ruiperes.seed.R


@SuppressLint("ClickableViewAccessibility")
class ZoomRecyclerView : RecyclerView {

    var mScaleDetector: ScaleGestureDetector? = null
    var mGestureDetector: GestureDetectorCompat? = null


    var mViewWidth
        = 0f
    var mViewHeight
        = 0f
    var mTranX
        = 0f
    var mTranY
        = 0f
    var mScaleFactor
        = 0f


    var mActivePointerId = MotionEvent.INVALID_POINTER_ID
    var mLastTouchX
        = 0f
    var mLastTouchY
        = 0f


    var isScaling = false
    var isEnableScale = false


    var mScaleAnimator
        : ValueAnimator? = null
    var mScaleCenterX
        = 0f
    var mScaleCenterY
        = 0f
    var mMaxTranX
        = 0f
    var mMaxTranY
        = 0f


    var mMaxScaleFactor
        = 0f
    var mMinScaleFactor
        = 0f
    var mDefaultScaleFactor
        = 0f
    var mScaleDuration
        = 0

    constructor(context: Context?) : super(context!!) {
        init(null)
    }

    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context!!, attrs) {
        init(attrs)
    }

    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {
        init(attrs)
    }

    private fun init(attr: AttributeSet?) {
        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        mGestureDetector = GestureDetectorCompat(context, GestureListener())
        if (attr != null) {
            val a = context
                .obtainStyledAttributes(attr, R.styleable.ZoomRecyclerView, 0, 0)
            mMinScaleFactor = a.getFloat(R.styleable.ZoomRecyclerView_min_scale, DEFAULT_MIN_SCALE_FACTOR)
            mMaxScaleFactor = a.getFloat(R.styleable.ZoomRecyclerView_max_scale, DEFAULT_MAX_SCALE_FACTOR)
            mDefaultScaleFactor = a
                .getFloat(R.styleable.ZoomRecyclerView_default_scale, DEFAULT_SCALE_FACTOR)
            mScaleFactor = mDefaultScaleFactor
            mScaleDuration = a.getInteger(
                R.styleable.ZoomRecyclerView_zoom_duration,
                DEFAULT_SCALE_DURATION
            )
            a.recycle()
        } else {

            mMaxScaleFactor = DEFAULT_MAX_SCALE_FACTOR
            mMinScaleFactor = DEFAULT_MIN_SCALE_FACTOR
            mDefaultScaleFactor = DEFAULT_SCALE_FACTOR
            mScaleFactor = mDefaultScaleFactor
            mScaleDuration = DEFAULT_SCALE_DURATION
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onTouchEvent(@NonNull ev: MotionEvent): Boolean {
        if (!isEnableScale) {
            return super.onTouchEvent(ev)
        }
        var retVal = mScaleDetector!!.onTouchEvent(ev)
        retVal = mGestureDetector!!.onTouchEvent(ev) || retVal
        val action = ev.actionMasked
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                val pointerIndex = ev.actionIndex
                val x = ev.getX(pointerIndex)
                val y = ev.getY(pointerIndex)

                mLastTouchX = x
                mLastTouchY = y

                mActivePointerId = ev.getPointerId(0)
            }
            MotionEvent.ACTION_MOVE -> {
                try {

                    val pointerIndex = ev.findPointerIndex(mActivePointerId)
                    val x = ev.getX(pointerIndex)
                    val y = ev.getY(pointerIndex)
                    if (!isScaling && mScaleFactor > 1) {

                        val dx = x - mLastTouchX
                        val dy = y - mLastTouchY
                        setTranslateXY(mTranX + dx, mTranY + dy)
                        correctTranslateXY()
                    }
                    invalidate()

                    mLastTouchX = x
                    mLastTouchY = y
                } catch (e: Exception) {
                    val x = ev.x
                    val y = ev.y
                    if (!isScaling && mScaleFactor > 1 && mLastTouchX != INVALID_TOUCH_POSITION) {

                        val dx = x - mLastTouchX
                        val dy = y - mLastTouchY
                        setTranslateXY(mTranX + dx, mTranY + dy)
                        correctTranslateXY()
                    }
                    invalidate()

                    mLastTouchX = x
                    mLastTouchY = y
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mActivePointerId = MotionEvent.INVALID_POINTER_ID
                mLastTouchX = INVALID_TOUCH_POSITION
                mLastTouchY = INVALID_TOUCH_POSITION
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = ev.actionIndex
                val pointerId = ev.getPointerId(pointerIndex)
                if (pointerId == mActivePointerId) {


                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    mLastTouchX = ev.getX(newPointerIndex)
                    mLastTouchY = ev.getY(newPointerIndex)
                    mActivePointerId = ev.getPointerId(newPointerIndex)
                }
            }
        }
        return super.onTouchEvent(ev) || retVal
    }

    @SuppressLint("WrongConstant")
    override fun dispatchDraw(@NonNull canvas: Canvas) {
        canvas.save()
        canvas.translate(mTranX, mTranY)
        canvas.scale(mScaleFactor, mScaleFactor)


        super.dispatchDraw(canvas)
        canvas.restore()
    }

    private fun setTranslateXY(tranX: Float, tranY: Float) {
        mTranX = tranX
        mTranY = tranY
    }


    private fun correctTranslateXY() {
        val correctXY = correctTranslateXY(mTranX, mTranY)
        mTranX = correctXY[0]
        mTranY = correctXY[1]
    }

    private fun correctTranslateXY(x: Float, y: Float): FloatArray {
        var x = x
        var y = y
        if (mScaleFactor <= 1) {
            return floatArrayOf(x, y)
        }
        if (x > 0.0f) {
            x = 0.0f
        } else if (x < mMaxTranX) {
            x = mMaxTranX
        }
        if (y > 0.0f) {
            y = 0.0f
        } else if (y < mMaxTranY) {
            y = mMaxTranY
        }
        return floatArrayOf(x, y)
    }

    private fun zoom(startVal: Float, endVal: Float) {
        if (mScaleAnimator == null) {
            newZoomAnimation()
        }
        if (mScaleAnimator!!.isRunning) {
            return
        }


        mMaxTranX = mViewWidth - mViewWidth * endVal
        mMaxTranY = mViewHeight - mViewHeight * endVal
        val startTranX = mTranX
        val startTranY = mTranY
        var endTranX = mTranX - (endVal - startVal) * mScaleCenterX
        var endTranY = mTranY - (endVal - startVal) * mScaleCenterY
        val correct = correctTranslateXY(endTranX, endTranY)
        endTranX = correct[0]
        endTranY = correct[1]
        val scaleHolder = PropertyValuesHolder
            .ofFloat(PROPERTY_SCALE, startVal, endVal)
        val tranXHolder = PropertyValuesHolder
            .ofFloat(PROPERTY_TRANX, startTranX, endTranX)
        val tranYHolder = PropertyValuesHolder
            .ofFloat(PROPERTY_TRANY, startTranY, endTranY)
        mScaleAnimator!!.setValues(scaleHolder, tranXHolder, tranYHolder)
        mScaleAnimator!!.duration = mScaleDuration.toLong()
        mScaleAnimator!!.start()
    }

    private fun newZoomAnimation() {
        mScaleAnimator = ValueAnimator()
        mScaleAnimator!!.interpolator = DecelerateInterpolator()
        mScaleAnimator!!.addUpdateListener { animation ->
            mScaleFactor = animation.getAnimatedValue(PROPERTY_SCALE) as Float
            setTranslateXY(
                animation.getAnimatedValue(PROPERTY_TRANX) as Float,
                animation.getAnimatedValue(PROPERTY_TRANY) as Float
            )
            invalidate()
        }


        mScaleAnimator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                isScaling = true
            }

            override fun onAnimationEnd(animation: Animator) {
                isScaling = false
            }

            override fun onAnimationCancel(animation: Animator) {
                isScaling = false
            }
        })
    }


    private inner class ScaleListener : OnScaleGestureListener {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val mLastScale = mScaleFactor
            mScaleFactor *= detector.scaleFactor

            mScaleFactor = Math.max(mMinScaleFactor, Math.min(mScaleFactor, mMaxScaleFactor))
            mMaxTranX = mViewWidth - mViewWidth * mScaleFactor
            mMaxTranY = mViewHeight - mViewHeight * mScaleFactor
            mScaleCenterX = detector.focusX
            mScaleCenterY = detector.focusY
            val offsetX = mScaleCenterX * (mLastScale - mScaleFactor)
            val offsetY = mScaleCenterY * (mLastScale - mScaleFactor)
            setTranslateXY(mTranX + offsetX, mTranY + offsetY)
            isScaling = true
            invalidate()
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            if (mScaleFactor <= mDefaultScaleFactor) {
                mScaleCenterX = -mTranX / (mScaleFactor - 1)
                mScaleCenterY = -mTranY / (mScaleFactor - 1)
                mScaleCenterX = if (java.lang.Float.isNaN(mScaleCenterX)) 0F else mScaleCenterX
                mScaleCenterY = if (java.lang.Float.isNaN(mScaleCenterY)) 0F else mScaleCenterY
                zoom(mScaleFactor, mDefaultScaleFactor)
            }
            isScaling = false
        }
    }

    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            val startFactor = mScaleFactor
            val endFactor: Float
            if (mScaleFactor == mDefaultScaleFactor) {
                mScaleCenterX = e.x
                mScaleCenterY = e.y
                endFactor = mMaxScaleFactor
            } else {
                mScaleCenterX = if (mScaleFactor == 1f) e.x else -mTranX / (mScaleFactor - 1)
                mScaleCenterY = if (mScaleFactor == 1f) e.y else -mTranY / (mScaleFactor - 1)
                endFactor = mDefaultScaleFactor
            }
            zoom(startFactor, endFactor)
            return super.onDoubleTap(e)
        }
    }

    companion object {
        private const val TAG = "999"


        private const val DEFAULT_SCALE_DURATION = 300
        private const val DEFAULT_SCALE_FACTOR = 1f
        private const val DEFAULT_MAX_SCALE_FACTOR = 2.0f
        private const val DEFAULT_MIN_SCALE_FACTOR = 0.5f
        private const val PROPERTY_SCALE = "scale"
        private const val PROPERTY_TRANX = "tranX"
        private const val PROPERTY_TRANY = "tranY"
        private const val INVALID_TOUCH_POSITION = -1f
    }
}
