package com.musevarg.devcloud.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [SavedAccount::class],
    version = 3
)

abstract class SavedAccountDatabase : RoomDatabase(){
    abstract val dao : SavedAccountDao

    // Define a companion object to provide a singleton instance of the database
    companion object {
        @Volatile
        private var INSTANCE: SavedAccountDatabase? = null
        fun getInstance(context: Context?): SavedAccountDatabase? {
            return INSTANCE ?: synchronized(this) {
                val instance = context?.let {
                    Room.databaseBuilder(
                        it.applicationContext,
                        SavedAccountDatabase::class.java,
                        "saved_account_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                } // <---- The crash occurs here
                INSTANCE = instance
                return instance
            }
        }
    }
}