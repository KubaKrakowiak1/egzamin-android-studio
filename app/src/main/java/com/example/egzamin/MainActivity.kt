package com.example.egzamin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Egzamin()
        }
    }
}

@Composable
fun Egzamin() {

    data class Question(
        val text: String,
        val answers: List<String>,
        val correctIndex: Int
    )

    val questions = remember {
        listOf(
            Question(
                "Który język jest używany w Android Studio?",
                listOf("Kotlin", "Python", "PHP", "C"),
                0
            ),
            Question(
                "Co służy do budowy UI w Compose?",
                listOf("XML", "Composable", "Fragment", "Intent"),
                1
            ),
            Question(
                "Która funkcja uruchamia ekran?",
                listOf("onStart()", "setContent{}", "runApp()", "build()"),
                1
            )
        ).shuffled()
    }

    var currentIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf(-1) }
    var isFinished by remember { mutableStateOf(false) }
    var time by remember { mutableStateOf(0) }

    // TIMER
    LaunchedEffect(Unit) {
        while (!isFinished) {
            delay(1000)
            time++
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {

        if (isFinished) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Egzamin zakończony!", fontSize = 22.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Wynik: $score / ${questions.size}")
                Text("Czas: $time sekund")
            }
        } else {

            val question = questions[currentIndex]

            Column(modifier = Modifier.padding(16.dp)) {

                Text("Czas: $time s", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Text(question.text, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(16.dp))

                question.answers.forEachIndexed { index, answer ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedAnswer == index,
                            onClick = { selectedAnswer = index }
                        )
                        Text(answer)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {

                    if (selectedAnswer == question.correctIndex) {
                        score++
                    }

                    selectedAnswer = -1

                    if (currentIndex < questions.size - 1) {
                        currentIndex++
                    } else {
                        isFinished = true
                    }

                }) {
                    Text("Dalej")
                }
            }
        }
    }
}