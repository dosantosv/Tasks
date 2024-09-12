package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.M)
    fun createTask(task: TaskModel, listener: APIListener<Boolean>) {

        isConnectionAvailable(listener)

        val call = remote.createTask(task.id, task.description, task.dueDate, task.complete)
        executeCall(listener, call)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun updateTask(task: TaskModel, listener: APIListener<Boolean>) {

        isConnectionAvailable(listener)

        val call = remote.updateTask(task.id, task.priorityId, task.description, task.dueDate, task.complete)
        executeCall(listener, call)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getTasks(listener: APIListener<List<TaskModel>>) {

        isConnectionAvailable(listener)

        val call = remote.getTasks()
        executeCall(listener, call)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getTasksNextSevenDays(listener: APIListener<List<TaskModel>>) {

        isConnectionAvailable(listener)

        val call = remote.getTasksNextSevenDays()
        executeCall(listener, call)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getTaskPerId(id: Int, listener: APIListener<TaskModel>) {

        isConnectionAvailable(listener)

        val call = remote.getTaskPerId(id)
        executeCall(listener, call)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getTaskOverdue(listener: APIListener<List<TaskModel>>) {

        isConnectionAvailable(listener)

        val call = remote.getTaskOverdue()
        executeCall(listener, call)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun deleteTask(id: Int, listener: APIListener<Boolean>) {

        isConnectionAvailable(listener)

        val call = remote.deleteTask(id)
        executeCall(listener, call)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun complete(id: Int, listener: APIListener<Boolean>) {

        isConnectionAvailable(listener)

        val call = remote.completeTask(id)
        executeCall(listener, call)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun undo(id: Int, listener: APIListener<Boolean>) {

        isConnectionAvailable(listener)

        val call = remote.undoTask(id)
        executeCall(listener, call)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun <T> isConnectionAvailable(listener: APIListener<T>) : APIListener<T> {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return listener
        }
        return listener
    }
}