package com.example.codingchallenge.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.codingchallenge.data.local.database.DatabaseUser

@Dao
interface UserDao {

    @Query("SELECT * FROM DatabaseUser")
    fun getAll(): List<DatabaseUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<DatabaseUser>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: DatabaseUser)

    @Query("SELECT * FROM DatabaseUser WHERE id = :userId")
    suspend fun getUser(userId: Int): DatabaseUser?
}