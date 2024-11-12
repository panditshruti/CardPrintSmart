package com.shrutipandit.cardprintsmart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shrutipandit.cardprintsmart.db.PageContent
import com.shrutipandit.cardprintsmart.db.Question

@Dao
interface PageContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPageContent(pageContent: PageContent)

    @Query("SELECT * FROM PageContent WHERE id = :id")
    suspend fun getPageContentById(id: Long): PageContent?

    @Query("UPDATE PageContent SET question = :questions WHERE id = :id")
    suspend fun updatePageContentQuestions(id: Long, questions: List<Question>)
}
