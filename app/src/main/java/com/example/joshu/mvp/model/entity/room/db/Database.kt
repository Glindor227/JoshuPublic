package com.example.joshu.mvp.model.entity.room.db

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.joshu.R
import com.example.joshu.mvp.model.entity.DateRepeatEnum
import com.example.joshu.mvp.model.entity.DeleteStatusEnum
import com.example.joshu.mvp.model.entity.TaskFolderFactory
import com.example.joshu.mvp.model.entity.TaskStatusEnum
import com.example.joshu.mvp.model.entity.room.Task
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.mvp.model.entity.room.dao.TaskDao
import com.example.joshu.mvp.model.entity.room.dao.TaskFolderDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@androidx.room.Database(entities = [Task::class, TaskFolder::class], version = 6)
abstract class Database: RoomDatabase() {
    companion object {
        private const val TABLE_NAME = "task_database"

        private val parentJob = Job()
        private val coroutineContext = parentJob + Dispatchers.Default
        private val scope = CoroutineScope(coroutineContext)

        @Volatile
        private var INSTANCE: Database? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE task_table ADD COLUMN status INTEGER NOT NULL DEFAULT ${TaskStatusEnum.DEFAULT.value}" )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE task_table ADD COLUMN repeatType INTEGER NOT NULL DEFAULT ${DateRepeatEnum.DEFAULT.value}" )
                database.execSQL("ALTER TABLE task_table ADD COLUMN deleteStatus INTEGER NOT NULL DEFAULT ${DeleteStatusEnum.DEFAULT.value}" )
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE task_folder_table ADD COLUMN createDate INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE task_table ADD COLUMN createDate INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
                database.execSQL("ALTER TABLE task_table ADD COLUMN editDate INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE task_folder_table ADD COLUMN editDate INTEGER NOT NULL DEFAULT 0")
            }

        }

        fun getDatabase(context: Context): Database {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    TABLE_NAME
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        scope.launch {
                            INSTANCE?.taskFolderDao?.insertAll(
                                listOf(
                                    TaskFolderFactory.create(
                                        context.getString(R.string.folder_work),
                                        getColorStringByResource(context, R.color.color_folder_work), 0L),
                                    TaskFolderFactory.create(
                                        context.getString(R.string.folder_home),
                                        getColorStringByResource(context, R.color.color_folder_home), 0L),
                                    TaskFolderFactory.create(
                                        context.getString(R.string.folder_study),
                                        getColorStringByResource(context, R.color.color_folder_study), 0L),
                                    TaskFolderFactory.create(
                                        context.getString(R.string.folder_health),
                                        getColorStringByResource(context, R.color.color_folder_health), 0L),
                                    TaskFolderFactory.create(
                                        context.getString(R.string.folder_recreation),
                                        getColorStringByResource(context, R.color.color_folder_recreation), 0L)
                                )
                            )
                        }
                    }
                }).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                .build()
                INSTANCE = instance
                return instance
            }
        }

        private fun getColorStringByResource(context: Context, @ColorRes color: Int): String =
            String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(context, color))
    }

    abstract val taskDao: TaskDao
    abstract val taskFolderDao: TaskFolderDao
}