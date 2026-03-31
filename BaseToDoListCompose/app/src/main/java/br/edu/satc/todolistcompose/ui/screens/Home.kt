@file:OptIn(ExperimentalMaterial3Api::class)

package br.edu.satc.todolistcompose.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.edu.satc.todolistcompose.data.TaskData
import br.edu.satc.todolistcompose.ui.components.TaskCard
import br.edu.satc.todolistcompose.ui.theme.ToDoListComposeTheme
import br.edu.satc.todolistcompose.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val previewTasks = listOf(
        TaskData(id = 1, title = "Estudar Room", description = "Revisar DAO e Entity", complete = false),
        TaskData(id = 2, title = "Entregar exercicio", description = "Finalizar To-Do", complete = true)
    )

    ToDoListComposeTheme {
        HomeScreenContent(
            tasks = previewTasks,
            showOnlyPending = false,
            onTaskCheckedChange = { _, _ -> },
            onFilterChange = {},
            onSaveTask = { _, _ -> }
        )
    }
}

@Composable
fun HomeScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val showOnlyPending by viewModel.showOnlyPending.collectAsStateWithLifecycle()

    HomeScreenContent(
        tasks = tasks,
        showOnlyPending = showOnlyPending,
        onTaskCheckedChange = { task, isChecked ->
            viewModel.updateTaskComplete(task, isChecked)
        },
        onFilterChange = viewModel::updateShowOnlyPending,
        onSaveTask = viewModel::addTask
    )
}

@Composable
private fun HomeScreenContent(
    tasks: List<TaskData>,
    showOnlyPending: Boolean,
    onTaskCheckedChange: (TaskData, Boolean) -> Unit,
    onFilterChange: (Boolean) -> Unit,
    onSaveTask: (String, String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            FilterRow(
                showOnlyPending = showOnlyPending,
                onFilterChange = onFilterChange
            )
            Content(
                tasks = tasks,
                onTaskCheckedChange = onTaskCheckedChange
            )
        }

        NewTask(onSaveTask = onSaveTask)
    }
}

@Composable
private fun FilterRow(
    showOnlyPending: Boolean,
    onFilterChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Somente pendentes")
            Switch(
                checked = showOnlyPending,
                onCheckedChange = onFilterChange
            )
        }
    }
}

@Composable
private fun Content(
    tasks: List<TaskData>,
    onTaskCheckedChange: (TaskData, Boolean) -> Unit
) {
    if (tasks.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Nenhuma tarefa cadastrada")
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 96.dp)
    ) {
        items(items = tasks, key = { it.id }) { task ->
            TaskCard(
                taskData = task,
                onTaskCheckedChange = { isChecked ->
                    onTaskCheckedChange(task, isChecked)
                }
            )
        }
    }
}

/**
 * NewTask opens a bottom sheet to create a new task.
 */
@Composable
private fun NewTask(onSaveTask: (String, String) -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ExtendedFloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            text = { Text("Nova tarefa") },
            icon = { Icon(Icons.Filled.Add, contentDescription = null) },
            onClick = {
                showBottomSheet = true
            }
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text(text = "Titulo da tarefa") }
                )
                OutlinedTextField(
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    label = { Text(text = "Descricao da tarefa") }
                )
                Button(
                    modifier = Modifier.padding(top = 4.dp),
                    enabled = taskTitle.isNotBlank(),
                    onClick = {
                        onSaveTask(taskTitle, taskDescription)

                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }

                        taskTitle = ""
                        taskDescription = ""
                    }
                ) {
                    Text("Salvar")
                }
            }
        }
    }
}
