package site.pnpl.mira.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import site.pnpl.mira.data.remote.dto.emotions.EmotionDtoList
import site.pnpl.mira.data.remote.dto.exercises.ExerciseDto
import site.pnpl.mira.data.remote.dto.exercises.ExerciseDtoList


interface MiraApi {
    @GET("emotion/")
    suspend fun getEmotions(): Response<EmotionDtoList>

    @GET("exercise/intro")
    suspend fun getIntroExercise(): Response<ExerciseDto>

    @GET("exercise/")
    suspend fun getExercisesByEmotionId(
        @Query("emotion_id") emotionId : Int,
        @Query("published_only") publishedOnly: Boolean,
    ): Response<ExerciseDtoList>

    @GET("exercise/{id}")
    suspend fun getExercisesById(
        @Path("id") id : Int,
    ): Response<ExerciseDto>

    @GET("exercise/")
    suspend fun getAllExercises(
        @Query("include_intro") includeIntro: Boolean,
        @Query("published_only") publishedOnly: Boolean
    ): Response<ExerciseDtoList>

}