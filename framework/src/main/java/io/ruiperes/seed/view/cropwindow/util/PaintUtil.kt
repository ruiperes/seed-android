package io.ruiperes.seed.view.cropwindow.util/*
 * Copyright 2013, Edmodo, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License.
 * You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
*/

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import io.ruiperes.seed.R

/**
 * Utility class for handling all of the Paint used to draw the CropOverlayView.
 */
object PaintUtil {
    // Public Methods //////////////////////////////////////////////////////////
    /**
     * Creates the Paint object for drawing the crop window border.
     */
    fun newBorderPaint(resources: Resources): Paint {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.color = Color.RED
        return paint
    }

    /**
     * Creates the Paint object for drawing the crop window guidelines.
     */
    fun newGuidelinePaint(resources: Resources): Paint {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f
        paint.color = Color.WHITE
        return paint
    }

    /**
     * Creates the Paint object for drawing the translucent overlay outside the crop window.
     *
     * @return the new Paint object
     */
    fun newSurroundingAreaOverlayPaint(resources: Resources): Paint {
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = resources.getColor(R.color.semiBlack)
        return paint
    }

    /**
     * Creates the Paint object for drawing the corners of the border
     */
    fun newCornerPaint(resources: Resources): Paint {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.color = Color.BLUE
        return paint
    }
}
