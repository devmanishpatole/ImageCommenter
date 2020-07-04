package com.devmanishpatole.imagecommenter.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Comment(
    @PrimaryKey
    val id: String,
    val comment: String
)