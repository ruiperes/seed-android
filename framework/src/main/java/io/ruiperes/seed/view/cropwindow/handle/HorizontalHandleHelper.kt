package io.ruiperes.seed.view.cropwindow.handle

import android.graphics.RectF
import io.ruiperes.seed.view.cropwindow.edge.Edge
import io.ruiperes.seed.view.cropwindow.util.AspectRatioUtil


internal class HorizontalHandleHelper
    (
    private val mEdge: Edge
) :
    HandleHelper(mEdge, null) {


    public override fun updateCropWindow(
        x: Float,
        y: Float,
        targetAspectRatio: Float,
        imageRect: RectF,
        snapRadius: Float
    ) {


        mEdge.adjustCoordinate(x, y, imageRect, snapRadius, targetAspectRatio)
        var left =
            Edge.LEFT.coordinate
        var right =
            Edge.RIGHT.coordinate


        val targetWidth = AspectRatioUtil.calculateWidth(
            Edge.height,
            targetAspectRatio
        )



        val difference: Float =
            targetWidth - Edge.width
        val halfDifference = difference / 2
        left -= halfDifference
        right += halfDifference
        Edge.LEFT.coordinate = left
        Edge.RIGHT.coordinate = right


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
