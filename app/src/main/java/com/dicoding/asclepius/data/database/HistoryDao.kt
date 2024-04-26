package com.dicoding.asclepius.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(history: History)

    @Update
    fun update(history: History)

    @Query("SELECT * from History ORDER BY id ASC")
    fun getAllHistories(): LiveData<List<History>>
}