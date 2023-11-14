package site.pnpl.mira.ui.check_in.custoview

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import site.pnpl.mira.R

class BubbleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    constructor(context: Context, type: Type, message: String) : this(context) {

        inflate(
            context,
            when (type) {
                Type.LEFT -> R.layout.item_bubble_left
                Type.RIGHT -> R.layout.item_bubble_right
            },
            this
        )
        this.type = type
        this.message = message
    }

    var type : Type = Type.LEFT
        private set
    var message : String = ""
        set(value) {
            findViewById<TextView>(R.id.message).text = value
            field = value
        }


    enum class Type {
        LEFT,
        RIGHT
    }
}