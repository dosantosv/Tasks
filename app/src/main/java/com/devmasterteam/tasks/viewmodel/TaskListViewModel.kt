package com.devmasterteam.tasks.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.FunctionResponse
import com.devmasterteam.tasks.service.model.FunctionResponseData
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private var taskFilter = 0

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    private val _delete = MutableLiveData<FunctionResponse>()
    val delete: LiveData<FunctionResponse> = _delete

    private val _status = MutableLiveData<FunctionResponse>()
    val status: LiveData<FunctionResponse> = _status

    @RequiresApi(Build.VERSION_CODES.M)
    fun getTasks(filter: Int) {

        val listener = object : APIListener<List<TaskModel>> {
            override fun onSucess(result: List<TaskModel>) {
                result.forEach {
                    it.priorityDescription = priorityRepository.getDescription(it.priorityId)
                }

                _tasks.value = result
            }

            override fun onFailure(message: String) {}

        }

        when(filter) {
            TaskConstants.FILTER.ALL -> taskRepository.getTasks(listener)
            TaskConstants.FILTER.NEXT -> taskRepository.getTasksNextSevenDays(listener)
            TaskConstants.FILTER.EXPIRED -> taskRepository.getTaskOverdue(listener)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun deleteTask(id: Int) {
        taskRepository.deleteTask(id, object : APIListener<Boolean> {
            override fun onSucess(result: Boolean) {
                getTasks(taskFilter)
            }

            override fun onFailure(message: String) {
                _delete.value = FunctionResponse(message, false)
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun status(id: Int, complete: Boolean) {
        val listener = object : APIListener<Boolean> {
            override fun onSucess(result: Boolean) {
                getTasks(taskFilter)
            }

            override fun onFailure(message: String) {
                _status.value = FunctionResponse(message, false)
            }
        }

        if(complete) {
            taskRepository.complete(id, listener)
        } else {
            taskRepository.undo(id, listener)
        }

    }

}