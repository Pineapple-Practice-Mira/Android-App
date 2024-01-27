package site.pnpl.mira.domain

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import site.pnpl.mira.data.models.EmotionDataModel
import site.pnpl.mira.data.repositories.EmotionRepository
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import javax.inject.Inject

class EmotionCreator @Inject constructor(
    private val context: Context,
    private val repository: EmotionRepository,
    private val applicationScope: CoroutineScope,
    private val emotionProvider: EmotionProvider
) {

    var loadingState: LoadingState<Any> = LoadingState.Loading

    private val directoryToSaveEmoji: String
        get() {
            val dir = File("${context.filesDir}$DIRECTORY_FOR_EMOJI")
            if (!dir.exists()) dir.mkdirs()
            return dir.path
        }

    fun update() {
        applicationScope.launch {
            val emotionsApi = getEmotionListFromApi()
            val emotionsDb = getEmotionListFromDb().toMutableList()

            if (emotionsApi.isEmpty() && emotionsDb.isEmpty()) {
                loadingState = LoadingState.Error("")
                return@launch
            }
            loadingState = LoadingState.Success

            emotionsApi.forEach { emotion ->
                if (!emotionsDb.remove(emotion)) {
                    saveEmotion(emotion)
                }
            }

            if (emotionsDb.isNotEmpty()) {
                deleteEmotionsFromBd(emotionsDb)
            }
            emotionProvider.init()
        }
    }

    private fun deleteEmotionsFromBd(emotions: List<EmotionDataModel>) {
        repository.deleteEmotions(emotions)
    }

    private suspend fun getEmotionListFromApi(): List<EmotionDataModel> {
        val def = applicationScope.async {
            val emotions = repository.getEmotionsFromApi()
            emotions.orEmpty()
        }
        return def.await()
    }

    private fun getEmotionListFromDb(): List<EmotionDataModel> =
        repository.getEmotionsFromDb()

    private suspend fun saveEmotion(emotion: EmotionDataModel) {
        val emotionDataModel = EmotionDataModel(
            emotionId = emotion.emotionId,
            name = emotion.name,
            nameGenitive = emotion.nameGenitive,
            remoteEmojiLink = emotion.remoteEmojiLink,
            localEmojiLink = loadEmoji(emotion.remoteEmojiLink).orEmpty(),
            isPositive = emotion.isPositive,
            isOpened = false
        )
        repository.writeEmotionToDb(emotionDataModel)
    }

    private suspend fun loadEmoji(url: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(directoryToSaveEmoji, url.parseFileName())
                if (!file.exists()) {
                    val inputStream = URL(url).openStream()
                    saveInputStreamToFile(inputStream, file)
                }
                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun saveInputStreamToFile(inputStream: InputStream, file: File) {
        val outputStream = FileOutputStream(file)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
    }

    companion object {
        const val DIRECTORY_FOR_EMOJI = "/emoji/"
    }


}

sealed class LoadingState<out T> {
    object Success : LoadingState<Any>()
    data class Error(val error: String): LoadingState<Any>()
    object Loading : LoadingState<Any>()
}

fun String.parseFileName(): String {
    val str = this.split("/")
    return str[str.size - 1]
}