package io.ruiperes.seed.view.cropwindow.util

import android.graphics.RectF

/**
 * Utility class for handling calculations involving a fixed aspect ratio.
 */
object AspectRatioUtil {
    /**
     * Calculates the aspect ratio given a rectangle.
     */
    fun calculateAspectRatio(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float
    ): Float {
        val width = right - left
        val height = bottom - top
        return width / height
    }

    /**
     * Calculates the aspect ratio given a rectangle.
     */
    fun calculateAspectRatio(rect: RectF): Float {
        return rect.width() / rect.height()
    }

    /**
     * Calculates the x-coordinate of the left edge given the other sides of the rectangle and an
     * aspect ratio.
     */
    fun calculateLeft(
        top: Float,
        right: Float,
        bottom: Float,
        targetAspectRatio: Float
    ): Float {
        val height = bottom - top
        // targetAspectRatio = width / height
        // width = targetAspectRatio * height
        // right - left = targetAspectRatio * height
        return right - targetAspectRatio * height
    }

    /**
     * Calculates the y-coordinate of the top edge given the other sides of the rectangle and an
     * aspect ratio.
     */
    fun calculateTop(
        left: Float,
        right: Float,
        bottom: Float,
        targetAspectRatio: Float
    ): Float {
        val width = right - left
        // targetAspectRatio = width / height
        // width = targetAspectRatio * height
        // height = width / targetAspectRatio
        // bottom - top = width / targetAspectRatio
        return bottom - width / targetAspectRatio
    }

    /**
     * Calculates the x-coordinate of the right edge given the other sides of the rectangle and an
     * aspect ratio.
     */
    fun calculateRight(
        left: Float,
        top: Float,
        bottom: Float,
        targetAspectRatio: Float
    ): Float {
        val height = bottom - top
        // targetAspectRatio = width / height
        // width = targetAspectRatio * height
        // right - left = targetAspectRatio * height
        return targetAspectRatio * height + left
    }

    /**
     * Calculates the y-coordinate of the bottom edge given the other sides of the rectangle and an
     * aspect ratio.
     */
    fun calculateBottom(
        left: Float,
        top: Float,
        right: Float,
        targetAspectRatio: Float
    ): Float {
        val width = right - left
        // targetAspectRatio = width / height
        // width = targetAspectRatio * height
        // height = width / targetAspectRatio
        // bottom - top = width / targetAspectRatio
        return width / targetAspectRatio + top
    }

    /**
     * Calculates the width of a rectangle given the top and bottom edges and an aspect ratio.
     */
    fun calculateWidth(height: Float, targetAspectRatio: Float): Float {
        return targetAspectRatio * height
    }

    /**
     * Calculates the height of a rectangle given the left and right edges and an aspect ratio.
     */
    fun calculateHeight(width: Float, targetAspectRatio: Float): Float {
        return width / targetAspectRatio
    }
}
