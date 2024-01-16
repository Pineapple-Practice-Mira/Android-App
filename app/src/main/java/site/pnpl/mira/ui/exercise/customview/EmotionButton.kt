package site.pnpl.mira.ui.exercise.customview

import android.content.Context
import android.widget.LinearLayout
import site.pnpl.mira.R
import site.pnpl.mira.databinding.ItemEmotionButtonBinding

class EmotionButton(context: Context) : LinearLayout(context) {

    private lateinit var binding: ItemEmotionButtonBinding
    var emotionId = -1
        private set

    constructor(
        context: Context,
        emotionName: String,
        emotionId: Int
    ) : this(context) {
        binding = ItemEmotionButtonBinding.bind(inflate(context, R.layout.item_emotion_button, this))
        this.emotionId = emotionId
        binding.itemEmotionTextView.text = emotionName
    }

    fun onClickListener(listener: (EmotionButton) -> Unit) {
        binding.root.setOnClickListener {
            isSelected = !isSelected
            listener(this)
        }
    }
}