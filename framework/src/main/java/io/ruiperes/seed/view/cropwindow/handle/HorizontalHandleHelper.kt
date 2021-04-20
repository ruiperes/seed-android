package io.ruiperes.seed.view.cropwindow.handle

import android.graphics.RectF
import io.ruiperes.seed.view.cropwindow.edge.Edge
import io.ruiperes.seed.view.cropwindow.util.AspectRatioUtil

/**
 * Handle helper class to handle horizontal handles (i.e. top and bottom handles).
 */
internal class HorizontalHandleHelper // Constructor /////////////////////////////////////////////////////////////////////////////////
    (// Member Variables ////////////////////////////////////////////////////////////////////////////
    private val mEdge: Edge
) :
    HandleHelper(mEdge, null) {

    // HandleHelper Methods ////////////////////////////////////////////////////////////////////////
    public override fun updateCropWindow(
        x: Float,
        y: Float,
        targetAspectRatio: Float,
        imageRect: RectF,
        snapRadius: Float
    ) {

        // Adjust this Edge accordingly.
        mEdge.adjustCoordinate(x, y, imageRect, snapRadius, targetAspectRatio)
        var left =
            Edge.LEFT.coordinate
        var right =
            Edge.RIGHT.coordinate

        // After this Edge is moved, our crop window is now out of proportion.
        val targetWidth = AspectRatioUtil.calculateWidth(
            Edge.height,
            targetAspectRatio
        )

        // Adjust the crop window so that it maintains the given aspect ratio by
        // moving the adjacent edges symmetrically in or out.
        val difference: Float =
            targetWidth - Edge.width
        val halfDifference = difference / 2
        left -= halfDifference
        right += halfDifference
        Edge.LEFT.coordinate = left
        Edge.RIGHT.coordinate = right

        // Check if we have gone out of bounds on the sides, and fix.
        if (Edge.LEFT.isOutsideMargin(
                imageRect,
                snapRadius
            )
            && !mEdge.isNewRectangleOutOfBounds(
                Edge.LEFT,
                imageRect,
                targetAspectRatio
            )
        ) {
            val offset =
                Edge.LEFT.snapToRect(imageRect)
            Edge.RIGHT.offset(-offset)
            mEdge.adjustCoordinate(targetAspectRatio)
        }
        if (Edge.RIGHT.isOutsideMargin(
                imageRect,
                snapRadius
            )
            && !mEdge.isNewRectangleOutOfBounds(
                Edge.RIGHT,
                imageRect,
                targetAspectRatio
            )
        ) {
            val offset =
                Edge.RIGHT.snapToRect(imageRect)
            Edge.LEFT.offset(-offset)
            mEdge.adjustCoordinate(targetAspectRatio)
        }
    }

}
