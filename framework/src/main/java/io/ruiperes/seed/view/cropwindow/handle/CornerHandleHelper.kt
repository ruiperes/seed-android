package io.ruiperes.seed.view.cropwindow.handle

import android.graphics.RectF
import io.ruiperes.seed.view.cropwindow.edge.Edge

/**
 * HandleHelper class to handle corner Handles (i.e. top-left, top-right, bottom-left, and
 * bottom-right handles).
 */
internal class CornerHandleHelper  // Constructor /////////////////////////////////////////////////////////////////////////////////
    (
    horizontalEdge: Edge?,
    verticalEdge: Edge?
) : HandleHelper(horizontalEdge, verticalEdge) {
    // HandleHelper Methods ////////////////////////////////////////////////////////////////////////
    public override fun updateCropWindow(
        x: Float,
        y: Float,
        targetAspectRatio: Float,
        imageRect: RectF,
        snapRadius: Float
    ) {
        val activeEdges = getActiveEdges(x, y, targetAspectRatio)
        val primaryEdge =
            activeEdges!!.primary
        val secondaryEdge =
            activeEdges.secondary
        primaryEdge!!.adjustCoordinate(x, y, imageRect, snapRadius, targetAspectRatio)
        secondaryEdge!!.adjustCoordinate(targetAspectRatio)
        if (secondaryEdge.isOutsideMargin(imageRect, snapRadius)) {
            secondaryEdge.snapToRect(imageRect)
            primaryEdge.adjustCoordinate(targetAspectRatio)
        }
    }
}
