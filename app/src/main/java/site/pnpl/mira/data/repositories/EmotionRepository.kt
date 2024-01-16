package site.pnpl.mira.data.repositories

import site.pnpl.mira.data.database.emotions.EmotionDao
import site.pnpl.mira.data.database.emotions.EmotionEntity
import site.pnpl.mira.data.models.EmotionDataModel
import site.pnpl.mira.data.remote.MiraApi
import site.pnpl.mira.data.remote.dto.emotions.EmotionDtoItem
import javax.inject.Inject

class EmotionRepository @Inject constructor(
    private val retrofitService: MiraApi,
    private val emotionDao: EmotionDao
) {
    suspend fun getEmotionsFromApi(): List<EmotionDataModel>? =
        try {
            val response = retrofitService.getEmotions()
            if (response.isSuccessful) {
                response.body()?.map { it.toEmotionDataModel() }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }

    fun getEmotionsFromDb(): List<EmotionDataModel> =
        emotionDao.getAllEmotions().map { it.toEmotionDataModel() }

    fun writeEmotionToDb(emotion: EmotionDataModel) =
        emotionDao.insertEmotions(listOf(emotion.toEmotionEntity()))

    fun deleteEmotions(emotions: List<EmotionDataModel>) {
        emotionDao.deleteEmotions(emotions.map { it.toEmotionEntity() })
    }

    fun openEmotion(emotionId: Int) {
        emotionDao.updateOpenedFlag(emotionId,true)
    }

}

fun EmotionDataModel.toEmotionEntity(): EmotionEntity =
    EmotionEntity(
        id = 0,
        emotionId = emotionId,
        name = name,
        nameGenitive = nameGenitive,
        remoteEmojiLink = remoteEmojiLink,
        localEmojiLink = localEmojiLink,
        isPositive = if (isPositive) 1 else 0,
        isOpened = if (isOpened) 1 else 0
    )

fun EmotionEntity.toEmotionDataModel(): EmotionDataModel =
    EmotionDataModel(
        emotionId = emotionId,
        name = name,
        nameGenitive = nameGenitive,
        remoteEmojiLink = remoteEmojiLink,
        localEmojiLink = localEmojiLink,
        isPositive = isPositive == 1,
        isOpened = isOpened == 1
    )

fun EmotionDtoItem.toEmotionDataModel(): EmotionDataModel =
    EmotionDataModel(
        emotionId = id,
        name = name,
        nameGenitive = nameGenitive,
        remoteEmojiLink = emojiLink,
        localEmojiLink = "",
        isPositive = isPositive,
        isOpened = false

    )