package com.dicoding.asclepius.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
class History(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "first_category")
    var first_category: String? = null,

    @ColumnInfo(name = "second_category")
    var second_category: String? = null,

    @ColumnInfo(name = "date")
    var date: String? = null
) : Parcelable