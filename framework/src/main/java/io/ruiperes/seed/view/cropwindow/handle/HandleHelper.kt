package io.ruiperes.seed.view.cropwindow.handle

import android.graphics.RectF
import io.ruiperes.seed.view.cropwindow.edge.Edge
import io.ruiperes.seed.view.cropwindow.util.AspectRatioUtil
import io.ruiperes.seed.view.cropwindow.edge.EdgePair

/**
 * Abstract helper class to handle operations on a crop window Handle.
 */
internal abstract class HandleHelper(
    private val mHorizontalEdge: Edge?,
    private val mVerticalEdge: Edge?
) {

    /**
     * Gets the Edges associated with this handle (i.e. the Edges that should be moved when this
     * handle is dragged). This is used when we are not maintaining the aspect ratio.
     *
     * @return the active edge as a pair (the pair may contain null values for the
     * `primary`, `secondary` or both fields)
     */
    // Save the Pair object as a member variable to avoid having to instantiate
    // a new Object every time getActiveEdges() is called.
    val activeEdges: EdgePair
    // Package-Private Methods /////////////////////////////////////////////////////////////////////
    /**
     * Updates the crop window by directly setting the Edge coordinates.
     *
     * @param x          the new x-coordinate of this handle
     * @param y          the new y-coordinate of this handle
     * @param imageRect  the bounding rectangle of the image
     * @param snapRadius the maximum distance (in pixels) at which the crop window should snap to
     * the image
     */
    open fun updateCropWindow(
        x: Float,
        y: Float,
        imageRect: RectF,
        snapRadius: Float
    ) {
        val activeEdges = activeEdges
        val primaryEdge =
            activeEdges.primary
        val secondaryEdge =
            activeEdges.secondary
        primaryEdge?.adjustCoordinate(
            x,
            y,
            imageRect,
            snapRadius,
            UNFIXED_ASPECT_RATIO_CONSTANT
        )
        secondaryEdge?.adjustCoordinate(
            x,
            y,
            imageRect,
            snapRadius,
            UNFIXED_ASPECT_RATIO_CONSTANT
        )
    }

    /**
     * Updates the crop window by directly setting the Edge coordinates; this method maintains a
     * given aspect ratio.
     *
     * @param x                 the new x-coordinate of this handle
     * @param y                 the new y-coordinate of this handle
     * @param targetAspectRatio the aspect ratio to maintain
     * @param imageRect         the bounding rectangle of the image
     * @param snapRadius        the maximum distance (in pixels) at which the crop window should
     * snap to the image
     */
    abstract fun updateCropWindow(
        x: Float,
        y: Float,
        targetAspectRatio: Float,
        imageRect: RectF,
        snapRadius: Float
    )

    /**
     * Gets the Edges associated with this handle as an ordered Pair. The `primary` Edge
     * in the pair is the determining side. This method is used when we need to maintain the aspect
     * ratio.
     *
     * @param x                 the x-coordinate of the touch point
     * @param y                 the y-coordinate of the touch point
     * @param targetAspectRatio the aspect ratio that we are maintaining
     *
     * @return the active edges as an ordered pair
     */
    fun getActiveEdges(
        x: Float,
        y: Float,
        targetAspectRatio: Float
    ): EdgePair {

        // Calculate the aspect ratio if this handle were dragged to the given x-y coordinate.
        val potentialAspectRatio = getAspectRatio(x, y)

        // If the touched point is wider than the aspect ratio, then x is the determining side. Else, y is the determining side.
        if (potentialAspectRatio > targetAspectRatio) {
            activeEdges.primary = mVerticalEdge
            activeEdges.secondary = mHorizontalEdge
        } else {
            activeEdges.primary = mHorizontalEdge
            activeEdges.secondary = mVerticalEdge
        }
        return activeEdges
    }
    // Private Methods /////////////////////////////////////////////////////////////////////////////
    /**
     * Gets the aspect ratio of the resulting crop window if this handle were dragged to the given
     * point.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     *
     * @return the aspect ratio
     */
    private fun getAspectRatio(x: Float, y: Float): Float {

        // Replace the active edge coordinate with the given touch coordinate.
        val left =
            if (mVerticalEdge == Edge.LEFT) x else Edge.LEFT.coordinate
        val top =
            if (mHorizontalEdge == Edge.TOP) y else Edge.TOP.coordinate
        val right =
            if (mVerticalEdge == Edge.RIGHT) x else Edge.RIGHT.coordinate
        val bottom =
            if (mHorizontalEdge == Edge.BOTTOM) y else Edge.BOTTOM.coordinate
        return AspectRatioUtil.calculateAspectRatio(left, top, right, bottom)
    }

    companion object {
        // Member Variables ////////////////////////////////////////////////////////
        private const val UNFIXED_ASPECT_RATIO_CONSTANT = 1f
    }
    // Constructor /////////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     *
     * @param horizontalEdge the horizontal edge associated with this handle; may be null
     * @param verticalEdge   the vertical edge associated with this handle; may be null
     */
    init {
        activeEdges = EdgePair(mHorizontalEdge, mVerticalEdge)
    }
}
