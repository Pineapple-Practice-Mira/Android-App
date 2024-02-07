package site.pnpl.mira.di.modules

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import site.pnpl.mira.BuildConfig
import site.pnpl.mira.data.remote.ApiConstants
import site.pnpl.mira.data.remote.MiraApi
import site.pnpl.mira.domain.analitycs.Analytics
import site.pnpl.mira.utils.AnalyticsInterceptor
import javax.inject.Singleton

@Module
class RemoteModule {

    @Singleton
    @Provides
    fun provideAnalyticsInterceptor(analytics: Analytics): AnalyticsInterceptor = AnalyticsInterceptor(analytics)

    @Singleton
    @Provides
    fun provideOkHttpClient(analyticsInterceptor: AnalyticsInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        })
        .addInterceptor(analyticsInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideMiraApi(retrofit: Retrofit): MiraApi = retrofit.create(MiraApi::class.java)
}