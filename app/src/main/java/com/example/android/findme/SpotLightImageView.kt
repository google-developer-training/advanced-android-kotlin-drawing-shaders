/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.findme

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Shader.TileMode
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.floor
import kotlin.random.Random


/**
 * This class demonstrates how to use Paint with a BitmapShader to fill the spotlight circle with a
 * bitmap instead of a simple color.
 */
class SpotLightImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var shaderPaint = Paint()
    private var shouldDrawSpotLight = false
    private var gameOver = false
    private var shader: Shader

    private lateinit var winnerRect: RectF
    private var androidBitmapX = 0f
    private var androidBitmapY = 0f
    private val bitmapAndroid = BitmapFactory.decodeResource(resources, R.drawable.android)
    private val spotlight = BitmapFactory.decodeResource(resources, R.drawable.mask)
    private val shaderMatrix = Matrix()

    init {
        val bitmap = Bitmap.createBitmap(spotlight.width, spotlight.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Draw a black rectangle.
        paint.color = Color.BLACK
        canvas.drawRect(0.0f, 0.0f, spotlight.width.toFloat(), spotlight.height.toFloat(), paint)

        // Use DST_OUT compositing mode to mask out the spotlight from the black rectangle.
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        canvas.drawBitmap(spotlight, 0.0f, 0.0f, paint)

        shader = BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP)
        shaderPaint.shader = shader
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        val motionEventX = motionEvent.x
        val motionEventY = motionEvent.y

        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                shouldDrawSpotLight = true
                if (gameOver) {
                    gameOver = false
                    setupWinnerRect()
                }
            }
            MotionEvent.ACTION_UP -> {
                shouldDrawSpotLight = false
                gameOver = winnerRect.contains(motionEventX, motionEventY)
            }
        }
        shaderMatrix.setTranslate(
            motionEventX - spotlight.width / 2.0f,
            motionEventY - spotlight.height / 2.0f
        )
        shader.setLocalMatrix(shaderMatrix)
        invalidate()
        return true
    }

    /**
     * This method is called every time the size of a view changes, including the first time after
     * it has been inflated.
     *
     * @param newWidth Current width of view
     * @param newHeight Current height of view
     * @param oldWidth Previous width of view
     * @param oldHeight Previous height of view
     */
    override fun onSizeChanged(
        newWidth: Int,
        newHeight: Int,
        oldWidth: Int,
        oldHeight: Int
    ) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight)
        setupWinnerRect()
    }

    /**
     * Render view content:
     * Fill the canvas with white and draw the bitmap of the Android image.
     * If game is not over and shouldDrawSpotLight is true, draw a full screen rectangle using the
     * paint with BitmapShader, else fill the canvas with black color.
     *
     * @param canvas The canvas on which the background will be drawn
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmapAndroid, androidBitmapX, androidBitmapY, shaderPaint)

        if (!gameOver) {
            if (shouldDrawSpotLight) {
                canvas.drawRect(0.0f, 0.0f, width.toFloat(), height.toFloat(), shaderPaint)
            } else {
                canvas.drawColor(Color.BLACK)
            }
        }
    }

    /**
     * Calculates a randomized location for the Android bitmap and the winning bounding rectangle.
     */
    private fun setupWinnerRect() {
        androidBitmapX = floor(Random.nextFloat() * (width - bitmapAndroid.width))
        androidBitmapY = floor(Random.nextFloat() * (height - bitmapAndroid.height))
        winnerRect = RectF(
            (androidBitmapX),
            (androidBitmapY),
            (androidBitmapX + bitmapAndroid.width),
            (androidBitmapY + bitmapAndroid.height)
        )
    }
}