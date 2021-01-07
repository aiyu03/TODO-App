package com.aatech.todoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {


    fun getTask(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
        when (sortOrder) {
            SortOrder.BY_DATE -> getTasksSortByDate(query, hideCompleted)
            SortOrder.BY_NAME -> getTasksSortByName(query, hideCompleted)
        }

    @Query("SELECT * FROM TASK_TABLE WHERE(completed!= :hideCompleted or completed =0) AND name  LIKE '%' || :searchQuery || '%'ORDER BY importance DESC,name")
    fun getTasksSortByName(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM TASK_TABLE WHERE(completed!= :hideCompleted or completed =0) AND name LIKE '%' || :searchQuery || '%' ORDER BY importance DESC,created")
    fun getTasksSortByDate(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task_table WHERE completed =1")
    suspend fun deleteCompletedTask()

}