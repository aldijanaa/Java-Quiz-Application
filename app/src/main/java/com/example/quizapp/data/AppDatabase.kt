package com.example.quizapp.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
//AppDatabase class is responsible for creating and managing the Room database instance <-----------
//OBJECT that represents the app's database and provides functions for initializing and accessing database (DAOs)
@Database(entities = [User::class], version = 1)
        abstract class AppDatabase : RoomDatabase() {  //uses Room database library
            //Room requires defining entities (tables in database)
            //User class defines the entity for the user table
            abstract fun userDao(): UserDao
            companion object {
                @Volatile
                private var INSTANCE: AppDatabase? = null

                //creates instance of the database
                fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "quiz_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

