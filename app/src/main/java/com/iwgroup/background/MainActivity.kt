package com.iwgroup.background

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import androidx.annotation.IntRange

class MainActivity : AppCompatActivity() {

    companion object {
        const val SAVED_ALPHA = "SAVED_ALPHA"
        const val SAVED_CORNER = "SAVED_CORNER"
    }

    private var savedAlpha: Int = 0
    private var savedCorner: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setImage(savedAlpha, savedCorner)

        findViewById<SeekBar>(R.id.sbCorners).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser && progress in 0..50) {
                    savedCorner = progress
                    setImage(savedAlpha, progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        findViewById<SeekBar>(R.id.sbAlpha).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser && progress in 0..100) {
                    savedAlpha = progress
                    setImage(progress, savedCorner)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setImage(@IntRange(from = 0, to = 100) alpha: Int, cornersDp: Int = 8) {
        findViewById<ImageView>(R.id.ivImage)
            .setImageBitmap(
                applicationContext
                    .getBitmap(
                        R.drawable.ic__021,
                        180.toPx(),
                        110.toPx()
                    )
                    ?.makeTransparent(alpha)
                    ?.makeRoundedCorners(cornersDp.toPx().toFloat())
            )
    }

}



fun Bitmap.makeTransparent(@IntRange(from = 0, to = 100) value: Int): Bitmap {
    val alpha = 255 * value / 100
    val _width = width
    val _height = height
    val transBitmap = Bitmap.createBitmap(_width, _height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(transBitmap)
    val paint = Paint()
    paint.alpha = alpha
    canvas.drawARGB(0, 0, 0, 0)
    canvas.drawBitmap(this, 0f, 0f, paint)
    return transBitmap
}

fun Bitmap.makeRoundedCorners(angleRadius: Float): Bitmap {
    val path = Path()
    val bitmap = this
    val transBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(transBitmap)
    val rect = Rect(0, 0, width, height)
    val rectF = RectF(rect)
    path.addRoundRect(rectF, angleRadius, angleRadius, Path.Direction.CW)
    canvas.clipPath(path)
    canvas.drawBitmap(bitmap, rect, rect, Paint(Paint.ANTI_ALIAS_FLAG))
    return transBitmap
}