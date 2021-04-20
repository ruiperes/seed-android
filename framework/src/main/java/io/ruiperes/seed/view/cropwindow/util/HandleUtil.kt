package io.ruiperes.seed.view.cropwindow.util

import android.graphics.PointF
import io.ruiperes.seed.view.cropwindow.handle.Handle

/**
 * Utility class to perform basic operations with Handles.
 */
object HandleUtil {
    // Public Methods //////////////////////////////////////////////////////////////////////////////
    /**
     * Determines which, if any, of the handles are pressed given the touch coordinates, the
     * bounding box, and the touch radius.
     *
     * @param x            the x-coordinate of the touch point
     * @param y            the y-coordinate of the touch point
     * @param left         the x-coordinate of the left bound
     * @param top          the y-coordinate of the top bound
     * @param right        the x-coordinate of the right bound
     * @param bottom       the y-coordinate of the bottom bound
     * @param targetRadius the target radius in pixels
     *
     * @return the Handle that was pressed; null if no Handle was pressed
     */
    fun getPressedHandle(
        x: Float,
        y: Float,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        targetRadius: Float
    ): Handle? {

        // Find the closest corner handle to the touch point.
        // If the touch point is in the target zone of this closest handle, then this is the pressed handle.
        // Else, check if any of the edges are in the target zone of the touch point.
        // Else, check if the touch point is within the crop window bounds; if so, then choose the center handle.
        var closestHandle: Handle? = null
        var closestDistance = Float.POSITIVE_INFINITY
        val distanceToTopLeft = MathUtil.calculateDistance(x, y, left, top)
        if (distanceToTopLeft < closestDistance) {
            closestDistance = distanceToTopLeft
            closestHandle = Handle.TOP_LEFT
        }
        val distanceToTopRight = MathUtil.calculateDistance(x, y, right, top)
        if (distanceToTopRight < closestDistance) {
            closestDistance = distanceToTopRight
            closestHandle = Handle.TOP_RIGHT
        }
        val distanceToBottomLeft = MathUtil.calculateDistance(x, y, left, bottom)
        if (distanceToBottomLeft < closestDistance) {
            closestDistance = distanceToBottomLeft
            closestHandle = Handle.BOTTOM_LEFT
        }
        val distanceToBottomRight = MathUtil.calculateDistance(x, y, right, bottom)
        if (distanceToBottomRight < closestDistance) {
            closestDistance = distanceToBottomRight
            closestHandle = Handle.BOTTOM_RIGHT
        }
        if (closestDistance <= targetRadius) {
            return closestHandle
        }

        // If we get to this point, none of the corner handles were in the touch target zone, so then we check the edges.
        if (isInHorizontalTargetZone(x, y, left, right, top, targetRadius)) {
            return Handle.TOP
        } else if (isInHorizontalTargetZone(x, y, left, right, bottom, targetRadius)) {
            return Handle.BOTTOM
        } else if (isInVerticalTargetZone(x, y, left, top, bottom, targetRadius)) {
            return Handle.LEFT
        } else if (isInVerticalTargetZone(x, y, right, top, bottom, targetRadius)) {
            return Handle.RIGHT
        }

        // If we get to this point, none of the corners or edges are in the touch target zone.
        // Check to see if the touch point is within the bounds of the crop window. If so, choose the center handle.
        return if (isWithinBounds(x, y, left, top, right, bottom)) {
            Handle.CENTER
        } else null
    }

    /**
     * Calculates the offset of the touch point from the precise location of the specified handle.
     *
     *
     * The offset will be returned in the 'touchOffsetOutput' parameter; the x-offset will be the
     * first value and the y-offset will be the second value.
     */
    fun getOffset(
        handle: Handle,
        x: Float,
        y: Float,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        touchOffsetOutput: PointF
    ) {
        var touchOffsetX = 0f
        var touchOffsetY = 0f
        when (handle) {
            Handle.TOP_LEFT -> {
                touchOffsetX = left - x
                touchOffsetY = top - y
            }
            Handle.TOP_RIGHT -> {
                touchOffsetX = right - x
                touchOffsetY = top - y
            }
            Handle.BOTTOM_LEFT -> {
                touchOffsetX = left - x
                touchOffsetY = bottom - y
            }
            Handle.BOTTOM_RIGHT -> {
                touchOffsetX = right - x
                touchOffsetY = bottom - y
            }
            Handle.LEFT -> {
                touchOffsetX = left - x
                touchOffsetY = 0f
            }
            Handle.TOP -> {
                touchOffsetX = 0f
                touchOffsetY = top - y
            }
            Handle.RIGHT -> {
                touchOffsetX = right - x
                touchOffsetY = 0f
            }
            Handle.BOTTOM -> {
                touchOffsetX = 0f
                touchOffsetY = bottom - y
            }
            Handle.CENTER -> {
                val centerX = (right + left) / 2
                val centerY = (top + bottom) / 2
                touchOffsetX = centerX - x
                touchOffsetY = centerY - y
            }
        }
        touchOffsetOutput.x = touchOffsetX
        touchOffsetOutput.y = touchOffsetY
    }
    // Private Methods /////////////////////////////////////////////////////////////////////////////
    /**
     * Determines if the specified coordinate is in the target touch zone for a horizontal bar
     * handle.
     *
     * @param x            the x-coordinate of the touch point
     * @param y            the y-coordinate of the touch point
     * @param handleXStart the left x-coordinate of the horizontal bar handle
     * @param handleXEnd   the right x-coordinate of the horizontal bar handle
     * @param handleY      the y-coordinate of the horizontal bar handle
     * @param targetRadius the target radius in pixels
     *
     * @return true if the touch point is in the target touch zone; false otherwise
     */
    private fun isInHorizontalTargetZone(
        x: Float,
        y: Float,
        handleXStart: Float,
        handleXEnd: Float,
        handleY: Float,
        targetRadius: Float
    ): Boolean {
        return x > handleXStart && x < handleXEnd && Math.abs(y - handleY) <= targetRadius
    }

    /**
     * Determines if the specified coordinate is in the target touch zone for a vertical bar
     * handle.
     *
     * @param x            the x-coordinate of the touch point
     * @param y            the y-coordinate of the touch point
     * @param handleX      the x-coordinate of the vertical bar handle
     * @param handleYStart the top y-coordinate of the vertical bar handle
     * @param handleYEnd   the bottom y-coordinate of the vertical bar handle
     * @param targetRadius the target radius in pixels
     *
     * @return true if the touch point is in the target touch zone; false otherwise
     */
    private fun isInVerticalTargetZone(
        x: Float,
        y: Float,
        handleX: Float,
        handleYStart: Float,
        handleYEnd: Float,
        targetRadius: Float
    ): Boolean {
        return Math.abs(x - handleX) <= targetRadius && y > handleYStart && y < handleYEnd
    }

    private fun isWithinBounds(
        x: Float,
        y: Float,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float
    ): Boolean {
        return x >= left && x <= right && y >= top && y <= bottom
    }
}
