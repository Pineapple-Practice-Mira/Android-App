package site.pnpl.mira.utils

import okhttp3.Interceptor
import okhttp3.Response
import site.pnpl.mira.domain.analitycs.Analytics
import site.pnpl.mira.domain.analitycs.AnalyticsEvent
import site.pnpl.mira.domain.analitycs.EventParameter
import java.io.IOException
import javax.inject.Inject

class AnalyticsInterceptor @Inject constructor(
    private val analytics: Analytics
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Записываем URL перед отправкой запроса
        val url = request.url.toString()

        val startTime = System.currentTimeMillis()

        try {
            // Отправка запроса
            val response = chain.proceed(request)

            // Замер времени после получения ответа
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime

            analytics.sendEvent(
                AnalyticsEvent.NAME_REQUEST_SUCCESS,
                listOf(
                    EventParameter(AnalyticsEvent.PARAMETER_RESPONSE_TIME, duration),
                    EventParameter(AnalyticsEvent.PARAMETER_REQUEST_URL, url)
                )
            )

            return response
        } catch (e: IOException) {
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime
            analytics.sendEvent(
                AnalyticsEvent.NAME_REQUEST_ERROR,
                listOf(
                    EventParameter(AnalyticsEvent.PARAMETER_RESPONSE_TIME, duration),
                    EventParameter(AnalyticsEvent.PARAMETER_REQUEST_URL, url),
                    EventParameter(AnalyticsEvent.PARAMETER_REQUEST_ERROR_BODY, e.toString())
                )
            )
            throw e
        }
    }
}