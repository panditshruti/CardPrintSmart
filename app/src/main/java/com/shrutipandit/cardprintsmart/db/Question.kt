package com.shrutipandit.cardprintsmart.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val answer: String,
    val options: String
)

data class Questions(
    val question: List<Question>
)