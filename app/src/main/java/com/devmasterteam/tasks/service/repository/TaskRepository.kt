package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(val context: Context): BaseRepository() {

    private val remote = RetrofitClient.getService(TaskService::class.java)

    fun createTask(task: TaskModel, listener: APIListener<Boolean>) {
        val call = remote.createTask(task.id, task.description, task.dueDate, task.complete)
        callEnqueue(listener, call)
    }

    fun getTasks(listener: APIListener<List<TaskModel>>) {
        val call = remote.getTasks()
        callEnqueue(listener, call)
    }

    fun getTasksNextSevenDays(listener: APIListener<List<TaskModel>>) {
        val call = remote.getTasksNextSevenDays()
        callEnqueue(listener, call)
    }

    fun getTaskOverdue(listener: APIListener<List<TaskModel>>) {
        val call = remote.getTaskOverdue()
        callEnqueue(listener, call)
    }

    fun deleteTask(id: Int, listener: APIListener<Boolean>) {
        val call = remote.deleteTask(id)
        callEnqueue(listener, call)
    }

    private fun <T> callEnqueue (listener: APIListener<T>, call: Call<T>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>, ) {
                handleResponse(response, listener)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }
}