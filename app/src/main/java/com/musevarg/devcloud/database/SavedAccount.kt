package com.musevarg.devcloud.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedAccount(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    var accountName : String,
    val accessToken : String,
    val refreshToken : String,
    val instanceUrl : String,
    val scope : String,
    val tokenType : String
    ) {}