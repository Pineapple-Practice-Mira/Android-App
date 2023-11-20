package site.pnpl.mira.ui.check_in.customview

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import site.pnpl.mira.R
import site.pnpl.mira.databinding.EmotionViewBinding
import site.pnpl.mira.entity.Emotion

class EmotionView constructor(context: Context) : LinearLayout(context) {

    private var _binding: EmotionViewBinding? = null
    private val binding: EmotionViewBinding get() = _binding!!
    private var emotion: Emotion? = null

    var emotionId = -1
        get() = emotion?.id ?: -1
        private set


    init {
        _binding = EmotionViewBinding.bind(LayoutInflater.from(context).inflate(R.layout.emotion_view, this))
    }

    fun setData(emotion: Emotion) {
        this.emotion = emotion
        binding.emoji.setImageDrawable(AppCompatResources.getDrawable(context, emotion.emojiResId))
        binding.emotionName.apply {
            text = resources.getString(emotion.nameResId)
            setTextColor(
                AppCompatResources.getColorStateList(
                    context,
                    when (emotion.type) {
                        Emotion.Type.POSITIVE -> R.color.emotion_view_text_color_positive_state
                        Emotion.Type.NEGATIVE -> R.color.emotion_view_text_color_negative_state
                    }
                )
            )
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}