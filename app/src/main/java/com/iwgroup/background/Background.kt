package com.iwgroup.background

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.core.content.res.ResourcesCompat

class Background(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRectFromDrawable(
            _width = 180.toPx(),
            _height = 110.toPx(),
            angleRadius = 8.toPx().toFloat(),
            alpha = 50,
            drawableRes = R.drawable.ic__021
        )
    }

    private fun Canvas.drawRoundRectFromDrawable(
        _width: Int,
        _height: Int,
        angleRadius: Float,
        @IntRange(from = 0, to = 100) alpha: Int,
        @DrawableRes drawableRes: Int
    ) {
        val path = Path()
        val bitmap = context.getBitmap(drawableRes, _width, _height)?.makeTransparent(alpha)
        val rect = Rect(0, 0, _width, _height)
        val rectF = RectF(rect)
        path.addRoundRect(rectF, angleRadius, angleRadius, Path.Direction.CW)
        clipPath(path)
        bitmap?.let {
            drawBitmap(it, rect, rect, Paint(Paint.ANTI_ALIAS_FLAG))
        }
    }


}
fun Int.toPx(): Int = (toFloat() * Resources.getSystem().displayMetrics.density).toInt()

fun Context.getBitmap(id: Int, _width: Int, _height: Int): Bitmap? {
    ResourcesCompat.getDrawable(resources, id, theme)?.let {
        if (it is BitmapDrawable) return it.bitmap
        val bitmap = Bitmap.createBitmap(_width, _height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        it.setBounds(0, 0, canvas.width, canvas.height)
        it.draw(canvas)
        return bitmap
    }
    return null
}