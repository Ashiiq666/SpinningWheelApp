package com.ak.spinningwheelapp

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.ak.spinningwheelapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), Animation.AnimationListener {
    private var binding: ActivityMainBinding? = null
    private var count = 0
    private var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.btnSpin!!.setOnTouchListener(spinTouchListener())


    }

    val prizes = intArrayOf(200, 600, 1000, 500, 400, 300, 700, 800)
    private var mSpinnerDuration: Long = 0
    private var mSpinnerRevolution = 0f
    var prizeText = "N/A"


    private fun startSpinner() {
        mSpinnerRevolution = 3500f
        mSpinnerDuration = 5000

        if (count >= 30) {
            mSpinnerDuration = 1000
            mSpinnerRevolution = (3500 * 2).toFloat()
        }
        if (count >= 60) {
            mSpinnerDuration = 15000
            mSpinnerRevolution = (3500 * 3).toFloat()
        }

        val end = Math.floor(Math.random() * 3600).toInt()
        val numOfPrizes = prizes.size
        val degreePerPrize = 360 / numOfPrizes
        val shift = 0
        val prizeIndex = (shift + end) % numOfPrizes

        prizeText = "prize is:${prizes[prizeIndex]}"

        val rotateAnim = RotateAnimation(
            0f, mSpinnerRevolution + end,
            Animation.RELATIVE_TO_SELF,
            0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnim.interpolator = DecelerateInterpolator()
        rotateAnim.repeatCount = 0
        rotateAnim.duration = mSpinnerDuration
        rotateAnim.setAnimationListener(this)
        rotateAnim.fillAfter = true
        binding?.wheel?.startAnimation(rotateAnim)

    }

    override fun onAnimationStart(p0: Animation?) {
        binding?.tvInfo?.text = "Spinning..."
    }

    override fun onAnimationEnd(p0: Animation?) {
        binding?.tvInfo?.text = prizeText
    }

    override fun onAnimationRepeat(p0: Animation?) {}
    private inner class spinTouchListener : View.OnTouchListener {
        override fun onTouch(p0: View?, motionEvent: MotionEvent?): Boolean {
            when (motionEvent!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    flag = true
                    count = 0
                    Thread {
                        while (flag) {
                            count++
                            if (count == 100) {
                                try {
                                    Thread.sleep(100)
                                } catch (e: InterruptedException) {
                                    e.printStackTrace()
                                }
                                count = 0
                            }
                            try {
                                Thread.sleep(10)
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }
                        }
                    }.start()
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    flag = false
                    startSpinner()
                    return false
                }
            }
            return false
        }


    }
}

