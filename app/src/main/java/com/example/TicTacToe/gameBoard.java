package com.example.TicTacToe;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class gameBoard {

    private Game game;
    private int[] state;
    private int currentPlayer;
    private ImageView[] cellViews; // Array to store references to cell ImageViews

    public gameBoard(ImageView[] cellViews) {
        this.cellViews = cellViews;
        state = new int[9];
        currentPlayer = 1; // Player 1 starts
    }

    public void initializeGame() {
        for (int i = 0; i < 9; i++) {
            state[i] = 0; // 0 represents an empty cell
        }
        currentPlayer = 1;
        updateUI();
    }

    public void updateState(int position) {
        if (state[position] == 0) {
            state[position] = currentPlayer;
            updateUI();
            if (checkForWinner()) {
                gameOver();
                return;
            }
            currentPlayer = (currentPlayer == 1) ? 2 : 1; // Switch player
        }
    }

    private boolean checkForWinner() {
        // Check rows
        for (int i = 0; i <= 6; i += 3) {
            if (state[i] != 0 && state[i] == state[i + 1] && state[i] == state[i + 2]) {
                return true;
            }
        }
        // Check columns
        for (int i = 0; i <= 2; i++) {
            if (state[i] != 0 && state[i] == state[i + 3] && state[i] == state[i + 6]) {
                return true;
            }
        }
        // Check diagonals
        if ((state[0] != 0 && state[0] == state[4] && state[0] == state[8]) ||
                (state[2] != 0 && state[2] == state[4] && state[2] == state[6])) {
            return true;
        }
        return false;
    }

    private void gameOver() {
        // Display winner or draw
        String message = "Player " + currentPlayer + " wins!";
        Toast.makeText(cellViews[0].getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void updateUI() {
        for (int i = 0; i < 9; i++) {
            if (state[i] == 1) {
                cellViews[i].setImageResource(R.drawable.ximage); // Replace with your X symbol drawable
            } else if (state[i] == 2) {
                cellViews[i].setImageResource(R.drawable.oimage); // Replace with your O symbol drawable
            } else {
                cellViews[i].setImageDrawable(null); // Clear the cell
            }
        }
    }
}