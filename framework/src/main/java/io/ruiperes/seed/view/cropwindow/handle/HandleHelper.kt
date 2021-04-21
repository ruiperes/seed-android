package io.ruiperes.seed.view.cropwindow.handle

import android.graphics.RectF
import io.ruiperes.seed.view.cropwindow.edge.Edge
import io.ruiperes.seed.view.cropwindow.util.AspectRatioUtil
import io.ruiperes.seed.view.cropwindow.edge.EdgePair


internal abstract class HandleHelper(
    private val mHorizontalEdge: Edge?,
    private val mVerticalEdge: Edge?
) {




    val activeEdges: EdgePair


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


    abstract fun updateCropWindow(
        x: Float,
        y: Float,
        targetAspectRatio: Float,
        imageRect: RectF,
        snapRadius: Float
    )


    fun getActiveEdges(
        x: Float,
        y: Float,
        targetAspectRatio: Float
    ): EdgePair {


        val potentialAspectRatio = getAspectRatio(x, y)


        if (potentialAspectRatio > targetAspectRatio) {
            activeEdges.primary = mVerticalEdge
            activeEdges.secondary = mHorizontalEdge
        } else {
            activeEdges.primary = mHorizontalEdge
            activeEdges.secondary = mVerticalEdge
        }
        return activeEdges
    }


    private fun getAspectRatio(x: Float, y: Float): Float {


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

        private const val UNFIXED_ASPECT_RATIO_CONSTANT = 1f
    }


    init {
        activeEdges = EdgePair(mHorizontalEdge, mVerticalEdge)
    }
}
