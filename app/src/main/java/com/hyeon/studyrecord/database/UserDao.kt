package com.hyeon.studyrecord.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hyeon.studyrecord.database.User

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM User")
    fun getAll() : List<User>
}

// Data Access Object