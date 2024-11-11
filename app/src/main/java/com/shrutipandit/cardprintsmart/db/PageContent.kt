package com.shrutipandit.cardprintsmart.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "PageContent")
data class PageContent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
   val question:List<Question>

)
