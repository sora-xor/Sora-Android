package jp.co.soramitsu.common.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class Sora2ApiCreator(
    private val okHttpClient: OkHttpClient,
    private val baseUrl: String,
) {
    fun <T> create(service: Class<T>): T {
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        return retrofit.create(service)
    }
}
