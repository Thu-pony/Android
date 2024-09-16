package com.example.jetpacktest

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert
    fun Insert(user: User):Long


    @Update
    fun Update(newUser: User)

    @Query("select * from User where age > :age")
    fun loadUserOlderThan(age:Int):List<User>

    @Delete
    fun deleteUser(user: User)

}