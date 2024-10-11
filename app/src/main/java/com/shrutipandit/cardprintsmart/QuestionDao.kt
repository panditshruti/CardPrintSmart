package com.shrutipandit.cardprintsmart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.shrutipandit.cardprintsmart.db.Question

@Dao
interface QuestionDao {
    @Insert
    suspend fun insert(question: Question)

    @Query("SELECT * FROM questions")
    suspend fun getAllQuestions(): List<Question>

    @Query("DELETE FROM questions")
    suspend fun deleteAll()
}
