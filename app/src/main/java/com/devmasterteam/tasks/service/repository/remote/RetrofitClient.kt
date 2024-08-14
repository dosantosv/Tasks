package com.devmasterteam.tasks.service.repository.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {
    companion object {
        private lateinit var _instance: Retrofit
        private fun getRetrofitInstance(): Retrofit
        {

            val httpClient = OkHttpClient.Builder()

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
    }
}