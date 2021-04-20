package io.ruiperes.seed.view.cropwindow.handle

import android.graphics.RectF
import io.ruiperes.seed.view.cropwindow.edge.Edge

/**
 * HandleHelper class to handle the center handle.
 */
internal class CenterHandleHelper  // Constructor /////////////////////////////////////////////////////////////////////////////////
    : HandleHelper(null, null) {
    // HandleHelper Methods ////////////////////////////////////////////////////////////////////////
    public override fun updateCropWindow(
        x: Float,
        y: Float,
        imageRect: RectF,
        snapRadius: Float
    ) {
        val left =
            Edge.LEFT.coordinate
        val top =
            Edge.TOP.coordinate
        val right =
            Edge.RIGHT.coordinate
        val bottom =
            Edge.BOTTOM.coordinate
        val currentCenterX = (left + right) / 2
        val currentCenterY = (top + bottom) / 2
        val offsetX = x - currentCenterX
        val offsetY = y - currentCenterY

        // Adjust the crop window.
        Edge.LEFT.offset(offsetX)
        Edge.TOP.offset(offsetY)
        Edge.RIGHT.offset(offsetX)
        Edge.BOTTOM.offset(offsetY)

        // Check if we have gone out of bounds on the sides, and fix.
        if (Edge.LEFT.isOutsideMargin(
                imageRect,
                snapRadius
            )
        ) {
            val offset =
                Edge.LEFT.snapToRect(imageRect)
            Edge.RIGHT.offset(offset)
        } else if (Edge.RIGHT.isOutsideMargin(
                imageRect,
                snapRadius
            )
        ) {
            val offset =
                Edge.RIGHT.snapToRect(imageRect)
            Edge.LEFT.offset(offset)
        }

        // Check if we have gone out of bounds on the top or bottom, and fix.
        if (Edge.TOP.isOutsideMargin(
                imageRect,
                snapRadius
            )
        ) {
            val offset =
                Edge.TOP.snapToRect(imageRect)
            Edge.BOTTOM.offset(offset)
        } else if (Edge.BOTTOM.isOutsideMargin(
                imageRect,
                snapRadius
            )
        ) {
            val offset =
                Edge.BOTTOM.snapToRect(imageRect)
            Edge.TOP.offset(offset)
        }
    }

    public override fun updateCropWindow(
        x: Float,
        y: Float,
        targetAspectRatio: Float,
        imageRect: RectF,
        snapRadius: Float
    ) {
        updateCropWindow(x, y, imageRect, snapRadius)
    }
}
