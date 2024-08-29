package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.FunctionResponse
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val taskRepository = TaskRepository(application.applicationContext)

    private val _priorityList = MutableLiveData<List<PriorityModel>>()
    val priorityList: LiveData<List<PriorityModel>> = _priorityList

    private val _taskSave = MutableLiveData<FunctionResponse>()
    val taskSave: LiveData<FunctionResponse> = _taskSave

    fun loadPriorities() {
        _priorityList.value = priorityRepository.list()
    }

    fun save(task: TaskModel) {
        taskRepository.createTask(task, object : APIListener<Boolean> {
            override fun onSucess(result: Boolean) {
                _taskSave.value = FunctionResponse()
            }

            override fun onFailure(message: String) {
                _taskSave.value = FunctionResponse(message, false)
            }

        })
    }
}