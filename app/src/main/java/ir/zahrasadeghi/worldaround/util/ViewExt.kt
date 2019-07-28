package ir.zahrasadeghi.worldaround.util

import android.view.View
import android.view.animation.AnimationUtils
import ir.zahrasadeghi.worldaround.R

fun View.popOut() {

    startAnimation(AnimationUtils.loadAnimation(context, R.anim.popout))
    this.visibility = View.VISIBLE
}

fun View.popIn() {
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.popin))

    this.visibility = View.GONE
}