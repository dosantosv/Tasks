package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(val context: Context) {

    private val remote = RetrofitClient.getService(TaskService::class.java)

    fun getTasks(listener: APIListener<List<TaskModel>>) {
        val call = remote.getTasks()
        callTasks(listener, call)
    }

    fun getTasksNextSevenDays(listener: APIListener<List<TaskModel>>) {
        val call = remote.getTasksNextSevenDays()
        callTasks(listener, call)
    }

    fun getTaskOverdue(listener: APIListener<List<TaskModel>>) {
        val call = remote.getTaskOverdue()
        callTasks(listener, call)
    }

    private fun callTasks (listener: APIListener<List<TaskModel>>, call: Call<List<TaskModel>>) {
        call.enqueue(object : Callback<List<TaskModel>> {
            override fun onResponse(call: Call<List<TaskModel>>, response: Response<List<TaskModel>>,
            ) {
                if(response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSucess(it) }
                } else {
                    listener.onFailure(failResponse(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<List<TaskModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }

    private fun failResponse(str: String) : String {
        return Gson().fromJson(str, String::class.java)
    }
}