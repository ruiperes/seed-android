package io.ruiperes.seed.view.cropwindow.handle

import android.graphics.RectF
import io.ruiperes.seed.view.cropwindow.edge.Edge
import io.ruiperes.seed.view.cropwindow.util.AspectRatioUtil

/**
 * HandleHelper class to handle vertical handles (i.e. left and right handles).
 */
internal class VerticalHandleHelper // Constructor /////////////////////////////////////////////////////////////////////////////////
    (// Member Variables ////////////////////////////////////////////////////////////////////////////
    private val mEdge: Edge
) :
    HandleHelper(null, mEdge) {

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
        var top =
            Edge.TOP.coordinate
        var bottom =
            Edge.BOTTOM.coordinate

        // After this Edge is moved, our crop window is now out of proportion.
        val targetHeight = AspectRatioUtil.calculateHeight(
            Edge.width,
            targetAspectRatio
        )

        // Adjust the crop window so that it maintains the given aspect ratio by
        // moving the adjacent edges symmetrically in or out.
        val difference: Float =
            targetHeight - Edge.height
        val halfDifference = difference / 2
        top -= halfDifference
        bottom += halfDifference
        Edge.TOP.coordinate = top
        Edge.BOTTOM.coordinate = bottom

        // Check if we have gone out of bounds on the top or bottom, and fix.
        if (Edge.TOP.isOutsideMargin(
                imageRect,
                snapRadius
            )
            && !mEdge.isNewRectangleOutOfBounds(
                Edge.TOP,
                imageRect,
                targetAspectRatio
            )
        ) {
            val offset =
                Edge.TOP.snapToRect(imageRect)
            Edge.BOTTOM.offset(-offset)
            mEdge.adjustCoordinate(targetAspectRatio)
        }
        if (Edge.BOTTOM.isOutsideMargin(
                imageRect,
                snapRadius
            )
            && !mEdge.isNewRectangleOutOfBounds(
                Edge.BOTTOM,
                imageRect,
                targetAspectRatio
            )
        ) {
            val offset =
                Edge.BOTTOM.snapToRect(imageRect)
            Edge.TOP.offset(-offset)
            mEdge.adjustCoordinate(targetAspectRatio)
        }
    }

}
