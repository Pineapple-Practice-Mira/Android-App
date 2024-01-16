package site.pnpl.mira.data.remote

import retrofit2.Response
import retrofit2.http.GET
import site.pnpl.mira.data.remote.dto.emotions.EmotionDtoList
import site.pnpl.mira.data.remote.dto.exercises.ExerciseDto


interface MiraApi {
    @GET("emotion/")
    suspend fun getEmotions(): Response<EmotionDtoList>

    @GET("exercise/intro")
    suspend fun getIntroExercise(): Response<ExerciseDto>
}