package site.pnpl.mira.data.remote

import retrofit2.Response
import retrofit2.http.GET
import site.pnpl.mira.data.remote.dto.EmotionDtoList


interface MiraApi {
    @GET("emotion/")
    suspend fun getEmotions(): Response<EmotionDtoList>
}