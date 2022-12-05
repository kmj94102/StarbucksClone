package com.example.starbucksclone.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.starbucksclone.database.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StarbucksDao {

    /** 유저 등록 **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    /** 로그인 **/
    @Query("SELECT id, name, nickname FROM UserEntity WHERE id = :id AND password = :password LIMIT 1")
    suspend fun login(id: String, password: String): BriefUserInfo?

    /** 회원가입 완료 정보 조회 **/
    @Query("SELECT name, nickname, pushConsent FROM UserEntity WHERE id = :id")
    suspend fun selectSignupCompleteInfo(id: String): SignupCompleteInfo

    /** Order 메뉴 아이템 등록 **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderMenu(menuList: List<OrderMenuEntity>)

    /** Order 메뉴 조회 **/
    @Query("SELECT * FROM OrderMenuEntity")
    fun selectOrderMenu(): Flow<List<OrderMenuEntity>>

    /** 음료 등록 **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrinks(drinkList: List<MenuEntity>)

    /** 음료 조회 **/
    @Query("SELECT * FROM MenuEntity WHERE `group` = :group")
    fun selectDrinks(group: String): Flow<List<MenuEntity>>

    /** 음료 상세 등록 **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrinkDetails(detailList: List<MenuDetailEntity>)

    /** 음료 상세 조회 **/
    @Query("SELECT * FROM MenuDetailEntity WHERE `index` = :firstIndex OR `index` = :secondIndex")
    fun selectDrinkDetail(firstIndex: String, secondIndex: String): Flow<List<MenuDetailEntity>>

    /** 카드 등록 **/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCard(cardEntity: CardEntity)

    /** 카드 리스트 조회 **/
    @Query("SELECT * FROM CardEntity WHERE id = :id ORDER BY representative DESC")
    fun selectCardList(id: String): Flow<List<CardEntity>>

    /** 대표 카드 수정 **/
    @Query("UPDATE CardEntity SET representative = :isRepresentative WHERE cardNumber = :cardNumber")
    suspend fun updateRepresentative(cardNumber: String, isRepresentative: Boolean)

    /** 카드 정보 조회 **/
    @Query("SELECT * FROM CardEntity WHERE cardNumber = :cardNumber")
    suspend fun selectCardInfo(cardNumber: String): CardEntity

    /** 카드 이름 변경 **/
    @Query("UPDATE CardEntity SET cardName = :cardName WHERE cardNumber = :cardNumber")
    suspend fun updateCardName(cardNumber: String, cardName: String)

}