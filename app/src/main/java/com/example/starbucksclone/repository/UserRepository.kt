package com.example.starbucksclone.repository

import android.content.SharedPreferences
import com.example.starbucksclone.database.client.UserClient
import com.example.starbucksclone.database.entity.LoginInfo
import com.example.starbucksclone.database.entity.UserEntity
import com.example.starbucksclone.di.setLoginId
import com.example.starbucksclone.di.setLoginNickname
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val client: UserClient,
    private val pref: SharedPreferences
) {

    /** 유저 등록 **/
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

    /** 로그인 **/
    suspend fun login(
        loginInfo: LoginInfo,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.login(
            loginInfo = loginInfo,
            successListener = {
                if (it != null) {
                    pref.setLoginId(it.id)
                    pref.setLoginNickname(it.nickname.ifEmpty { it.name })
                    successListener()
                } else {
                    failureListener()
                }
            },
            failureListener = failureListener
        )
    }

    /** 로그아웃 **/
    fun logout() {
        pref.setLoginId("")
        pref.setLoginNickname("")
    }

    /** 회원가입 완료 정보 조회 **/
    suspend fun selectSignupCompleteInfo(
        id: String,
        successListener: (String, Boolean) -> Unit,
        failureListener: () -> Unit
    ) {
        client.selectSignupCompleteInfo(
            id = id,
            successListener = {
                successListener(it.nickname.ifEmpty { it.name }, it.pushConsent)
            },
            failureListener = failureListener
        )
    }

}