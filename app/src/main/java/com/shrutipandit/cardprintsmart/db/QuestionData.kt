package com.shrutipandit.cardprintsmart.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val heading: String,
    val question: String,
    val option: String
)
