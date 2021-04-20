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

/**
 * Custom view that provides cropping capabilities to an image.
 */
class CropImageView : AppCompatImageView {
    // The Paint used to darken the surrounding areas outside the crop area.
    private var mSurroundingAreaOverlayPaint: Paint? = null

    // The radius (in pixels) of the touchable area around the handle.
    // We are basing this value off of the recommended 48dp touch target size.
    private var mHandleRadius = 48f

    // An edge of the crop window will snap to the corresponding edge of a
    // specified bounding box when the crop window edge is less than or equal to
    // this distance (in pixels) away from the bounding box edge.
    private var mSnapRadius = 20f

    // The bounding box around the Bitmap that we are cropping.
    private var mBitmapRect = RectF()

    // Holds the x and y offset between the exact touch location and the exact
    // handle location that is activated. There may be an offset because we
    // allow for some leeway (specified by 'mHandleRadius') in activating a
    // handle. However, we want to maintain these offset values while the handle
    // is being dragged so that the handle doesn't jump.
    private val mTouchOffset = PointF()

    // The Handle that is currently pressed; null if no Handle is pressed.
    private var mPressedHandle: Handle? = null

    // Mode indicating how/whether to show the guidelines; must be one of GUIDELINES_OFF, GUIDELINES_ON_TOUCH, GUIDELINES_ON.
    private var mGuidelinesMode = 1

    // Constructors ////////////////////////////////////////////////////////////////////////////////
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

        // If this View is not enabled, don't allow for touch interactions.
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

    /**
     * Gets the cropped image based on the current crop window.
     *
     * @return a new Bitmap representing the cropped image
     */
    val croppedImage: Bitmap?
        get() {

            // Implementation reference: http://stackoverflow.com/a/26930938/1068656
            val drawable = drawable
            if (drawable == null || drawable !is BitmapDrawable) {
                return null
            }

            // Get the original bitmap object.
            val originalBitmap = drawable.bitmap

            val h = bitmapRect.bottom - bitmapRect.top
            val w = bitmapRect.right - bitmapRect.left
            val widthFinal: Float = (Edge.width * originalBitmap.width) / w
            val heightFinal: Float = (Edge.height * originalBitmap.height) / h
            val leftFinal: Float = (Edge.LEFT.coordinate * originalBitmap.width) / w
            val topFinal: Float = (Edge.TOP.coordinate * originalBitmap.height) / h

            return Bitmap.createBitmap(originalBitmap, leftFinal.toInt(), topFinal.toInt(), widthFinal.toInt(), heightFinal.toInt())
        }

    /**
     * Gets the bounding rectangle of the bitmap within the ImageView.
     */
    private val bitmapRect: RectF
        get() {
            val drawable = drawable ?: return RectF()

            // Get image matrix values and place them in an array.
            val matrixValues = FloatArray(9)
            imageMatrix.getValues(matrixValues)

            // Extract the scale and translation values from the matrix.
            val scaleX = matrixValues[Matrix.MSCALE_X]
            val scaleY = matrixValues[Matrix.MSCALE_Y]
            val transX = matrixValues[Matrix.MTRANS_X]
            val transY = matrixValues[Matrix.MTRANS_Y]

            // Get the width and height of the original bitmap.
            val drawableIntrinsicWidth = drawable.intrinsicWidth
            val drawableIntrinsicHeight = drawable.intrinsicHeight

            // Calculate the dimensions as seen on screen.
            val drawableDisplayWidth = Math.round(drawableIntrinsicWidth * scaleX)
            val drawableDisplayHeight = Math.round(drawableIntrinsicHeight * scaleY)

            // Get the Rect of the displayed image within the ImageView.
            val left = 0f
            val top = 0f
            val right = (left + drawableDisplayWidth).coerceAtMost(width.toFloat())
            val bottom = (top + drawableDisplayHeight).coerceAtMost(height.toFloat())
            return RectF(left, top, right, bottom)
        }

    /**
     * Initialize the crop window by setting the proper [Edge] values.
     *
     *
     * If fixed aspect ratio is turned off, the initial crop window will be set to the displayed
     * image with 10% margin. If fixed aspect ratio is turned on, the initial crop window will
     * conform to the aspect ratio with at least one dimension maximized.
     */
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

        /*-
          -------------------------------------
          |                top                |
          -------------------------------------
          |      |                    |       |
          |      |                    |       |
          | left |                    | right |
          |      |                    |       |
          |      |                    |       |
          -------------------------------------
          |              bottom               |
          -------------------------------------
         */

        // Draw "top", "bottom", "left", then "right" quadrants according to diagram above.
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

    /**
     * Handles a [MotionEvent.ACTION_DOWN] event.
     *
     * @param x the x-coordinate of the down action
     * @param y the y-coordinate of the down action
     */
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

        // Calculate the offset of the touch point from the precise location of the handle.
        // Save these values in member variable 'mTouchOffset' so that we can maintain this offset as we drag the handle.
        if (mPressedHandle != null) {
            HandleUtil.getOffset(mPressedHandle!!, x, y, left, top, right, bottom, mTouchOffset)
            invalidate()
        }
    }

    /**
     * Handles a [MotionEvent.ACTION_UP] or [MotionEvent.ACTION_CANCEL] event.
     */
    private fun onActionUp() {
        if (mPressedHandle != null) {
            mPressedHandle = null
            invalidate()
        }
    }

    /**
     * Handles a [MotionEvent.ACTION_MOVE] event.
     *
     * @param x the x-coordinate of the move event
     * @param y the y-coordinate of the move event
     */
    private fun onActionMove(x: Float, y: Float) {
        var x = x
        var y = y
        if (mPressedHandle == null) {
            return
        }

        // Adjust the coordinates for the finger position's offset (i.e. the distance from the initial touch to the precise handle location).
        // We want to maintain the initial touch's distance to the pressed handle so that the crop window size does not "jump".
        x += mTouchOffset.x
        y += mTouchOffset.y

        // Calculate the new crop window size/position.
        mPressedHandle!!.updateCropWindow(x, y, mBitmapRect, mSnapRadius)
        invalidate()
    }

    companion object {
        // Private Constants ///////////////////////////////////////////////////////////////////////////
        private val TAG = CropImageView::class.java.name
        const val GUIDELINES_OFF = 0
        const val GUIDELINES_ON_TOUCH = 1
        const val GUIDELINES_ON = 2
    }
}
