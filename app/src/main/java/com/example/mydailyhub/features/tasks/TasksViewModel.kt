package com.example.mydailyhub.features.tasks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class TaskItem(
    val id: Long,
    val title: String,
    val isDone: Boolean,
)

class TasksViewModel : ViewModel() {

    var taskInput by mutableStateOf("")
        private set

    private val _tasks = mutableStateListOf<TaskItem>()
    val tasks: List<TaskItem> get() = _tasks

    fun onTaskInputChanged(value: String) {
        taskInput = value
    }

    fun addTask() {
        val title = taskInput.trim()
        if (title.isEmpty()) return
        _tasks.add(
            TaskItem(
                id = System.currentTimeMillis(),
                title = title,
                isDone = false,
            ),
        )
        taskInput = ""
    }

    fun toggleTask(id: Long) {
        val index = _tasks.indexOfFirst { it.id == id }
        if (index == -1) return
        val task = _tasks[index]
        _tasks[index] = task.copy(isDone = !task.isDone)
    }

    fun removeTask(id: Long) {
        _tasks.removeAll { it.id == id }
    }
}
