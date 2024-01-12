package site.pnpl.mira.ui.exercise.customview

import android.content.Context
import android.widget.LinearLayout
import site.pnpl.mira.R
import site.pnpl.mira.databinding.ItemEmotionButtonBinding

class EmotionButton(context: Context) : LinearLayout(context) {
    constructor(
        context: Context,
        emotionName: String
        ) : this(context) {
            inflate(context, R.layout.item_emotion_button, this).also {
                ItemEmotionButtonBinding.bind(it).itemEmotionTextView.text = emotionName
            }
        }
}