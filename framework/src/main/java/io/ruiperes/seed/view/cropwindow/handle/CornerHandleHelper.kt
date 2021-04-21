package io.ruiperes.seed.view.cropwindow.handle

import android.graphics.RectF
import io.ruiperes.seed.view.cropwindow.edge.Edge


internal class CornerHandleHelper
    (
    horizontalEdge: Edge?,
    verticalEdge: Edge?
) : HandleHelper(horizontalEdge, verticalEdge) {

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
