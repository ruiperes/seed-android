package io.ruiperes.seed.view.cropwindow.handle

import android.graphics.RectF
import io.ruiperes.seed.view.cropwindow.edge.Edge
import io.ruiperes.seed.view.cropwindow.util.AspectRatioUtil


internal class VerticalHandleHelper
    (
    private val mEdge: Edge
) :
    HandleHelper(null, mEdge) {


    public override fun updateCropWindow(
        x: Float,
        y: Float,
        targetAspectRatio: Float,
        imageRect: RectF,
        snapRadius: Float
    ) {


        mEdge.adjustCoordinate(x, y, imageRect, snapRadius, targetAspectRatio)
        var top =
            Edge.TOP.coordinate
        var bottom =
            Edge.BOTTOM.coordinate


        val targetHeight = AspectRatioUtil.calculateHeight(
            Edge.width,
            targetAspectRatio
        )



        val difference: Float =
            targetHeight - Edge.height
        val halfDifference = difference / 2
        top -= halfDifference
        bottom += halfDifference
        Edge.TOP.coordinate = top
        Edge.BOTTOM.coordinate = bottom


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
