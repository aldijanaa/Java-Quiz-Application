package com.example.quizapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update



//Interface for managing user-related operations <-----------
// serves as abstraction layer for the app to interact with Room database
@Dao
interface UserDao {
    @Insert
    suspend fun register(user: User)

    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    suspend fun login(username: String, password: String): User?

    @Query("SELECT * FROM user WHERE username = :username")
    suspend fun getUser(username: String): User?

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM user WHERE username = :username")
    fun getUserLiveData(username: String): LiveData<User?>

    //UserDao interface is used in conjunction with Room to define the database operations specific to the User entity.

    // @Insert, @Query, @Update --> Room annotations for the operations


    //WHAT IS Room database?
    //Room is an Android library that provides an abstraction layer over SQLite and simplifies working with databases.

}
