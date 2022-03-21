package com.example.joshu.mvp.model.entity.room.dao

import androidx.room.*
import com.example.joshu.mvp.model.entity.room.Task

@Dao
interface TaskDao {
    @Query("SELECT * from task_table ORDER BY dateTime ASC")
    suspend fun getAll(): List<Task>

    @Query("SELECT * from task_table WHERE dateTime >= :dateTimeFrom AND dateTime <= :dateTimeTo AND deleteStatus = :deleteStatus ORDER BY dateTime ASC")
    suspend fun getAllByBetweenDateTime(dateTimeFrom: Long, dateTimeTo: Long, deleteStatus: Int): List<Task>

    @Query("SELECT * FROM task_table WHERE status = :status AND deleteStatus = :deleteStatus")
    suspend fun getAllByStatus(status: Int, deleteStatus: Int): List<Task>

    @Query("SELECT * FROM task_table WHERE deleteStatus = :deleteStatus")
    suspend fun getAllByDeleteStatus(deleteStatus: Int): List<Task>

    @Query("SELECT * from task_table WHERE id = :id")
    suspend fun getById(id: Int): Task

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: List<Task>)

    @Update
    suspend fun update(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Query("DELETE FROM task_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM task_table WHERE status = :status AND deleteStatus = :deleteStatus")
    suspend fun getCountByStatus(status: Int, deleteStatus: Int): Int

    @Query("DELETE FROM task_table WHERE folderId = :folderId")
    suspend fun deleteByFolderId(folderId: Int)

    @Query("SELECT COUNT(*) FROM task_table WHERE status = :status")
    suspend fun getCountByStatus(status: Int): Int

    @Query("SELECT COUNT(*) FROM task_table WHERE folderId = :folderId AND deleteStatus = :deleteStatus")
    suspend fun getCountByFolder(folderId: Int, deleteStatus: Int): Int

    @Query("SELECT * FROM task_table WHERE folderId = :folderId AND deleteStatus = :deleteStatus")
    suspend fun getAllByFolder(folderId: Int, deleteStatus: Int): List<Task>

    @Query("SELECT createDate FROM task_table ORDER BY createDate LIMIT 1")
    suspend fun getLatestCreateDate(): Long?
}