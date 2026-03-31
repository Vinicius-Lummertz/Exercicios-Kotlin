package br.edu.satc.todolistcompose.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao
) {
    val tasks: Flow<List<TaskData>> = taskDao.getAllTasks()

    suspend fun insertTask(taskData: TaskData) {
        taskDao.insertTask(taskData)
    }

    suspend fun updateTask(taskData: TaskData) {
        taskDao.updateTask(taskData)
    }
}
