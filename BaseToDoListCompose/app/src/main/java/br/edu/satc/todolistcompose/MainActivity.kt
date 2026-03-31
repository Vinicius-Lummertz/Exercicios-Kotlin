package br.edu.satc.todolistcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.satc.todolistcompose.data.AppDatabase
import br.edu.satc.todolistcompose.data.TaskPreferences
import br.edu.satc.todolistcompose.data.TaskRepository
import br.edu.satc.todolistcompose.ui.screens.HomeScreen
import br.edu.satc.todolistcompose.ui.theme.ToDoListComposeTheme
import br.edu.satc.todolistcompose.viewmodel.TaskViewModel
import br.edu.satc.todolistcompose.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val taskRepository = TaskRepository(database.taskDao())
        val taskPreferences = TaskPreferences(applicationContext)
        val viewModelFactory = TaskViewModelFactory(taskRepository, taskPreferences)

        setContent {
            val taskViewModel: TaskViewModel = viewModel(factory = viewModelFactory)

            ToDoListComposeTheme {
                HomeScreen(viewModel = taskViewModel)
            }
        }
    }
}
