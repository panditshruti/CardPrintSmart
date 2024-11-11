package com.shrutipandit.cardprintsmart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shrutipandit.cardprintsmart.db.PageContent

@Dao
interface PageContentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPageContent(pageContent: PageContent)

    @Query("SELECT * FROM PageContent")
    suspend fun getAllPageContents(): List<PageContent>
}
