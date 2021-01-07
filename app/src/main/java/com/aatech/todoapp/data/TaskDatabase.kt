package com.aatech.todoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aatech.todoapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().taskDao()
            applicationScope.launch {
                dao.insert(
                    Task(
                        name = "Kotlin Tutorial",
                        description = "This is new Kotlin Description"
                    )
                )
                dao.insert(
                    Task(
                        name = "C Tutorial",
                        description = "This is new C Description new C Description This is new C Description new C Description"
                    )
                )
                dao.insert(
                    Task(
                        name = "Java Tutorial",
                        description = "This is new Java Description This is new Java Description This is new Java Description This is new Java Description This is new Java Description This is new Java Description",
                        importance = true
                    )
                )
                dao.insert(
                    Task(
                        name = "Call Elon Musk",
                        description = "Phone number :- 123455 Phone number :- 123455 Phone number :- 123455",
                        completed = true
                    )
                )
                dao.insert(
                    Task(
                        name = "Call Mom",
                        description = "Phone number :- 123455 Phone number :- 123455 Phone number :- 123455"
                    )
                )

                dao.insert(
                    Task(
                        name = "Call Dad",
                        description = "Phone number :- 123455 Phone number :- 123455 Phone number :- 123455"
                    )
                )
                dao.insert(
                    Task(
                        name = "Call Sister",
                        description = "Phone number :- 123455 Phone number :- 123455 Phone number :- 123455"
                    )
                )
                dao.insert(
                    Task(
                        name = "Call Nilay",
                        description = "Phone number :- 123455 Phone number :- 123455 Phone number :- 123455"
                    )
                )
            }
        }
    }
}