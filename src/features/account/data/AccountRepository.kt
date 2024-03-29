package com.example.features.account.data

import com.example.database.user.UserDataSource
import com.example.model.Address
import com.example.model.User

class AccountRepository(
    private val userDataSource: UserDataSource
) {
    suspend fun getUser(email: String): User {
        return userDataSource.getUser(email)
    }

    suspend fun updateUser(user: User) {
        userDataSource.updateUser(user)
    }

    suspend fun updateUsername(email: String, username: String) {
        val user = userDataSource.getUser(email)
        user.username = username
        userDataSource.updateUser(user)
    }

    suspend fun updateAddress(email: String, address: Address) {
        val user = userDataSource.getUser(email)
        user.address = address
        userDataSource.updateUser(user)
    }
}