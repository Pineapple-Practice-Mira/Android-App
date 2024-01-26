package site.pnpl.mira.data.repositories

import retrofit2.Response
import site.pnpl.mira.data.database.exercises.ExerciseDao
import site.pnpl.mira.data.database.exercises.ExerciseEntity
import site.pnpl.mira.data.models.ApiResult
import site.pnpl.mira.data.remote.MiraApi
import site.pnpl.mira.data.remote.dto.emotions.EmotionDtoItem
import site.pnpl.mira.data.remote.dto.exercises.ExerciseDto
import site.pnpl.mira.data.remote.dto.exercises.ExerciseDtoList
import site.pnpl.mira.data.remote.dto.exercises.ScreenDto
import site.pnpl.mira.models.ExerciseUI
import site.pnpl.mira.models.ScreenUI
import javax.inject.Inject
import kotlin.random.Random

class ExerciseRepository @Inject constructor(
    private val retrofitService: MiraApi,
    private val exerciseDao: ExerciseDao
) {

    suspend fun getIntroExerciseFromApi(): ApiResult<Any> =
        try {
            val response = retrofitService.getIntroExercise()
            if (response.isSuccessful && response.body() != null) {
                val exerciseUI = response.body()!!.toExerciseUI()
                openExercise(exerciseUI.id)
                ApiResult.Success(exerciseUI)
            } else {
                ApiResult.Error("Response error from server")
            }
        } catch (e: Exception) {
            ApiResult.Error(e.toString())
        }

    suspend fun getExercisesListByEmotionId(emotionId: Int): ApiResult<Any> =
        getExercisesList {
            retrofitService.getExercisesByEmotionId(emotionId, true)
        }

    suspend fun getAllExercisesFromApi(): ApiResult<Any> {
        val result = getExercisesList {
            retrofitService.getAllExercises(includeIntro = true, publishedOnly = true)
        }

        return if (result is ApiResult.Success) {
            val exercises = mutableListOf<ExerciseUI>()
            (result.value as ExerciseDtoList).toListExerciseUI().forEach { exerciseUI ->
                if (isOpenExercise(exerciseUI.id) || exerciseUI.isIntro ) {
                    exercises.add(exerciseUI)
                }
            }
            ApiResult.Success(exercises)
        } else {
            result
        }
    }

    private suspend fun <T> getExercisesList(
        apiCall: suspend () -> Response<T>
    ): ApiResult<Any> {
        return try {
            val response = apiCall.invoke()

            if (response.isSuccessful && response.body() != null) {
                val exerciseDtoList = response.body() as ExerciseDtoList

                if (exerciseDtoList.isEmpty()) {
                    ApiResult.Error("Exercises not found")
                }

                ApiResult.Success(exerciseDtoList)
            } else {
                ApiResult.Error("Response error from server")
            }

        } catch (e: Exception) {
            ApiResult.Error(e.toString())
        }
    }

    suspend fun getRandomExercise(exercises: ExerciseDtoList): ApiResult<Any> {
        val nonOpenedExercises = exercises.toMutableList()
        nonOpenedExercises.removeIf { isOpenExercise(it.id) }

        val exerciseDto = if (nonOpenedExercises.isEmpty()) {
            exercises[Random.nextInt(0, exercises.size)]
        } else {
            nonOpenedExercises[Random.nextInt(0, nonOpenedExercises.size)]
        }
        return getExerciseById(exerciseDto.id)
    }

    suspend fun getExerciseById(id: Int): ApiResult<Any> =
        try {
            val response = retrofitService.getExercisesById(id)

            if (response.isSuccessful && response.body() != null) {
                val exerciseDto = response.body() as ExerciseDto
                openExercise(exerciseDto.id)
                ApiResult.Success(exerciseDto.toExerciseUI())
            } else {
                ApiResult.Error("Response error from server")
            }
        } catch (e: Exception) {
            ApiResult.Error(e.toString())
        }


    private fun isOpenExercise(exerciseId: Int): Boolean {
        val exercise = exerciseDao.getExerciseById(exerciseId)
        if (exercise != null && exercise.isOpened == 1) {
            return true
        }
        return false
    }

    private fun openExercise(exerciseId: Int) {
        exerciseDao.insert(
            ExerciseEntity(
                exerciseId = exerciseId,
                isOpened = 1
            )
        )
    }
}

fun ExerciseDtoList.toListExerciseUI(): List<ExerciseUI> =
    this.map { it.toExerciseUI() }.toList()


fun ExerciseDto.toExerciseUI(): ExerciseUI =
    ExerciseUI(id = id,
        name = name,
        description = description,
        previewImageLink = previewImageLink,
        emotionsId = emotions.toListInteger(),
        screens = screens?.map { it.toScreenUI() } ?: emptyList(),
        isIntro = isIntro
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