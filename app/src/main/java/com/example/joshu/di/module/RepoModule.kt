package com.example.joshu.di.module

import com.example.joshu.mvp.model.ISharedPreferences
import com.example.joshu.mvp.model.api.IApiService
import com.example.joshu.mvp.model.api.IJwtParser
import com.example.joshu.mvp.model.api.ITaskApiService
import com.example.joshu.mvp.model.api.ITaskFolderApiService
import com.example.joshu.mvp.model.api.network.error.NetworkParserError
import com.example.joshu.mvp.model.entity.room.dao.TaskDao
import com.example.joshu.mvp.model.entity.room.dao.TaskFolderDao
import com.example.joshu.mvp.model.repo.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepoModule {
    @Provides
    @Singleton
    fun provideUserRepo(jwtParser: IJwtParser, apiService: IApiService,
                        sharedPreferences: ISharedPreferences, networkParserError: NetworkParserError): UserRepo {
        return UserRepo(apiService, jwtParser, sharedPreferences, networkParserError)
    }

    @Provides
    @Singleton
    fun provideTaskRepo(apiService: ITaskApiService,
                        sharedPreferences: ISharedPreferences, networkParserError: NetworkParserError): ITaskRepo {
        return TaskRepoImpl(apiService, sharedPreferences, networkParserError)
    }

    @Provides
    @Singleton
    fun provideTaskFolderRepo(apiService: ITaskFolderApiService,
                              sharedPreferences: ISharedPreferences, networkParserError: NetworkParserError): ITaskFolderRepo {
        return TaskFolderRepoImpl(apiService, sharedPreferences,networkParserError)
    }

    @Provides
    @Singleton
    fun provideSynchronizeRepo(taskDao: TaskDao, taskFolderDao: TaskFolderDao, taskRepo: ITaskRepo,
                               taskFolderRepo: ITaskFolderRepo): SynchronizeRepo {
        return SynchronizeRepo(taskDao, taskFolderDao, taskRepo, taskFolderRepo)
    }
}