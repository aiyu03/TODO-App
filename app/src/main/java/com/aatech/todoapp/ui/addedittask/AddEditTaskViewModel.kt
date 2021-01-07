package com.aatech.todoapp.ui.addedittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aatech.todoapp.data.Task
import com.aatech.todoapp.data.TaskDao
import com.aatech.todoapp.ui.ADD_TASK_RESULT_OK
import com.aatech.todoapp.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditTaskViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
        set(value) {
            field = value
            state.set("taskName", taskName)
        }

    var taskDescription = state.get<String>("taskDescription") ?: task?.description ?: ""
        set(value) {
            field = value
            state.set("taskDescription", taskDescription)
        }
    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.importance ?: false
        set(value) {
            field = value
            state.set("taskName", taskImportance)
        }

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    fun onSavedClick() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Name Can't be Empty")
            return
        }
        if (taskDescription.isBlank()) {
            showInvalidInputMessage("Description Can't be Empty")
            return
        }
        if (task != null) {
            val updateTask = task.copy(
                name = taskName,
                description = taskDescription,
                importance = taskImportance
            )
            updateTask(updateTask)
        } else {
            val newTask =
                Task(name = taskName, description = taskDescription, importance = taskImportance)
            createTask(newTask)
        }
    }

    private fun showInvalidInputMessage(s: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidMessage(s))
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.update(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    private fun createTask(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidMessage(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }
}