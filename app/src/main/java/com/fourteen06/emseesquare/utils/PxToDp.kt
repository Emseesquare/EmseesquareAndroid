package com.fourteen06.emseesquare.utils

import android.content.Context
import android.util.DisplayMetrics


fun Context.dpToPx(dp: Int): Int {
    val displayMetrics: DisplayMetrics = resources.displayMetrics
    return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}