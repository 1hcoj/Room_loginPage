package com.hyeon.studyrecord.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hyeon.studyrecord.database.User

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user : User)

    /*
    *   SELECT : 특정 열 검색하기 ( 테이블의 원소 )
    *   FROM : 데이터가 저장된 테이블 이름 ( ? )
    *   WHERE : 특정 행 검색하기
    *   PARAMETER : ':' 사용 !!
    * */
    @Query("SELECT EXISTS(SELECT user_id FROM user WHERE user_id = :enterId)")
    fun checkUser(enterId : String) : Boolean

    @Query("SELECT user_pwd FROM user WHERE user_id = :enterId")
    fun getPwd(enterId : String) : String

    @Query("SELECT * FROM user")
    fun getAll(): List<User>
}

// Data Access Object