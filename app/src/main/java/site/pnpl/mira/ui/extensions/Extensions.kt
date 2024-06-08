package site.pnpl.mira.ui.extensions

import androidx.fragment.app.Fragment
import android.content.Context
import android.content.res.Resources

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

val Fragment.screenHeight: Int
    get() = requireContext().screenHeight


/**
 * Pixel and Dp Conversion
 */
val Float.toPx get() = this * Resources.getSystem().displayMetrics.density

val Int.toPx get() = (this * Resources.getSystem().displayMetrics.density).toInt()

