package com.hajizade.lingualmate.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_comments")
data class UserCommentEntity(
    @PrimaryKey
    val id: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorAvatarUrl: String? = null,
    val date: String = "",
    val comment: String = ""
)