package site.pnpl.mira.ui.extensions

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.setCurrentItem(
    index: Int,
    duration: Long,
//    interpolator: TimeInterpolator = PathInterpolator(0.8f, 0f, 0.35f, 1f),
    interpolator: LinearInterpolator = LinearInterpolator(),
    pageWidth: Int = width - paddingLeft - paddingRight,
) {
    val pxToDrag: Int = pageWidth * (index - currentItem)
    val animator = ValueAnimator.ofInt(0, pxToDrag)
    var previousValue = 0

    val scrollView = (getChildAt(0) as? RecyclerView)
    animator.addUpdateListener { valueAnimator ->
        val currentValue = valueAnimator.animatedValue as Int
        val currentPxToDrag = (currentValue - previousValue).toFloat()
        scrollView?.scrollBy(currentPxToDrag.toInt(), 0)
        previousValue = currentValue
    }

    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) {
            // Fallback to fix minor offset inconsistency while scrolling
            setCurrentItem(index, true)
            post { requestTransform() } // To make sure custom transforms are applied
        }

        override fun onAnimationCancel(animation: Animator) { /* Ignored */
        }

        override fun onAnimationRepeat(animation: Animator) { /* Ignored */
        }
    })

    animator.interpolator = interpolator
    animator.duration = duration
    animator.start()
}