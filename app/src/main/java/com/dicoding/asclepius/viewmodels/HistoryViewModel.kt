package com.dicoding.asclepius.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.database.History
import com.dicoding.asclepius.data.repository.HistoryRepository

class HistoryViewModel(application: Application) : ViewModel() {
    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)

    fun getAllHistories(): LiveData<List<History>> = mHistoryRepository.getAllHistories()

    fun insert(history: History) {
        mHistoryRepository.insert(history)
    }
}