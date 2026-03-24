package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

// Layout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

// Material3 (IMPORTANTE: só usar esses)
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

// Compose core
import androidx.compose.runtime.Composable

// UI
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen {
                navController.navigate("pista1")
            }
        }

        composable("pista1") {
            PistaScreen(
                texto = "Tenho chaves mas não abro portas. O que sou?",
                onNext = { navController.navigate("pista2") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("pista2") {
            PistaScreen(
                texto = "Quanto é 2 + 2?",
                onNext = { navController.navigate("pista3") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("pista3") {
            PistaScreen(
                texto = "Sou redondo e ilumino a noite. Quem sou?",
                onNext = { navController.navigate("tesouro") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("tesouro") {
            TreasureScreen {
                // 🔥 LIMPA A PILHA
                navController.navigate("home") {
                    popUpTo("home") {
                        inclusive = true
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(onStart: () -> Unit) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // ✅ resolve warning
                .padding(16.dp)
        ) {
            Text("🏴‍☠️ Caça ao Tesouro")

            Button(onClick = onStart) {
                Text("Iniciar caça ao tesouro")
            }
        }
    }
}

@Composable
fun PistaScreen(
    texto: String,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(texto)

            Button(onClick = onNext) {
                Text("Próxima pista")
            }

            Button(onClick = onBack) {
                Text("Voltar")
            }
        }
    }
}

@Composable
fun TreasureScreen(onRestart: () -> Unit) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("🎉 Parabéns! Você encontrou o tesouro!")

            Button(onClick = onRestart) {
                Text("Voltar para Home")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    AppNavigation()
}