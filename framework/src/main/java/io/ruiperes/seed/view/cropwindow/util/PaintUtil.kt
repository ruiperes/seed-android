package io.ruiperes.seed.view.cropwindow.util

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import io.ruiperes.seed.R


object PaintUtil {


    fun newBorderPaint(resources: Resources): Paint {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.color = Color.RED
        return paint
    }


    fun newGuidelinePaint(resources: Resources): Paint {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f
        paint.color = Color.WHITE
        return paint
    }


    fun newSurroundingAreaOverlayPaint(resources: Resources): Paint {
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = resources.getColor(R.color.semiBlack)
        return paint
    }


    fun newCornerPaint(resources: Resources): Paint {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.color = Color.BLUE
        return paint
    }
}
