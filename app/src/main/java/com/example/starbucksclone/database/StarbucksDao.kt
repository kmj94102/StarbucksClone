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
    @Query("SELECT * FROM OrderMenuEntity WHERE `group` = :group")
    fun selectOrderMenu(group: String): Flow<List<OrderMenuEntity>>

    /** 메뉴 등록 **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuList(menuList: List<MenuEntity>)

    /** 메뉴 조회 **/
    @Query("SELECT * FROM MenuEntity WHERE `group` = :name AND orderGroup = :group")
    fun selectMenuList(group: String, name: String): Flow<List<MenuEntity>>

    /** 홈 화면 새로 나온 메뉴 조회 **/
    @Query("SELECT indexes, name, image FROM MenuEntity WHERE isNew = 1")
    fun selectHomeNewMenuList(): Flow<List<HomeNewMenu>>

    /** New 메뉴 조회 **/
    @Query("SELECT * FROM MenuEntity WHERE isNew = 1 AND orderGroup = :group")
    fun selectNewMenuList(group: String): Flow<List<MenuEntity>>

    /** 추천 메뉴 조회 **/
    @Query("SELECT * FROM MenuEntity WHERE isRecommendation = 1 AND orderGroup = :group")
    fun selectRecommendMenuList(group: String): Flow<List<MenuEntity>>

    /** 검색 메뉴 조회 **/
    @Query("SELECT name, nameEng, indexes, image, price, color, isBest FROM MenuEntity WHERE name like :name AND orderGroup like :group")
    suspend fun selectSearchMenuList(group: String, name: String): List<MenuSearchResult>

    /** 메뉴 상세 등록 **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuDetails(detailList: List<MenuDetailEntity>)

    /** 메뉴 상세 조회 **/
    @Query("SELECT * FROM MenuDetailEntity WHERE `index` IN (:indexList)")
    fun selectMenuDetail(indexList: List<String>): Flow<List<MenuDetailEntity>>
    @Query("SELECT detail.`index`, detail.name, detail.nameEng, detail.description, detail.image, detail.type, menu.size, menu.sizePrice, menu.color, menu.type as drinkType, menu.isBest\n" +
            "FROM MenuDetailEntity as detail, MenuEntity as menu \n" +
            "WHERE detail.`index` IN (:indexList) AND menu.name = :name")
    suspend fun selectMenuDetail(indexList: List<String>, name: String): List<MenuDetailInfoResult>

    /** 카드 등록 **/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCard(cardEntity: CardEntity)

    /** 카드 리스트 조회 **/
    @Query("SELECT * FROM CardEntity WHERE id = :id ORDER BY representative DESC")
    fun selectCardList(id: String): Flow<List<CardEntity>>

    /** 대표 카드 존재 여부 확인 **/
    @Query("SELECT COUNT(*) FROM CardEntity WHERE id = :id")
    suspend fun selectCountRepresentative(id: String): Int

    /** 대표 카드 수정 **/
    @Query("UPDATE CardEntity SET representative = :isRepresentative WHERE cardNumber = :cardNumber")
    suspend fun updateRepresentative(cardNumber: String, isRepresentative: Boolean)

    /** 카드 정보 조회 **/
    @Query("SELECT * FROM CardEntity WHERE cardNumber = :cardNumber")
    suspend fun selectCardInfo(cardNumber: String): CardEntity

    /** 카드 이름 변경 **/
    @Query("UPDATE CardEntity SET cardName = :cardName WHERE cardNumber = :cardNumber")
    suspend fun updateCardName(cardNumber: String, cardName: String)

    /** 카드 충전 **/
    @Query("UPDATE CardEntity SET balance = :balance WHERE cardNumber = :cardNumber")
    suspend fun updateBalance(cardNumber: String, balance: Long)

    /** 카드 삭제 **/
    @Query("DELETE FROM CardEntity WHERE cardNumber = :cardNumber")
    suspend fun deleteCard(cardNumber: String)

    /** 나만의 메뉴 추가 **/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMyMenu(myMenuEntity: MyMenuEntity)

    /** 나만의 메뉴 삭제 **/
    @Query("DELETE FROM MyMenuEntity WHERE `index` = :index")
    suspend fun deleteMyMenu(index: Int)

    /** 나만의 메뉴 조회 **/
    @Query("SELECT * FROM MyMenuEntity WHERE id = :id ORDER BY date")
    fun selectMyMenu(id: String): Flow<List<MyMenuEntity>>

    /** 장바구니 추가 **/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCartItem(cartEntity: CartEntity)

    /** 장바구니 조회 **/
    @Query("SELECT * FROM CartEntity WHERE id = :id ORDER BY date")
    fun selectCartItems(id: String): Flow<List<CartEntity>>

    /** 장바구니 조회 **/
    @Query("SELECT * FROM CartEntity WHERE id = :id ORDER BY date")
    suspend fun selectCartItemList(id: String): List<CartEntity>

    /** 장바구니 카운트 조회 **/
    @Query("SELECT COUNT(*) FROM CartEntity WHERE id = :id")
    fun selectCartItemsCount(id: String): Flow<Int>

    /** 장바구니 삭제 **/
    @Query("DELETE FROM CartEntity WHERE `index` = :index")
    suspend fun deleteCartItem(index: Int)

    /** 장바구니 전체 삭제 **/
    @Query("DELETE FROM CartEntity WHERE id = :id")
    suspend fun allDeleteCartItems(id: String)

    /** 이용내역 추가 **/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsageHistory(vararg usageHistoryEntity: UsageHistoryEntity)

    /** 이용내역 조회 **/
    @Query("SELECT * FROM UsageHistoryEntity WHERE id = :id AND cardNumber = :cardNumber ORDER BY date DESC")
    fun selectUsageHistoryList(id: String, cardNumber: String): Flow<List<UsageHistoryEntity>>

}