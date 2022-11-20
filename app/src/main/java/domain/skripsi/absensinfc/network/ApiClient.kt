package domain.skripsi.absensinfc.network

import android.content.Context
import domain.skripsi.absensinfc.utils.Constant
import domain.skripsi.absensinfc.utils.PreferencesHelper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private const val URL = "${Constant.BASE_URL}/api/"

    val instancesNoToken: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }

    class SetContext(context: Context) {

        private val sharedPref: PreferencesHelper = PreferencesHelper(context)
        private val TOKEN: String? = sharedPref.getString(Constant.PREF_USER_TOKEN)

        private val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        private val client = OkHttpClient.Builder().apply {
            addInterceptor(Interceptor { chain ->
                val originalRequest: Request = chain.request()
                val newRequest: Request = originalRequest.newBuilder()
//                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer $TOKEN")
                    .build()
                chain.proceed(newRequest)
            })
            addInterceptor(interceptor)
        }.build()

        val instancesWithToken: ApiService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(ApiService::class.java)
        }
    }
}

