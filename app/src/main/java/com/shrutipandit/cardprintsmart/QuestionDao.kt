package com.shrutipandit.cardprintsmart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.shrutipandit.cardprintsmart.db.QuestionData

@Dao
interface QuestionDao {
    @Insert
    suspend fun insert(question: QuestionData)

    @Query("SELECT * FROM questions")
    suspend fun getAllQuestions(): List<QuestionData>
}
