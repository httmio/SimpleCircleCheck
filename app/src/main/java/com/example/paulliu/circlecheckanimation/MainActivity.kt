package com.example.paulliu.circlecheckanimation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var MSG_REFRESH_PROGRESS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repeatAnimation.setOnClickListener({
            srpStroke0.doAnimation()
           })
//        var mAnimator: ValueAnimator = ValueAnimator.ofInt(0, 100)
//        mAnimator.addUpdateListener {
//            animation ->
//            val animatorValue =animation.animatedValue as Int
//            srpStroke0.progress = animatorValue
//        }
//        mAnimator.duration = 100000
//        mAnimator.start()

    }

}
