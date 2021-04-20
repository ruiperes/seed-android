package io.ruiperes.seed.view.cropwindow.edge

import android.graphics.RectF
import io.ruiperes.seed.view.cropwindow.util.AspectRatioUtil

/**
 * Enum representing an edge in the crop window.
 */
enum class Edge {
    LEFT, TOP, RIGHT, BOTTOM;

    /**
     * Gets the coordinate of the Edge
     *
     * @return the Edge coordinate (x-coordinate for LEFT and RIGHT Edges and the y-coordinate for
     * TOP and BOTTOM edges)
     */
    /**
     * Sets the coordinate of the Edge. The coordinate will represent the x-coordinate for LEFT and
     * RIGHT Edges and the y-coordinate for TOP and BOTTOM edges.
     *
     * @param coordinate the position of the edge
     */
    // Member Variables ////////////////////////////////////////////////////////////////////////////
    // The coordinate value of this edge.
    // This will be the x-coordinate for LEFT and RIGHT edges and the y-coordinate for TOP and BOTTOM edges.
    var coordinate = 0f
    // Public Methods //////////////////////////////////////////////////////////////////////////////

    /**
     * Add the given number of pixels to the current coordinate position of this Edge.
     *
     * @param distance the number of pixels to add
     */
    fun offset(distance: Float) {
        coordinate += distance
    }

    /**
     * Sets the Edge to the given x-y coordinate but also adjusting for snapping to the image bounds
     * and parent view border constraints.
     *
     * @param x               the x-coordinate
     * @param y               the y-coordinate
     * @param imageRect       the bounding rectangle of the image
     * @param imageSnapRadius the radius (in pixels) at which the edge should snap to the image
     */
    fun adjustCoordinate(
        x: Float,
        y: Float,
        imageRect: RectF,
        imageSnapRadius: Float,
        aspectRatio: Float
    ) {
        when (this) {
            LEFT -> coordinate =
                adjustLeft(
                    x,
                    imageRect,
                    imageSnapRadius,
                    aspectRatio
                )
            TOP -> coordinate =
                adjustTop(
                    y,
                    imageRect,
                    imageSnapRadius,
                    aspectRatio
                )
            RIGHT -> coordinate =
                adjustRight(
                    x,
                    imageRect,
                    imageSnapRadius,
                    aspectRatio
                )
            BOTTOM -> coordinate =
                adjustBottom(
                    y,
                    imageRect,
                    imageSnapRadius,
                    aspectRatio
                )
        }
    }

    /**
     * Adjusts this Edge position such that the resulting window will have the given aspect ratio.
     *
     * @param aspectRatio the aspect ratio to achieve
     */
    fun adjustCoordinate(aspectRatio: Float) {
        val left =
            coordinate
        val top =
            coordinate
        val right =
            coordinate
        val bottom =
            coordinate
        when (this) {
            LEFT -> coordinate =
                AspectRatioUtil.calculateLeft(top, right, bottom, aspectRatio)
            TOP -> coordinate =
                AspectRatioUtil.calculateTop(left, right, bottom, aspectRatio)
            RIGHT -> coordinate =
                AspectRatioUtil.calculateRight(left, top, bottom, aspectRatio)
            BOTTOM -> coordinate =
                AspectRatioUtil.calculateBottom(left, top, right, aspectRatio)
        }
    }

    /**
     * Returns whether or not you can re-scale the image based on whether any edge would be out of
     * bounds. Checks all the edges for a possibility of jumping out of bounds.
     *
     * @param edge        the Edge that is about to be expanded
     * @param imageRect   the rectangle of the picture
     * @param aspectRatio the desired aspectRatio of the picture
     *
     * @return whether or not the new image would be out of bounds.
     */
    fun isNewRectangleOutOfBounds(
        edge: Edge,
        imageRect: RectF,
        aspectRatio: Float
    ): Boolean {
        val offset = edge.snapOffset(imageRect)
        when (this) {
            LEFT -> if (edge == TOP) {
                val top = imageRect.top
                val bottom =
                    coordinate - offset
                val right =
                    coordinate
                val left =
                    AspectRatioUtil.calculateLeft(top, right, bottom, aspectRatio)
                return isOutOfBounds(top, left, bottom, right, imageRect)
            } else if (edge == BOTTOM) {
                val bottom = imageRect.bottom
                val top =
                    coordinate - offset
                val right =
                    coordinate
                val left =
                    AspectRatioUtil.calculateLeft(top, right, bottom, aspectRatio)
                return isOutOfBounds(top, left, bottom, right, imageRect)
            }
            TOP -> if (edge == LEFT) {
                val left = imageRect.left
                val right =
                    coordinate - offset
                val bottom =
                    coordinate
                val top =
                    AspectRatioUtil.calculateTop(left, right, bottom, aspectRatio)
                return isOutOfBounds(top, left, bottom, right, imageRect)
            } else if (edge == RIGHT) {
                val right = imageRect.right
                val left =
                    coordinate - offset
                val bottom =
                    coordinate
                val top =
                    AspectRatioUtil.calculateTop(left, right, bottom, aspectRatio)
                return isOutOfBounds(top, left, bottom, right, imageRect)
            }
            RIGHT -> if (edge == TOP) {
                val top = imageRect.top
                val bottom =
                    coordinate - offset
                val left =
                    coordinate
                val right =
                    AspectRatioUtil.calculateRight(left, top, bottom, aspectRatio)
                return isOutOfBounds(top, left, bottom, right, imageRect)
            } else if (edge == BOTTOM) {
                val bottom = imageRect.bottom
                val top =
                    coordinate - offset
                val left =
                    coordinate
                val right =
                    AspectRatioUtil.calculateRight(left, top, bottom, aspectRatio)
                return isOutOfBounds(top, left, bottom, right, imageRect)
            }
            BOTTOM -> if (edge == LEFT) {
                val left = imageRect.left
                val right =
                    coordinate - offset
                val top =
                    coordinate
                val bottom =
                    AspectRatioUtil.calculateBottom(left, top, right, aspectRatio)
                return isOutOfBounds(top, left, bottom, right, imageRect)
            } else if (edge == RIGHT) {
                val right = imageRect.right
                val left =
                    coordinate - offset
                val top =
                    coordinate
                val bottom =
                    AspectRatioUtil.calculateBottom(left, top, right, aspectRatio)
                return isOutOfBounds(top, left, bottom, right, imageRect)
            }
        }
        return true
    }

    /**
     * Returns whether the new rectangle would be out of bounds.
     *
     * @param imageRect the Image to be compared with
     *
     * @return whether it would be out of bounds
     */
    private fun isOutOfBounds(
        top: Float,
        left: Float,
        bottom: Float,
        right: Float,
        imageRect: RectF
    ): Boolean {
        return top < imageRect.top || left < imageRect.left || bottom > imageRect.bottom || right > imageRect.right
    }

    /**
     * Snap this Edge to the given image boundaries.
     *
     * @param imageRect the bounding rectangle of the image to snap to
     *
     * @return the amount (in pixels) that this coordinate was changed (i.e. the new coordinate
     * minus the old coordinate value)
     */
    fun snapToRect(imageRect: RectF): Float {
        val oldCoordinate = coordinate
        when (this) {
            LEFT -> coordinate =
                imageRect.left
            TOP -> coordinate =
                imageRect.top
            RIGHT -> coordinate =
                imageRect.right
            BOTTOM -> coordinate =
                imageRect.bottom
        }
        return coordinate - oldCoordinate
    }

    /**
     * Returns the potential snap offset of snapToRect, without changing the coordinate.
     *
     * @param imageRect the bounding rectangle of the image to snap to
     *
     * @return the amount (in pixels) that this coordinate was changed (i.e. the new coordinate
     * minus the old coordinate value)
     */
    fun snapOffset(imageRect: RectF): Float {
        val oldCoordinate = coordinate
        val newCoordinate: Float
        newCoordinate = when (this) {
            LEFT -> imageRect.left
            TOP -> imageRect.top
            RIGHT -> imageRect.right
            else -> imageRect.bottom
        }
        return newCoordinate - oldCoordinate
    }

    /**
     * Determines if this Edge is outside the inner margins of the given bounding rectangle. The
     * margins come inside the actual frame by SNAPRADIUS amount; therefore, determines if the point
     * is outside the inner "margin" frame.
     */
    fun isOutsideMargin(rect: RectF, margin: Float): Boolean {
        val result: Boolean
        result = when (this) {
            LEFT -> coordinate - rect.left < margin
            TOP -> coordinate - rect.top < margin
            RIGHT -> rect.right - coordinate < margin
            else -> rect.bottom - coordinate < margin
        }
        return result
    }

    companion object {
        // Private Constants ///////////////////////////////////////////////////////////////////////////
        // Minimum distance in pixels that one edge can get to its opposing edge.
        // This is an arbitrary value that simply prevents the crop window from becoming too small.
        const val MIN_CROP_LENGTH_PX = 40

        /**
         * Gets the current width of the crop window.
         */
        val width: Float
            get() = RIGHT.coordinate - LEFT.coordinate

        /**
         * Gets the current height of the crop window.
         */
        val height: Float
            get() = BOTTOM.coordinate - TOP.coordinate
        // Private Methods /////////////////////////////////////////////////////////////////////////////
        /**
         * Get the resulting x-position of the left edge of the crop window given the handle's position
         * and the image's bounding box and snap radius.
         *
         * @param x               the x-position that the left edge is dragged to
         * @param imageRect       the bounding box of the image that is being cropped
         * @param imageSnapRadius the snap distance to the image edge (in pixels)
         *
         * @return the actual x-position of the left edge
         */
        private fun adjustLeft(
            x: Float,
            imageRect: RectF,
            imageSnapRadius: Float,
            aspectRatio: Float
        ): Float {
            val resultX: Float
            if (x - imageRect.left < imageSnapRadius) {
                resultX = imageRect.left
            } else {

                // Select the minimum of the three possible values to use
                var resultXHoriz = Float.POSITIVE_INFINITY
                var resultXVert = Float.POSITIVE_INFINITY

                // Checks if the window is too small horizontally
                if (x >= RIGHT.coordinate - MIN_CROP_LENGTH_PX) {
                    resultXHoriz =
                        RIGHT.coordinate - MIN_CROP_LENGTH_PX
                }
                // Checks if the window is too small vertically
                if ((RIGHT.coordinate - x) / aspectRatio <= MIN_CROP_LENGTH_PX) {
                    resultXVert =
                        RIGHT.coordinate - MIN_CROP_LENGTH_PX * aspectRatio
                }
                resultX = Math.min(x, Math.min(resultXHoriz, resultXVert))
            }
            return resultX
        }

        /**
         * Get the resulting x-position of the right edge of the crop window given the handle's position
         * and the image's bounding box and snap radius.
         *
         * @param x               the x-position that the right edge is dragged to
         * @param imageRect       the bounding box of the image that is being cropped
         * @param imageSnapRadius the snap distance to the image edge (in pixels)
         *
         * @return the actual x-position of the right edge
         */
        private fun adjustRight(
            x: Float,
            imageRect: RectF,
            imageSnapRadius: Float,
            aspectRatio: Float
        ): Float {
            val resultX: Float

            // If close to the edge...
            if (imageRect.right - x < imageSnapRadius) {
                resultX = imageRect.right
            } else {

                // Select the maximum of the three possible values to use
                var resultXHoriz = Float.NEGATIVE_INFINITY
                var resultXVert = Float.NEGATIVE_INFINITY

                // Checks if the window is too small horizontally
                if (x <= LEFT.coordinate + MIN_CROP_LENGTH_PX) {
                    resultXHoriz =
                        LEFT.coordinate + MIN_CROP_LENGTH_PX
                }
                // Checks if the window is too small vertically
                if ((x - LEFT.coordinate) / aspectRatio <= MIN_CROP_LENGTH_PX) {
                    resultXVert =
                        LEFT.coordinate + MIN_CROP_LENGTH_PX * aspectRatio
                }
                resultX = Math.max(x, Math.max(resultXHoriz, resultXVert))
            }
            return resultX
        }

        /**
         * Get the resulting y-position of the top edge of the crop window given the handle's position
         * and the image's bounding box and snap radius.
         *
         * @param y               the x-position that the top edge is dragged to
         * @param imageRect       the bounding box of the image that is being cropped
         * @param imageSnapRadius the snap distance to the image edge (in pixels)
         *
         * @return the actual y-position of the top edge
         */
        private fun adjustTop(
            y: Float,
            imageRect: RectF,
            imageSnapRadius: Float,
            aspectRatio: Float
        ): Float {
            val resultY: Float
            if (y - imageRect.top < imageSnapRadius) {
                resultY = imageRect.top
            } else {

                // Select the minimum of the three possible values to use
                var resultYVert = Float.POSITIVE_INFINITY
                var resultYHoriz = Float.POSITIVE_INFINITY

                // Checks if the window is too small vertically
                if (y >= BOTTOM.coordinate - MIN_CROP_LENGTH_PX) resultYHoriz =
                    BOTTOM.coordinate - MIN_CROP_LENGTH_PX

                // Checks if the window is too small horizontally
                if ((BOTTOM.coordinate - y) * aspectRatio <= MIN_CROP_LENGTH_PX) resultYVert =
                    BOTTOM.coordinate - MIN_CROP_LENGTH_PX / aspectRatio
                resultY = Math.min(y, Math.min(resultYHoriz, resultYVert))
            }
            return resultY
        }

        /**
         * Get the resulting y-position of the bottom edge of the crop window given the handle's
         * position and the image's bounding box and snap radius.
         *
         * @param y               the x-position that the bottom edge is dragged to
         * @param imageRect       the bounding box of the image that is being cropped
         * @param imageSnapRadius the snap distance to the image edge (in pixels)
         *
         * @return the actual y-position of the bottom edge
         */
        private fun adjustBottom(
            y: Float,
            imageRect: RectF,
            imageSnapRadius: Float,
            aspectRatio: Float
        ): Float {
            val resultY: Float
            if (imageRect.bottom - y < imageSnapRadius) {
                resultY = imageRect.bottom
            } else {

                // Select the maximum of the three possible values to use
                var resultYVert = Float.NEGATIVE_INFINITY
                var resultYHoriz = Float.NEGATIVE_INFINITY

                // Checks if the window is too small vertically
                if (y <= TOP.coordinate + MIN_CROP_LENGTH_PX) {
                    resultYVert =
                        TOP.coordinate + MIN_CROP_LENGTH_PX
                }
                // Checks if the window is too small horizontally
                if ((y - TOP.coordinate) * aspectRatio <= MIN_CROP_LENGTH_PX) {
                    resultYHoriz =
                        TOP.coordinate + MIN_CROP_LENGTH_PX / aspectRatio
                }
                resultY = Math.max(y, Math.max(resultYHoriz, resultYVert))
            }
            return resultY
        }
    }
}
