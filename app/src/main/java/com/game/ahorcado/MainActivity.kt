package com.game.ahorcado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.game.ahorcado.ui.theme.AhorcadoTheme

class MainActivity : ComponentActivity() {
    private val words = listOf(
        "android",
        "jetpack",
        "componer",
        "kotlin",
        "desarrollo",
        "estudio",
        "aplicacion",
        "variable",
        "funcion",
        "interfaz",
        "arquitectura",
        "actividad",
        "fragmento",
        "programacion",
        "empanada",
        "telefono",
        "navegacion",
        "repositorio",
        "framework",
        "lenguaje"
    )
    private val alphabet = ('A'..'Z').toList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AhorcadoTheme {
                var word by remember { mutableStateOf(words.random().uppercase().toCharArray().toList()) }
                var guessedLetters by remember { mutableStateOf(mutableStateListOf<Char>()) }
                var attempts by remember { mutableStateOf(0) }
                var wordsGuessed by remember { mutableStateOf(0) }  // Estado para contar las palabras adivinadas

                AhorcadoScreen(
                    word = word,
                    alphabet = alphabet,
                    guessedLetters = guessedLetters,
                    attempts = attempts,
                    wordsGuessed = wordsGuessed,
                    onLetterClick = { letter ->
                        if (letter in word) {
                            if (letter !in guessedLetters) {
                                guessedLetters.add(letter)
                            }
                        } else {
                            attempts++
                        }
                    },
                    onResetGame = {
                        if (word.all { it in guessedLetters }) {
                            wordsGuessed++  // Incrementa el conteo si se adivinó la palabra
                        }
                        word = words.random().uppercase().toCharArray().toList()
                        guessedLetters = mutableStateListOf()
                        attempts = 0
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AhorcadoScreen(
    word: List<Char>,
    alphabet: List<Char>,
    guessedLetters: List<Char>,
    attempts: Int,
    wordsGuessed: Int,  // Agrega el parámetro para las palabras adivinadas
    onLetterClick: (Char) -> Unit,
    onResetGame: () -> Unit
) {
    val images = listOf(
        R.drawable.img1, R.drawable.img2, R.drawable.img3,
        R.drawable.img4
    )
    val imageResource = if (attempts < images.size) images[attempts] else images.last()

    val gameWon = word.all { it in guessedLetters }
    val gameLost = attempts > 2

    Column(
        modifier = Modifier.padding(16.dp, vertical = 50.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Espacio vacío para la alineación
            Spacer(modifier = Modifier.width(1.dp))
            // Conteo de palabras adivinadas
            Text(text = "Palabras adivinadas: $wordsGuessed")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = imageResource), contentDescription = null,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            items(word) { letter ->
                Text(
                    text = if (letter in guessedLetters) "$letter" else "___",
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (gameWon || gameLost) {
            val message = if (gameWon) "¡Ganaste!" else "¡Perdiste!"
            AlertDialog(
                onDismissRequest = {},
                title = { Text(message) },
                text = { Text(if (gameWon) "¡Felicidades! Adivinaste la palabra." else "Lo siento, has agotado tus intentos.") },
                confirmButton = {
                    Button(onClick = onResetGame) {
                        Text("Reiniciar")
                    }
                }
            )
        } else {
            FlowRow(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center
            ) {
                alphabet.chunked(7).forEach { rowLetters ->
                    rowLetters.forEach { letter ->
                        Button(
                            onClick = { onLetterClick(letter) },
                            enabled = letter !in guessedLetters,
                            shape = RoundedCornerShape(0)
                        ) {
                            Text(text = "$letter", fontSize = 30.sp, modifier = Modifier.width(30.dp))
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                    }
                }
            }
        }
    }
}
