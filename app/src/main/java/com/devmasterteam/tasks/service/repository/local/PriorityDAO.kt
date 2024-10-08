package com.devmasterteam.tasks.service.repository.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.remote.PriorityService

@Dao
interface PriorityDAO {

    @Insert
    fun save(list: List<PriorityModel>)

    @Query("SELECT * FROM Priority")
    fun list(): List<PriorityModel>

    @Query("SELECT description FROM Priority WHERE id = :id")
    fun getDescription(id: Int): String

    @Query("DELETE FROM Priority")
    fun clear()
}