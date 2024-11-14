package com.shrutipandit.cardprintsmart.viewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shrutipandit.cardprintsmart.AppDatabase

import com.shrutipandit.cardprintsmart.db.PageSummary
import kotlinx.coroutines.launch

class PageViewModel(application: Application) : AndroidViewModel(application) {

    private val _pageSummaries = MutableLiveData<List<PageSummary>>()
    val pageSummaries: LiveData<List<PageSummary>> get() = _pageSummaries

    init {
        getPageSummary()
    }

    private fun getPageSummary() {
        viewModelScope.launch {
            val db = AppDatabase.getDatabase(getApplication())
            val summaries = db.pageContentDao().getAllPageSummaries()
            _pageSummaries.postValue(summaries)
        }
    }
}
