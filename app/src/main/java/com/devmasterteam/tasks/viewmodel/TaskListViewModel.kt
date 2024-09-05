package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.FunctionResponse
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    private val _delete = MutableLiveData<FunctionResponse>()
    val delete: LiveData<FunctionResponse> = _delete

    fun getTasks() {
        taskRepository.getTasks(object: APIListener<List<TaskModel>> {
            override fun onSucess(result: List<TaskModel>) {
                result.forEach {
                    it.priorityDescription = priorityRepository.getDescription(it.priorityId)
                }

                _tasks.value = result
            }

            override fun onFailure(message: String) {}

        })
    }

    fun deleteTask(id: Int) {
        taskRepository.deleteTask(id, object : APIListener<Boolean> {
            override fun onSucess(result: Boolean) {
                getTasks()
            }

            override fun onFailure(message: String) {
                _delete.value = FunctionResponse(message, false)
            }

        })
    }

}