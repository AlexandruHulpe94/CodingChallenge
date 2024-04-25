package com.example.codingchallenge.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseUser(
    @PrimaryKey val id: Int,
    val name: String,
    val avatar: String
)