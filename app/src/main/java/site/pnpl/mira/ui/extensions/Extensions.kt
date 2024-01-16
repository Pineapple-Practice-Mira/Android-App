package site.pnpl.mira.ui.extensions

import android.content.Context
import android.content.res.Resources
import androidx.fragment.app.Fragment

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

val Fragment.screenHeight: Int
    get() = requireContext().screenHeight


/**
 * Pixel and Dp Conversion
 */
val Float.toPx get() = this * Resources.getSystem().displayMetrics.density

val Int.toPx get() = (this * Resources.getSystem().displayMetrics.density).toInt()

