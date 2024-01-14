package site.pnpl.mira.domain

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import site.pnpl.mira.data.EmotionDataModel
import site.pnpl.mira.data.EmotionRepository
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import javax.inject.Inject

class EmotionCreator @Inject constructor(
    private val context: Context,
    private val repository: EmotionRepository,
    private val applicationScope: CoroutineScope
) {

    private val directoryToSaveEmoji: String
        get() {
            val dir = File("${context.filesDir}$DIRECTORY_FOR_EMOJI")
            if (!dir.exists()) dir.mkdirs()
            return dir.path
        }

    fun update() {
        applicationScope.launch {
            val emotionsApi = getEmotionListFromApi()
            val emotionsDb = getEmotionListFromDb()

            emotionsApi.forEach { emotion ->
                if (!emotionsDb.contains(emotion)) {
                    saveEmotion(emotion)
                }
            }
        }
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
                    // Открываем InputStream из URL
                    val inputStream = URL(url).openStream()
                    // Сохраняем InputStream в файл
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

fun String.parseFileName(): String {
    val str = this.split("/")
    return str[str.size - 1]
}