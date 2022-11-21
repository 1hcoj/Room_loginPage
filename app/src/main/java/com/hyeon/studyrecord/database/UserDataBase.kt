package com.hyeon.studyrecord.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// @Database 주석 : "DataBase" 와 연결된 "Data Entity class" 를 모두 나열
@Database(entities = [User::class], version = 1)

abstract class UserDataBase : RoomDatabase()  {
    abstract  fun userDao() : UserDao
    // SingleTon
    companion object{

        private var instance : UserDataBase? = null
        // 동기화
        @Synchronized
        fun getInstance(context:Context): UserDataBase?{
            if (instance == null){
                /*
                 *  함수명 : synchronized
                 *  매개변수 : lock ( ? )  : Any , block ( 실행할 소스 코드 블럭 ) : ( ) -> R
                 *  리턴형 : Unit ( 없음 )
                 *  함수설명 :
                 *      > lock 을 받아서 다른 Thread와 동시 접근이 불가능하도록 한다.
                 *      > block 내부의 코드를 동기화 방식으로 실행한다.
                 */
                synchronized(UserDataBase::class){
                    /*
                     *  함수명 : databaseBuilder
                     *  매개변수 :
                     *      > context ( ? ) : Context ,
                     *      > database ( DB 클래스 ) : Class ,
                     *      > name : String
                     *  리턴형 : DB를 생성할 수 있는 databaseBuilder
                     *  함수설명 :
                     *      > 이름이 name 인 DB를  context에 생성 ?
                     */
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