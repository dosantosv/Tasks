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

class TaskRepository(context: Context): BaseRepository(context) {

    private val remote = RetrofitClient.getService(TaskService::class.java)

    fun createTask(task: TaskModel, listener: APIListener<Boolean>) {
        val call = remote.createTask(task.id, task.description, task.dueDate, task.complete)
        executeCall(listener, call)
    }

    fun getTasks(listener: APIListener<List<TaskModel>>) {
        val call = remote.getTasks()
        executeCall(listener, call)
    }

    fun getTasksNextSevenDays(listener: APIListener<List<TaskModel>>) {
        val call = remote.getTasksNextSevenDays()
        executeCall(listener, call)
    }

    fun getTaskOverdue(listener: APIListener<List<TaskModel>>) {
        val call = remote.getTaskOverdue()
        executeCall(listener, call)
    }

    fun deleteTask(id: Int, listener: APIListener<Boolean>) {
        val call = remote.deleteTask(id)
        executeCall(listener, call)
    }
}