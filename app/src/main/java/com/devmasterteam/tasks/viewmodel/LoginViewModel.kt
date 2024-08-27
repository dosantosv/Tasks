package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.FunctionResponse
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application.applicationContext)
    private val prioriryRepository = PriorityRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _login = MutableLiveData<FunctionResponse>()
    val login: LiveData<FunctionResponse> = _login

    private val _loggedUser = MutableLiveData<FunctionResponse>()
    val loggedUser: LiveData<FunctionResponse> = _loggedUser

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object : APIListener<PersonModel> {
            override fun onSucess(result: PersonModel) {
                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, result.name)

                RetrofitClient.addHeaders(result.token, result.personKey)

                _login.value = FunctionResponse()
            }

            override fun onFailure(message: String) {
                _login.value = FunctionResponse(message, false)
            }
        })
    }

    fun verifyLoggedUser() {
        val token = securityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val person = securityPreferences.get(TaskConstants.SHARED.PERSON_KEY)

        RetrofitClient.addHeaders(token, person)

        val logged = (token == "" && person == "")

        if (logged)
            _loggedUser.value = FunctionResponse("Usuário não está logado!", false)

        if (!logged) {
            prioriryRepository.list(object : APIListener<List<PriorityModel>> {
                override fun onSucess(result: List<PriorityModel>) {
                    prioriryRepository.save(result)
                }

                override fun onFailure(message: String) {
                    _loggedUser.value = FunctionResponse()
                }

            })
        }
    }

}