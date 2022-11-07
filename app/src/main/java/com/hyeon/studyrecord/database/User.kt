package com.hyeon.studyrecord.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @ColumnInfo(name = "user_id") val userId:String,
    @ColumnInfo(name = "user_pwd") val userPwd:String,
    @ColumnInfo(name = "user_nick") val userNick:String
){
    @PrimaryKey(autoGenerate = true) var id:Int = 0
}
// SQLite의 table 및 열 이름은 대소문자 구분 x

// 주생성자로 생성한 property 는 getter ,setter 가능하다.