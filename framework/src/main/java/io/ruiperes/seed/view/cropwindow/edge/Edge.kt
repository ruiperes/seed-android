package io.ruiperes.seed.view.cropwindow.edge

import android.graphics.RectF
import io.ruiperes.seed.view.cropwindow.util.AspectRatioUtil

enum class Edge {
    LEFT, TOP, RIGHT, BOTTOM;

    var coordinate = 0f

    fun offset(distance: Float) {
        coordinate += distance
    }

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

    private fun isOutOfBounds(
        top: Float,
        left: Float,
        bottom: Float,
        right: Float,
        imageRect: RectF
    ): Boolean {
        return top < imageRect.top || left < imageRect.left || bottom > imageRect.bottom || right > imageRect.right
    }

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

    fun snapOffset(imageRect: RectF): Float {
        val oldCoordinate = coordinate
        val newCoordinate: Float = when (this) {
            LEFT -> imageRect.left
            TOP -> imageRect.top
            RIGHT -> imageRect.right
            else -> imageRect.bottom
        }
        return newCoordinate - oldCoordinate
    }

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
        const val MIN_CROP_LENGTH_PX = 40

        val width: Float
            get() = RIGHT.coordinate - LEFT.coordinate

        val height: Float
            get() = BOTTOM.coordinate - TOP.coordinate

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


                var resultXHoriz = Float.POSITIVE_INFINITY
                var resultXVert = Float.POSITIVE_INFINITY


                if (x >= RIGHT.coordinate - MIN_CROP_LENGTH_PX) {
                    resultXHoriz =
                        RIGHT.coordinate - MIN_CROP_LENGTH_PX
                }

                if ((RIGHT.coordinate - x) / aspectRatio <= MIN_CROP_LENGTH_PX) {
                    resultXVert =
                        RIGHT.coordinate - MIN_CROP_LENGTH_PX * aspectRatio
                }
                resultX = Math.min(x, Math.min(resultXHoriz, resultXVert))
            }
            return resultX
        }

        private fun adjustRight(
            x: Float,
            imageRect: RectF,
            imageSnapRadius: Float,
            aspectRatio: Float
        ): Float {
            val resultX: Float


            if (imageRect.right - x < imageSnapRadius) {
                resultX = imageRect.right
            } else {


                var resultXHoriz = Float.NEGATIVE_INFINITY
                var resultXVert = Float.NEGATIVE_INFINITY


                if (x <= LEFT.coordinate + MIN_CROP_LENGTH_PX) {
                    resultXHoriz =
                        LEFT.coordinate + MIN_CROP_LENGTH_PX
                }

                if ((x - LEFT.coordinate) / aspectRatio <= MIN_CROP_LENGTH_PX) {
                    resultXVert =
                        LEFT.coordinate + MIN_CROP_LENGTH_PX * aspectRatio
                }
                resultX = Math.max(x, Math.max(resultXHoriz, resultXVert))
            }
            return resultX
        }

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


                var resultYVert = Float.POSITIVE_INFINITY
                var resultYHoriz = Float.POSITIVE_INFINITY


                if (y >= BOTTOM.coordinate - MIN_CROP_LENGTH_PX) resultYHoriz =
                    BOTTOM.coordinate - MIN_CROP_LENGTH_PX


                if ((BOTTOM.coordinate - y) * aspectRatio <= MIN_CROP_LENGTH_PX) resultYVert =
                    BOTTOM.coordinate - MIN_CROP_LENGTH_PX / aspectRatio
                resultY = Math.min(y, Math.min(resultYHoriz, resultYVert))
            }
            return resultY
        }

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


                var resultYVert = Float.NEGATIVE_INFINITY
                var resultYHoriz = Float.NEGATIVE_INFINITY


                if (y <= TOP.coordinate + MIN_CROP_LENGTH_PX) {
                    resultYVert =
                        TOP.coordinate + MIN_CROP_LENGTH_PX
                }

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
