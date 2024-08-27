package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {
    companion object {
        private lateinit var _instance: Retrofit
        private var token: String = ""
        private var personKey: String = ""

        private fun getRetrofitInstance(): Retrofit {
            val httpClient = OkHttpClient.Builder()

            httpClient.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                        .newBuilder()
                        .addHeader(TaskConstants.HEADER.TOKEN_KEY, token)
                        .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                        .build()

                    return chain.proceed(request)
                }

            })

            if(!Companion::_instance.isInitialized) {
                synchronized(RetrofitClient::class.java) {
                    _instance = Retrofit.Builder()
                        .baseUrl("http://devmasterteam.com/CursoAndroidAPI/")
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }

            return _instance
        }

        fun <T> getService(serviceClass: Class<T>) : T {
            return getRetrofitInstance().create(serviceClass)
        }

        fun addHeaders(tokenValeue: String, personKeyValue: String) {
            this.token = tokenValeue
            this.personKey = personKeyValue
        }
    }
}