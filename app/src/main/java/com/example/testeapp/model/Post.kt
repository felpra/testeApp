package com.example.testeapp.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "posts")
data class Post(
    val userId: Int,
    @NonNull
    @PrimaryKey
    val id: Int,
    val title: String,
    val body: String,
    var isFavorite: Boolean = false
): Parcelable
