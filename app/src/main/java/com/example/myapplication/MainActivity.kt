package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var board: Array<Array<String>>
    private lateinit var buttons: Array<Array<Button>>
    private lateinit var statusTextView: TextView
    private lateinit var resetButton: Button
    private var currentPlayer = "X"
    private var isGameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize board and UI components
        board = Array(3) { Array(3) { "" } }
        statusTextView = findViewById(R.id.main_status_textview)
        resetButton = findViewById(R.id.main_reset_button)

        buttons = Array(3) { row ->
            Array(3) { col ->
                val buttonId = resources.getIdentifier("main_mark_btn_${row}${col}", "id", packageName)
                findViewById<Button>(buttonId).apply {
                    setOnClickListener { onCellClick(this, row, col) }
                }
            }
        }

        resetButton.setOnClickListener { resetGame() }
    }

    private fun onCellClick(button: Button, row: Int, col: Int) {
        if (isGameOver || board[row][col].isNotEmpty()) return

        // Update board and button
        board[row][col] = currentPlayer
        button.text = currentPlayer

        // Check for winner or draw
        if (checkWinner(row, col)) {
            isGameOver = true
            statusTextView.text = "Player $currentPlayer Wins!"
            Toast.makeText(this, "Player $currentPlayer Wins!", Toast.LENGTH_SHORT).show()
        } else if (isBoardFull()) {
            isGameOver = true
            statusTextView.text = "It's a Draw!"
        } else {
            // Switch player
            currentPlayer = if (currentPlayer == "X") "O" else "X"
            statusTextView.text = "Player $currentPlayer's Turn"
        }
    }

    private fun checkWinner(row: Int, col: Int): Boolean {
        // Check row
        if (board[row].all { it == currentPlayer }) return true

        // Check column
        if (board.all { it[col] == currentPlayer }) return true

        // Check diagonals
        if (row == col && board.indices.all { board[it][it] == currentPlayer }) return true
        if (row + col == 2 && board.indices.all { board[it][2 - it] == currentPlayer }) return true

        return false
    }

    private fun isBoardFull(): Boolean {
        return board.all { row -> row.all { it.isNotEmpty() } }
    }

    private fun resetGame() {
        // Reset board and game state
        board = Array(3) { Array(3) { "" } }
        currentPlayer = "X"
        isGameOver = false
        statusTextView.text = "Player X's Turn"

        // Clear all buttons
        for (row in buttons) {
            for (button in row) {
                button.text = ""
            }
        }
    }
}