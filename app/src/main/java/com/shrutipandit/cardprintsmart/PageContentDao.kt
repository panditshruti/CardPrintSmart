package com.shrutipandit.cardprintsmart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shrutipandit.cardprintsmart.db.PageContent
import com.shrutipandit.cardprintsmart.db.PageSummary
import com.shrutipandit.cardprintsmart.db.Question

@Dao
interface PageContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPageContent(pageContent: PageContent)

    @Query("SELECT * FROM PageContent WHERE id = :id")
    suspend fun getPageContentById(id: kotlin.Long): PageContent?

    @Query("UPDATE PageContent SET question = :questions WHERE id = :id")
    suspend fun updatePageContentQuestions(id: Long, questions: List<Question>)

    @Query("SELECT id, title, description FROM PageContent")
    suspend fun getAllPageSummaries(): MutableList<PageSummary>
}
