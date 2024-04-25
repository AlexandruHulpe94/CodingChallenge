package com.example.codingchallenge.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.codingchallenge.data.local.dao.UserDao

@Database(entities = [DatabaseUser::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}