package com.example.codingchallenge.mapper

import com.example.codingchallenge.data.local.database.DatabaseUser
import com.example.codingchallenge.data.remote.ApiUser
import com.example.codingchallenge.domain.model.User

object UserMapper {
    fun ApiUser.toDatabaseUser(): DatabaseUser {
        return DatabaseUser(
            id = this.id,
            name = "${this.first_name} ${this.last_name}",
            avatar = this.avatar
        )
    }

    fun DatabaseUser.toDomainUser(): User {
        return User(
            id = this.id,
            name = this.name,
            avatar = this.avatar
        )
    }

    fun ApiUser.toDomainUser(): User {
        return User(
            id = this.id,
            name = "${this.first_name} ${this.last_name}",
            avatar = this.avatar
        )
    }
}
