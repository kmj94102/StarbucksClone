package com.example.starbucksclone.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.starbucksclone.database.entity.DrinkDetailEntity
import com.example.starbucksclone.database.entity.DrinkEntity
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

    /** 음료 등록 **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrinks(drinkList: List<DrinkEntity>)

    /** 음료 조회 **/
    @Query("SELECT * FROM DrinkEntity WHERE `group` = :group")
    fun selectDrinks(group: String): Flow<List<DrinkEntity>>

    /** 음료 상세 등록 **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrinkDetails(detailList: List<DrinkDetailEntity>)

    /** 음료 상세 조회 **/
    @Query("SELECT * FROM DrinkDetailEntity WHERE `index` = :index")
    fun selectDrinkDetail(index: String): Flow<List<DrinkDetailEntity>>

}