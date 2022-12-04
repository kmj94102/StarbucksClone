package com.example.starbucksclone.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// id: String 아이디
// password: String 비밀번호
// appPassword: String 앱 비밀번호
// nickname: String 닉네임
// phone: String 전화번호
// email: String 이메일
// birthday: String 생년월일
// sex : String 성별
// pushConsent : Boolean 푸시 알림
// eventConsent : Boolean 프로모션/이벤트 알림 수신
// locationConsent : Boolean 위치 정보 서비스 이용약관 동의
// shakePayConsent : Boolean shake to pay 설정
@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val index: Long,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "appPassword") val appPassword: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "nickname") val nickname: String,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "birthday") val birthday: String,
    @ColumnInfo(name = "sex") val sex: String,
    @ColumnInfo(name = "pushConsent") val pushConsent: Boolean,
    @ColumnInfo(name = "eventConsent") val eventConsent: Boolean,
    @ColumnInfo(name = "locationConsent") val locationConsent: Boolean,
    @ColumnInfo(name = "shakePayConsent") val shakePayConsent: Boolean
)

data class SignupInfo(
    var id: String = "",
    var password: String = "",
    var appPassword: String = "",
    var name: String = "",
    var nickname: String = "",
    var phone: String = "",
    var email: String = "",
    var birthday: String = "",
    var sex: String = "",
    var pushConsent: Boolean = false,
    var eventConsent: Boolean = false,
    var locationConsent: Boolean = false,
    var shakePayConsent: Boolean = false,
) {
    fun mapper() = UserEntity(
        index = 0,
        id = id,
        password = password,
        appPassword = appPassword,
        name = name,
        nickname = nickname,
        phone = phone,
        email = email,
        birthday = birthday,
        sex = sex,
        pushConsent = pushConsent,
        eventConsent = eventConsent,
        locationConsent = locationConsent,
        shakePayConsent = shakePayConsent
    )
}

data class LoginInfo(
    var id: String = "",
    var password: String = ""
)

data class SignupCompleteInfo(
    val name: String = "",
    val nickname: String = "",
    val pushConsent: Boolean = false
)

data class BriefUserInfo(
    val id: String = "",
    val name: String = "",
    val nickname: String = ""
)