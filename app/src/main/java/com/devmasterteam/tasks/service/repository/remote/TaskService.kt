package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.model.TaskModel
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskService {

    @GET("Task")
    fun getTasks(): Call<List<TaskModel>>

    @GET("Task/Next7Days")
    fun getTasksNextSevenDays(): Call<List<TaskModel>>

    @GET("Task/Overdue")
    fun getTaskOverdue(): Call<List<TaskModel>>

    @GET("Task/{id}")
    fun getTaskPerId(@Path(value = "id", encoded = true)id: Int): Call<TaskModel>

    @POST("Task")
    @FormUrlEncoded
    fun createTask(
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean,
    ): Call<Boolean>

    @PUT("Task")
    @FormUrlEncoded
    fun updateTask(
        @Field("Id") id: Int,
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean,
    ): Call<Boolean>

    @PUT("Task/Complete")
    @FormUrlEncoded
    fun completeTask(@Field("Id") id: Int): Call<Boolean>

    @PUT("Task/Undo")
    @FormUrlEncoded
    fun undoTask(@Field("Id") id: Int): Call<Boolean>

    @HTTP(method = "DELETE", path = "Task", hasBody = true)
    @FormUrlEncoded
    fun deleteTask(@Field("Id") id: Int): Call<Boolean>
}