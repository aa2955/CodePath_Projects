package com.example.project1_wordle

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project1_wordle.FourLetterWordList.getRandomFourLetterWord

class MainActivity : AppCompatActivity() {

    private var wordToGuess: String = ""
    private var guessCount = 0
    private val maxGuesses = 3 // Only allow 3 guesses
    private var streak = 0
    private lateinit var guessGrid: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val guessInput = findViewById<EditText>(R.id.guessInput)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val resetButton = findViewById<Button>(R.id.resetButton)
        val streakText = findViewById<TextView>(R.id.streakText)
        guessGrid = findViewById(R.id.guessGrid)

        // Initialize the game
        resetGame()

        submitButton.setOnClickListener {
            val guess = guessInput.text.toString().uppercase()

            if (guess.length != 4 || !guess.matches(Regex("[A-Z]+"))) {
                Toast.makeText(this, "Invalid input! Please enter a 4-letter word.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            guessCount++

            // Display the guess in the grid with color feedback
            displayGuessInGrid(guess)

            if (guess == wordToGuess) {
                streak++
                streakText.text = "Streak: $streak"
                Toast.makeText(this, "Congratulations! You guessed the word!", Toast.LENGTH_SHORT).show()
                endGame()
            } else if (guessCount >= maxGuesses) {
                Toast.makeText(this, "Out of guesses! The word was $wordToGuess.", Toast.LENGTH_SHORT).show()
                endGame()
            }
        }

        resetButton.setOnClickListener {
            resetGame()
        }
    }

    private fun checkGuess(guess: String): String {
        var result = ""
        for (i in guess.indices) {
            if (guess[i] == wordToGuess[i]) {
                result += "O" // Correct letter in the correct position
            } else if (guess[i] in wordToGuess) {
                result += "+" // Correct letter in the wrong position
            } else {
                result += "X" // Incorrect letter
            }
        }
        return result
    }

    private fun resetGame() {
        wordToGuess = getRandomFourLetterWord().uppercase()
        guessCount = 0
        guessGrid.removeAllViews() // Clear the grid for a new game

        findViewById<EditText>(R.id.guessInput).text.clear()
        findViewById<Button>(R.id.submitButton).isEnabled = true
        findViewById<Button>(R.id.submitButton).visibility = View.VISIBLE
        findViewById<Button>(R.id.resetButton).visibility = View.GONE
    }

    private fun endGame() {
        findViewById<Button>(R.id.submitButton).isEnabled = false
        findViewById<Button>(R.id.submitButton).visibility = View.GONE
        findViewById<Button>(R.id.resetButton).visibility = View.VISIBLE
    }

    private fun displayGuessInGrid(guess: String) {
        val feedback = checkGuess(guess)

        for (i in guess.indices) {
            val letterView = TextView(this)
            letterView.text = guess[i].toString()
            letterView.textSize = 24f
            letterView.setPadding(16, 16, 16, 16)
            letterView.setBackgroundColor(getColorForFeedback(feedback[i]))

            // Set layout params for GridLayout
            val gridParams = GridLayout.LayoutParams()
            gridParams.columnSpec = GridLayout.spec(i)
            gridParams.rowSpec = GridLayout.spec(guessCount - 1) // Display in the correct row based on guessCount
            letterView.layoutParams = gridParams

            guessGrid.addView(letterView)
        }
    }

    private fun getColorForFeedback(feedbackChar: Char): Int {
        return when (feedbackChar) {
            'O' -> Color.GREEN // Correct letter in the correct place
            '+' -> Color.YELLOW // Correct letter, wrong place
            else -> Color.RED // Incorrect letter
        }
    }
}
