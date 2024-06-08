package com.game.ahorcado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.game.ahorcado.ui.theme.AhorcadoTheme

class MainActivity : ComponentActivity() {
    private val words = listOf(
        "android",
        "jetpack",
        "compose"
    )
    private val alphabet = ('A'..'Z').toList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AhorcadoTheme {
                var word by remember { mutableStateOf(words.random().toUpperCase().toCharArray().toList()) }
                var guessedLetters by remember { mutableStateOf(mutableStateListOf<Char>()) }
                var attempts by remember { mutableStateOf(0) }

                AhorcadoScreen(
                    word = word,
                    alphabet = alphabet,
                    guessedLetters = guessedLetters,
                    attempts = attempts,
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
                        word = words.random().toUpperCase().toCharArray().toList()
                        guessedLetters = mutableStateListOf()
                        attempts = 0
                    }
                )
            }
        }
    }
}

@Composable
fun AhorcadoScreen(
    word: List<Char>,
    alphabet: List<Char>,
    guessedLetters: List<Char>,
    attempts: Int,
    onLetterClick: (Char) -> Unit,
    onResetGame: () -> Unit
) {
    val maxAttempts = 4
    val images = listOf(
        R.drawable.img1, R.drawable.img2, R.drawable.img3,
        R.drawable.img4
    )

    val gameWon = word.all { it in guessedLetters }
    val gameLost = attempts >= maxAttempts

    Column(modifier = Modifier.padding(16.dp)) {
        Image(
            painter = painterResource(id = images[attempts]), contentDescription = null,
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(alphabet.chunked(7)) { rowLetters ->
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        rowLetters.forEach { letter ->
                            Button(onClick = { onLetterClick(letter) }, enabled = letter !in guessedLetters) {
                                Text(text = "$letter")
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
