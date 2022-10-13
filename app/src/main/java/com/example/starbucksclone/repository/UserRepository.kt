package com.example.starbucksclone.repository

import com.example.starbucksclone.database.client.UserClient
import com.example.starbucksclone.database.entity.LoginInfo
import com.example.starbucksclone.database.entity.UserEntity
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val client: UserClient
) {

    suspend fun insertUser(
        user: UserEntity,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.insertUser(
            user = user,
            successListener = {
                successListener()
            },
            failureListener = failureListener
        )
    }

    suspend fun login(
        loginInfo: LoginInfo,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.login(
            loginInfo = loginInfo,
            successListener = {
                successListener()
            },
            failureListener = failureListener
        )
    }

}