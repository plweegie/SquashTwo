package com.plweegie.android.squashtwo.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RepoEntry::class], version = 3)
abstract class RepoDatabase : RoomDatabase() {

    abstract fun repoDao(): RepoDao
}
