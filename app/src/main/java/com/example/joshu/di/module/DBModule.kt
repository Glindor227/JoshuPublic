package com.example.joshu.di.module

import android.content.Context
import com.example.joshu.mvp.model.entity.room.dao.TaskDao
import com.example.joshu.mvp.model.entity.room.dao.TaskFolderDao
import com.example.joshu.mvp.model.entity.room.db.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DBModule {
    @Provides
    @Singleton
    fun provideRoomDB(context: Context): Database {
        return Database.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideRoomDBTaskDao(db: Database): TaskDao {
        return db.taskDao
    }

    @Provides
    @Singleton
    fun provideRoomDBTaskFolderDao(db: Database): TaskFolderDao {
        return db.taskFolderDao
    }
}