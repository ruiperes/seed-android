package io.ruiperes.seed.view.cropwindow

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import io.ruiperes.seed.R
import io.ruiperes.seed.view.cropwindow.edge.Edge
import io.ruiperes.seed.view.cropwindow.handle.Handle
import io.ruiperes.seed.view.cropwindow.util.HandleUtil
import io.ruiperes.seed.view.cropwindow.util.PaintUtil


class CropImageView : AppCompatImageView {

    private var mSurroundingAreaOverlayPaint: Paint? = null



    private var mHandleRadius = 48f




    private var mSnapRadius = 20f


    private var mBitmapRect = RectF()






    private val mTouchOffset = PointF()


    private var mPressedHandle: Handle? = null


    private var mGuidelinesMode = 1


    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {

        mGuidelinesMode = 1
        val resources = context.resources
        mSurroundingAreaOverlayPaint = PaintUtil.newSurroundingAreaOverlayPaint(resources)
        mHandleRadius = resources.getDimension(R.dimen.touchSize)
        mSnapRadius = 0f
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mBitmapRect = bitmapRect
        initCropWindow(mBitmapRect)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawDarkenedSurroundingArea(canvas)
        drawBorder(canvas)
        drawCorners(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {


        return if (!isEnabled) {
            false
        } else when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onActionDown(event.x, event.y)
                true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
                onActionUp()
                true
            }
            MotionEvent.ACTION_MOVE -> {
                onActionMove(event.x, event.y)
                parent.requestDisallowInterceptTouchEvent(true)
                true
            }
            else -> false
        }
    }


    val croppedImage: Bitmap?
        get() {


            val drawable = drawable
            if (drawable == null || drawable !is BitmapDrawable) {
                return null
            }


            val originalBitmap = drawable.bitmap

            val h = bitmapRect.bottom - bitmapRect.top
            val w = bitmapRect.right - bitmapRect.left
            val widthFinal: Float = (Edge.width * originalBitmap.width) / w
            val heightFinal: Float = (Edge.height * originalBitmap.height) / h
            val leftFinal: Float = (Edge.LEFT.coordinate * originalBitmap.width) / w
            val topFinal: Float = (Edge.TOP.coordinate * originalBitmap.height) / h

            return Bitmap.createBitmap(originalBitmap, leftFinal.toInt(), topFinal.toInt(), widthFinal.toInt(), heightFinal.toInt())
        }


    private val bitmapRect: RectF
        get() {
            val drawable = drawable ?: return RectF()


            val matrixValues = FloatArray(9)
            imageMatrix.getValues(matrixValues)


            val scaleX = matrixValues[Matrix.MSCALE_X]
            val scaleY = matrixValues[Matrix.MSCALE_Y]
            val transX = matrixValues[Matrix.MTRANS_X]
            val transY = matrixValues[Matrix.MTRANS_Y]


            val drawableIntrinsicWidth = drawable.intrinsicWidth
            val drawableIntrinsicHeight = drawable.intrinsicHeight


            val drawableDisplayWidth = Math.round(drawableIntrinsicWidth * scaleX)
            val drawableDisplayHeight = Math.round(drawableIntrinsicHeight * scaleY)


            val left = 0f
            val top = 0f
            val right = (left + drawableDisplayWidth).coerceAtMost(width.toFloat())
            val bottom = (top + drawableDisplayHeight).coerceAtMost(height.toFloat())
            return RectF(left, top, right, bottom)
        }


    private fun initCropWindow(bitmapRect: RectF) {
        Edge.LEFT.coordinate = bitmapRect.left
        Edge.TOP.coordinate = bitmapRect.top
        Edge.RIGHT.coordinate = bitmapRect.right
        Edge.BOTTOM.coordinate = bitmapRect.bottom
    }

    private fun drawDarkenedSurroundingArea(canvas: Canvas) {
        val bitmapRect = mBitmapRect
        val left = Edge.LEFT.coordinate + 2
        val top = Edge.TOP.coordinate
        val right = Edge.RIGHT.coordinate - 2
        val bottom = Edge.BOTTOM.coordinate




        canvas.drawRect(bitmapRect.left, bitmapRect.top, bitmapRect.right, top, mSurroundingAreaOverlayPaint!!)
        canvas.drawRect(bitmapRect.left, bottom, bitmapRect.right, bitmapRect.bottom, mSurroundingAreaOverlayPaint!!)
        canvas.drawRect(bitmapRect.left, top, left, bottom, mSurroundingAreaOverlayPaint!!)
        canvas.drawRect(right, top, bitmapRect.right, bottom, mSurroundingAreaOverlayPaint!!)
    }

    private fun drawGuidelines(canvas: Canvas) {
    }

    private fun drawBorder(canvas: Canvas) {
        val d = resources.getDrawable(R.drawable.ic_crop_frame)
        d.bounds = Rect(
            Edge.LEFT.coordinate.toInt(),
            Edge.TOP.coordinate.toInt(),
            Edge.RIGHT.coordinate.toInt(),
            Edge.BOTTOM.coordinate.toInt()
        )
        d.draw(canvas)
    }

    private fun drawCorners(canvas: Canvas) {}

    private fun shouldGuidelinesBeShown(): Boolean {
        return (mGuidelinesMode == GUIDELINES_ON
            || mGuidelinesMode == GUIDELINES_ON_TOUCH && mPressedHandle != null)
    }

    private val targetAspectRatio: Float
        private get() = 1 / 1.toFloat()


    private fun onActionDown(x: Float, y: Float) {
        val left =
            Edge.LEFT.coordinate
        val top =
            Edge.TOP.coordinate
        val right =
            Edge.RIGHT.coordinate
        val bottom =
            Edge.BOTTOM.coordinate
        mPressedHandle = HandleUtil.getPressedHandle(x, y, left, top, right, bottom, mHandleRadius)



        if (mPressedHandle != null) {
            HandleUtil.getOffset(mPressedHandle!!, x, y, left, top, right, bottom, mTouchOffset)
            invalidate()
        }
    }


    private fun onActionUp() {
        if (mPressedHandle != null) {
            mPressedHandle = null
            invalidate()
        }
    }


    private fun onActionMove(x: Float, y: Float) {
        var x = x
        var y = y
        if (mPressedHandle == null) {
            return
        }



        x += mTouchOffset.x
        y += mTouchOffset.y


        mPressedHandle!!.updateCropWindow(x, y, mBitmapRect, mSnapRadius)
        invalidate()
    }

    companion object {

        private val TAG = CropImageView::class.java.name
        const val GUIDELINES_OFF = 0
        const val GUIDELINES_ON_TOUCH = 1
        const val GUIDELINES_ON = 2
    }
}
