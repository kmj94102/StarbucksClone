package com.example.starbucksclone.database.client

import com.example.starbucksclone.database.StarbucksDao
import com.example.starbucksclone.database.entity.LoginInfo
import com.example.starbucksclone.database.entity.UserEntity
import javax.inject.Inject

class UserClient @Inject constructor(
    private val dao: StarbucksDao
) {

    /** 유저 등록 **/
    suspend fun insertUser(
        user: UserEntity,
        successListener: (Unit) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(dao.insertUser(user))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    /** 로그인 **/
    suspend fun login(
        loginInfo: LoginInfo,
        successListener: (Int) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(dao.login(loginInfo.id, loginInfo.password))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}