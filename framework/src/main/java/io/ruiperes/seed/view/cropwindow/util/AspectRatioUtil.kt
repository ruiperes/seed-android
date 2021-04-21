package io.ruiperes.seed.view.cropwindow.util

import android.graphics.RectF


object AspectRatioUtil {

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


    fun calculateAspectRatio(rect: RectF): Float {
        return rect.width() / rect.height()
    }


    fun calculateLeft(
        top: Float,
        right: Float,
        bottom: Float,
        targetAspectRatio: Float
    ): Float {
        val height = bottom - top



        return right - targetAspectRatio * height
    }


    fun calculateTop(
        left: Float,
        right: Float,
        bottom: Float,
        targetAspectRatio: Float
    ): Float {
        val width = right - left




        return bottom - width / targetAspectRatio
    }


    fun calculateRight(
        left: Float,
        top: Float,
        bottom: Float,
        targetAspectRatio: Float
    ): Float {
        val height = bottom - top



        return targetAspectRatio * height + left
    }


    fun calculateBottom(
        left: Float,
        top: Float,
        right: Float,
        targetAspectRatio: Float
    ): Float {
        val width = right - left




        return width / targetAspectRatio + top
    }


    fun calculateWidth(height: Float, targetAspectRatio: Float): Float {
        return targetAspectRatio * height
    }


    fun calculateHeight(width: Float, targetAspectRatio: Float): Float {
        return width / targetAspectRatio
    }
}
