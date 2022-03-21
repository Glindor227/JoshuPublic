package com.example.joshu.mvp.model.repo

import com.example.joshu.mvp.model.api.network.Result
import com.example.joshu.mvp.model.api.network.error.ServerException
import com.example.joshu.mvp.model.api.request.taskFolder.millisToSeconds
import com.example.joshu.mvp.model.entity.DeleteStatusEnum
import com.example.joshu.mvp.model.entity.room.dao.TaskDao
import com.example.joshu.mvp.model.entity.room.dao.TaskFolderDao

class SynchronizeRepo(private val taskDao: TaskDao,
                        private val taskFolderDao: TaskFolderDao,
                        private val taskRepo: ITaskRepo,
                        private val taskFolderRepo: ITaskFolderRepo)  {
    suspend fun synchronize(): Result<Unit> {
        deleteOnServer()
        synchronizeTaskFolders()
        synchronizeTasks()
        return Result.Success(Unit)
    }

    private suspend fun synchronizeTaskFolders(): Result<Unit> {
        val lastTaskFolderDate = taskFolderDao.getLatestCreateDate() ?: 0

        if (lastTaskFolderDate == 0L) {
            return when(val response = taskFolderRepo.getAllTaskFolders(lastTaskFolderDate)) {
                is Result.Success -> {
                    taskFolderDao.insertAll(response.data)
                    Result.Success(Unit)
                }

                is Result.Error -> {
                    response
                }
            }
        } else {
            when (val taskFoldersResult = taskFolderRepo.getAllTaskFolders(lastTaskFolderDate)) {
                is Result.Success -> {
                    if (taskFoldersResult.data.isEmpty()) {
                        taskFolderDao.getAll().forEach {
                            taskFolderRepo.sendTaskFolder(it)
                        }
                    } else {
                        val localTaskFolders = taskFolderDao.getAll()
                        taskFoldersResult.data.filter { task -> localTaskFolders.map { it.id }.contains(task.id) }.forEach { taskFolder ->
                            if (taskFolderDao.getById(taskFolder.id).editDate < taskFolder.editDate) {
                                taskFolderDao.update(taskFolder)
                            } else {
                                taskFolderRepo.updateTaskFolder(taskFolder)
                            }
                        }
                        taskFolderDao.insertAll(taskFoldersResult.data.filter { taskFolder -> !localTaskFolders.map { it.id }.contains(taskFolder.id) })
                        localTaskFolders.filter { taskFolder -> taskFolder.editDate > 0 }.forEach { taskFolder ->
                            taskFolderRepo.updateTaskFolder(taskFolder)
                        }
                    }
                    return Result.Success(Unit)
                }

                is Result.Error -> {
                    return taskFoldersResult
                }
            }
        }
    }

    private suspend fun synchronizeTasks(): Result<Unit> {
        val lastTaskDate = taskDao.getLatestCreateDate() ?: 0

        if (lastTaskDate == 0L) {
            when(val response = taskRepo.getAllTasks(lastTaskDate)) {
                is Result.Success -> {
                    taskDao.insert(response.data)
                    return Result.Success(Unit)
                }

                is Result.Error -> {
                    return response
                }
            }
        } else {
            when (val tasksResult = taskRepo.getAllTasks(lastTaskDate)) {
                is Result.Success -> {
                    if (tasksResult.data.isEmpty()) {
                        taskDao.getAll().forEach {
                            taskRepo.sendTask(it)
                        }
                    } else {
                        val localTasks = taskDao.getAll()
                        tasksResult.data.filter { task -> localTasks.map { it.id }.contains(task.id) }.forEach { task ->
                            if (taskDao.getById(task.id).editDate < task.editDate) {
                                taskDao.update(task)
                            } else {
                                taskRepo.updateTask(task)
                            }
                        }

                        taskDao.insert(tasksResult.data.filter { task -> !localTasks.map { it.id }.contains(task.id) })

                        localTasks.filter { task -> task.editDate > 0 }.forEach { task ->
                            taskRepo.updateTask(task)
                        }
                    }
                    return Result.Success(Unit)
                }

                is Result.Error -> {
                    return tasksResult
                }
            }
        }
    }

    private suspend fun deleteOnServer() {
        taskDao.getAll().filter { task ->  task.deleteStatus == DeleteStatusEnum.Deleted.value }
            .forEach { task ->
                 val result = taskRepo.deleteTask(task.id)
                when (result) {
                    is Result.Success -> {
                        taskDao.deleteById(task.id)
                    }
                    is Result.Error -> {}
                }
            }
    }
}