package site.pnpl.mira.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.pnpl.mira.data.models.EmotionDataModel
import site.pnpl.mira.data.repositories.EmotionRepository
import site.pnpl.mira.models.EmotionUI
import javax.inject.Inject

class EmotionProvider @Inject constructor(
    private val repository: EmotionRepository,
    private val applicationScope: CoroutineScope
) {

    var emotions: List<EmotionUI> = emptyList()
        private set

    fun init() {
        getAllEmotion()
    }

    private fun getAllEmotion() {
        applicationScope.launch {
            emotions = repository.getEmotionsFromDb().map { it.toEmotionUI() }
        }
    }

    fun open(emotionId: Int) {
        getEmotion(emotionId)?.let { emotionUI ->
            if (!emotionUI.isOpened) {
                applicationScope.launch {
                    repository.openEmotion(emotionId)
                }
            }
        }
    }

    fun getNameGenitive(emotionId: Int): String {
        val emotion = getEmotion(emotionId)
        return emotion?.nameGenitive ?: ""
    }

    fun getName(emotionId: Int): String {
        val emotion = getEmotion(emotionId)
        return emotion?.name ?: ""
    }

    fun getPathToEmoji(emotionId: Int): String {
        val emotion = getEmotion(emotionId)
        return emotion?.emojiLink ?: ""
    }

    fun isPositive(emotionId: Int): Boolean {
        val emotion = getEmotion(emotionId)
        return emotion?.isPositive ?: false
    }

    private fun getEmotion(emotionId: Int): EmotionUI? =
        emotions.find { it.id == emotionId }
}

fun EmotionDataModel.toEmotionUI(): EmotionUI =
    EmotionUI(
        id = emotionId,
        name = name,
        nameGenitive = nameGenitive,
        isPositive = isPositive,
        emojiLink = localEmojiLink,
        isOpened = isOpened
    )
