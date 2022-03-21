package com.example.joshu.mvp.model.entity.room.dao

import androidx.room.*
import com.example.joshu.mvp.model.entity.room.TaskFolder

@Dao
interface TaskFolderDao {
    @Query("SELECT * from task_folder_table")
    suspend fun getAll(): List<TaskFolder>

    @Query("SELECT * from task_folder_table WHERE id = 1")
    suspend fun getDefault(): TaskFolder

    @Query("SELECT * from task_folder_table WHERE id = :id")
    suspend fun getById(id: Int): TaskFolder

    @Query("SELECT * from task_folder_table WHERE id IN (:ids)")
    suspend fun getByFilteredId(ids: List<Int>): List<TaskFolder>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskFolder: TaskFolder) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(taskFolderList: List<TaskFolder>)

    @Update
    suspend fun update(taskFolder: TaskFolder)

    @Query("DELETE FROM task_folder_table")
    suspend fun deleteAll()

    @Query("DELETE FROM task_folder_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT createDate FROM task_folder_table ORDER BY createDate DESC LIMIT 1")
    suspend fun getLatestCreateDate(): Long?
}