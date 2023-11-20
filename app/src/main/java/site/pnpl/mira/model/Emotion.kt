package site.pnpl.mira.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import site.pnpl.mira.R

data class Emotion(
    val id: Int,
    @StringRes val nameResId: Int,
    @StringRes val nameParentCaseResId: Int = nameResId,
    val type: Type,
    @DrawableRes val emojiResId: Int
) {
    enum class Type {
        POSITIVE,
        NEGATIVE
    }
}

object EmotionsList {
    val emotions = listOf(
        Emotion(id = 0, nameResId = R.string.joy, type = Emotion.Type.POSITIVE, emojiResId = R.drawable.emotion_joy),
        Emotion(id = 1, nameResId = R.string.calmness, type = Emotion.Type.POSITIVE, emojiResId = R.drawable.emotion_calmness),
        Emotion(id = 2, nameResId = R.string.satisfaction, type = Emotion.Type.POSITIVE, emojiResId = R.drawable.emotion_satisfaction),
        Emotion(id = 3, nameResId = R.string.interest, type = Emotion.Type.POSITIVE, emojiResId = R.drawable.emotion_interest),
        Emotion(id = 4, nameResId = R.string.confidence, type = Emotion.Type.POSITIVE, emojiResId = R.drawable.enotion_confidence),
        Emotion(id = 5, nameResId = R.string.sympathy, nameParentCaseResId = R.string.sympathy_parent_case, type = Emotion.Type.POSITIVE, emojiResId = R.drawable.emotion_sympathy),
        Emotion(id = 6, nameResId = R.string.delight, type = Emotion.Type.POSITIVE, emojiResId = R.drawable.emotion_delight),
        Emotion(id = 7, nameResId = R.string.love, type = Emotion.Type.POSITIVE, emojiResId = R.drawable.emotion_love),
        Emotion(id = 8, nameResId = R.string.sadness, type = Emotion.Type.NEGATIVE, emojiResId = R.drawable.emotion_sadness),
        Emotion(id = 9, nameResId = R.string.fatigue, type = Emotion.Type.NEGATIVE, emojiResId = R.drawable.emotion_fatigue),
        Emotion(id = 10, nameResId = R.string.anxiety, nameParentCaseResId = R.string.anxiety_parent_case,  type = Emotion.Type.NEGATIVE, emojiResId = R.drawable.emotion_anxiety),
        Emotion(id = 11, nameResId = R.string.irritation, type = Emotion.Type.NEGATIVE, emojiResId = R.drawable.emotion_irritation),
        Emotion(id = 12, nameResId = R.string.fear, type = Emotion.Type.NEGATIVE, emojiResId = R.drawable.emotion_fear),
        Emotion(id = 13, nameResId = R.string.depression, type = Emotion.Type.NEGATIVE, emojiResId = R.drawable.emotion_depression),
        Emotion(id = 14, nameResId = R.string.disappointment, type = Emotion.Type.NEGATIVE, emojiResId = R.drawable.emotion_disappointment),
        Emotion(id = 15, nameResId = R.string.anger, type = Emotion.Type.NEGATIVE, emojiResId = R.drawable.emotion_anger),
    )
}

