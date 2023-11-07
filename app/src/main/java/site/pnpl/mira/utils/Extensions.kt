package site.pnpl.mira.utils

import android.content.Context
import android.content.res.Resources

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

/**
 * Pixel and Dp Conversion
 */
val Float.toPx get() = this * Resources.getSystem().displayMetrics.density
val Float.toDp get() = this / Resources.getSystem().displayMetrics.density

val Int.toPx get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Int.toDp get() = (this / Resources.getSystem().displayMetrics.density).toInt()