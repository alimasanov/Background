package com.iwgroup.background

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange

class MainActivity : AppCompatActivity() {

    private var savedAlpha: Int = 100
    private var savedCorner: Int = 8
    private var savedWidth: Int = 40
    private var savedHeight: Int = 40
    @DrawableRes
    private var savedImage: Int = R.drawable.ic_small

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setImage(savedImage, savedAlpha, savedCorner, savedWidth, savedHeight)

        findViewById<RadioGroup>(R.id.rgSize).setOnCheckedChangeListener { group, checkedId ->
            val rb = findViewById<RadioButton>(checkedId)
            when (rb.tag) {
                "rbSmall" -> {
                    savedWidth = 40
                    savedHeight = 40
                    savedImage = R.drawable.ic_small
                    setImage(savedImage, savedAlpha, savedCorner, savedWidth, savedHeight)
                }
                "rbMiddle" -> {
                    savedWidth = 110
                    savedHeight = 110
                    savedImage = R.drawable.ic_middle
                    setImage(savedImage, savedAlpha, savedCorner, savedWidth, savedHeight)
                }
                "rbBig" -> {
                    savedWidth = 180
                    savedHeight = 110
                    savedImage = R.drawable.ic_big
                    setImage(savedImage, savedAlpha, savedCorner, savedWidth, savedHeight)
                }
            }
        }

        findViewById<SeekBar>(R.id.sbCorners).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser && progress in 0..50) {
                    savedCorner = progress
                    setImage(savedImage, savedAlpha, savedCorner, savedWidth, savedHeight)
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
                    setImage(savedImage, savedAlpha, savedCorner, savedWidth, savedHeight)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setImage(@DrawableRes drawableRes: Int, @IntRange(from = 0, to = 100) alpha: Int, cornersDp: Int = 8, _widthDp: Int, _heightDp: Int) {
        findViewById<ImageView>(R.id.ivImage)
            .setImageBitmap(
                applicationContext
                    .getBitmap(
                        drawableRes,
                        _widthDp.toPx(),
                        _heightDp.toPx()
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