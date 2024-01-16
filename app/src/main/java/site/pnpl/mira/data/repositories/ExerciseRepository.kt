package site.pnpl.mira.data.repositories

import site.pnpl.mira.data.models.ApiResult
import site.pnpl.mira.data.remote.MiraApi
import site.pnpl.mira.data.remote.dto.emotions.EmotionDtoItem
import site.pnpl.mira.data.remote.dto.exercises.ExerciseDto
import site.pnpl.mira.data.remote.dto.exercises.ScreenDto
import site.pnpl.mira.models.ExerciseUI
import site.pnpl.mira.models.ScreenUI
import javax.inject.Inject

class ExerciseRepository @Inject constructor(
    private val retrofitService: MiraApi
) {

    suspend fun getIntroExercise(): ApiResult<Any> =
        try {
            val response = retrofitService.getIntroExercise()
            if (response.isSuccessful && response.body() != null) {
                ApiResult.Success(response.body()!!.toExerciseUI())
            } else {
                ApiResult.Error("Response error from server")
            }
        } catch (e: Exception) {
            ApiResult.Error(e.toString())
        }
}

fun ExerciseDto.toExerciseUI(): ExerciseUI =
    ExerciseUI(id = 0,
        name = name,
        title = title,
        description = description,
        previewImageLink = previewImageLink,
        emotionsId = emotions.toListInteger(),
        screens = screens.map { it.toScreenUI() }
    )

fun List<EmotionDtoItem>.toListInteger(): List<Int> {
    val list = mutableListOf<Int>()
    this.forEach {
        list.add(it.id)
    }
    return list
}

fun ScreenDto.toScreenUI(): ScreenUI =
    ScreenUI(
        animationLink = animationLink,
        sequenceNumber = sequenceNumber,
        text = text,
        title = title
    )