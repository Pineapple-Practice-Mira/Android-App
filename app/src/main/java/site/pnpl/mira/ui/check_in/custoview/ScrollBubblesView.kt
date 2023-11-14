package site.pnpl.mira.ui.check_in.custoview

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.doOnLayout
import androidx.core.view.forEach
import site.pnpl.mira.R

class ScrollBubblesView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attributeSet, defStyle) {

    private var animationDuration = 700
    private var maxVisibleBubbles = 2
    private val bubbles = mutableListOf<BubbleView>()

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.ScrollBubblesView, 0, 0)
        try {
            animationDuration = attrs.getInt(R.styleable.ScrollBubblesView_animationDuration, animationDuration)
            maxVisibleBubbles = attrs.getInt(R.styleable.ScrollBubblesView_maxVisibleBubbles, maxVisibleBubbles)
        } finally {
            attrs.recycle()
        }
    }

    fun addListOfBubbles(bubbleViews: List<BubbleView>) {
        bubbles.clear()
        bubbles.addAll(bubbleViews)
        bubbles.forEach {
            addView(it)
        }
        doOnLayout {
            if (bubbles.size > maxVisibleBubbles) {
                var yOffset = 0
                bubbles.forEachIndexed { index, bubbleView ->
                    if (index > maxVisibleBubbles - 1) {
                        yOffset += bubbleView.height
                    }
                }
                bubbles.forEach { bubbleView ->
                    bubbleView.y = bubbleView.y + yOffset
                }
            }
        }

    }

    fun scrollUp() {
        bubbles.find { it.y >= y + height }?.let { targetBubbleView ->
            val yOffset = targetBubbleView.height

            bubbles.forEachIndexed { index, bubbleView ->
                val newPos = bubbleView.y - yOffset
                bubbleView.animate()
                    .y(newPos)
                    .alpha(if (index == 0) 0f else 1f)
                    .duration = animationDuration.toLong()
            }
        }
    }

    fun scrollDown() {
        val downBorder = y + height
        val yOffset = bubbles.last {
            it.y <= downBorder
        }.height

        forEach { bubbleView ->
            val newPos = bubbleView.y + yOffset
            println("bubbleView.y ${bubbleView.y} yOffset $yOffset newPos $newPos bubbles[0].y ${bubbles[0].y} bubbles[0].y - yOffset ${bubbles[0].y - yOffset}")
            bubbleView.animate()
                .y(newPos)
                .alphaBy(1f)
                .duration = animationDuration.toLong()
        }

    }

    fun setMessageInRightBubble(message: String) {
        bubbles.forEach { bubbleView ->
            if (bubbleView.type == BubbleView.Type.RIGHT) {
                bubbleView.message = message
            }
        }
    }

}