package com.musevarg.devcloud.database

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface SavedAccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSavedAccount(savedAccount: SavedAccount)

    @Update
    fun updateSavedAccount(savedAccount: SavedAccount)

    @Transaction
    fun upsertSavedAccount(savedAccount: SavedAccount) {
        if (savedAccount.id == 0) {
            insertSavedAccount(savedAccount)
        } else {
            updateSavedAccount(savedAccount)
        }
        Log.d("BrowserConnect", "Upsert ran (saved ${savedAccount.accountName})")
    }

    @Delete
    fun deleteSavedAccount(savedAccount : SavedAccount)

    @Query("SELECT * FROM SavedAccount ORDER BY accountName ASC")
    fun getAllSavedAccounts() : List<SavedAccount>
}