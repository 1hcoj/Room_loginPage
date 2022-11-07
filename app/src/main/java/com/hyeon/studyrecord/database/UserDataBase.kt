package com.hyeon.studyrecord.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


// @Database 주석 : "DataBase" 와 연결된 "Data Entity class" 를 모두 나열
@Database(entities = [User::class], version = 1)
// 추상 클래스 ??
// 상속 받아서 사용?
// 추상 메소드
// 구현..?
abstract class UserDataBase : RoomDatabase()  {
    abstract  fun userDao() : UserDao

    companion object{
        private var instance : UserDataBase? = null

        @Synchronized
        fun getInstance(context:Context): UserDataBase?{
            if (instance == null){
                synchronized(UserDataBase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDataBase::class.java,
                        "user-database"
                    ).build()
                }
            }
            return instance
        }

    }

}