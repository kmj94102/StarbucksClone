package com.example.starbucksclone.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.starbucksclone.database.entity.OrderMenuEntity
import com.example.starbucksclone.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StarbucksDao {

    /** 유저 등록 **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    /** 로그인 **/
    @Query("SELECT COUNT(*) FROM UserEntity WHERE id = :id AND password = :password")
    suspend fun login(id: String, password: String): Int

    /** Order 메뉴 아이템 등록 **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderMenu(menuList: List<OrderMenuEntity>)

    /** Order 메뉴 조회 **/
    @Query("SELECT * FROM OrderMenuEntity")
    fun selectOrderMenu(): Flow<List<OrderMenuEntity>>

}