package br.edu.satc.todolistcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.edu.satc.todolistcompose.data.TaskData
import br.edu.satc.todolistcompose.data.TaskPreferences
import br.edu.satc.todolistcompose.data.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val taskPreferences: TaskPreferences
) : ViewModel() {

    private val showOnlyPendingState = MutableStateFlow(taskPreferences.getShowOnlyPending())

    val showOnlyPending: StateFlow<Boolean> = showOnlyPendingState

    val tasks: StateFlow<List<TaskData>> = combine(
        taskRepository.tasks,
        showOnlyPendingState
    ) { allTasks, showOnlyPending ->
        if (showOnlyPending) {
            allTasks.filter { !it.complete }
        } else {
            allTasks
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun addTask(title: String, description: String) {
        val trimmedTitle = title.trim()
        if (trimmedTitle.isBlank()) return

        viewModelScope.launch {
            taskRepository.insertTask(
                TaskData(
                    title = trimmedTitle,
                    description = description.trim(),
                    complete = false
                )
            )
        }
    }

    fun updateTaskComplete(taskData: TaskData, isComplete: Boolean) {
        viewModelScope.launch {
            taskRepository.updateTask(taskData.copy(complete = isComplete))
        }
    }

    fun updateShowOnlyPending(showOnlyPending: Boolean) {
        showOnlyPendingState.value = showOnlyPending
        taskPreferences.setShowOnlyPending(showOnlyPending)
    }
}

class TaskViewModelFactory(
    private val taskRepository: TaskRepository,
    private val taskPreferences: TaskPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskRepository, taskPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
