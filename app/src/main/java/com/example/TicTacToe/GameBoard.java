package com.example.TicTacToe;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;

public class GameBoard {
    private int[][][][] state;
    private int[][] largerBoardState;
    private int currentPlayer;
    private ImageView[][][][] cellViews;
    private int activeBoardRow = -1;
    private int activeBoardCol = -1;
    private GameActivityListener listener;
    private Context context;

    public interface GameActivityListener {
        void onGameOver(int player);
    }

    public GameBoard(ImageView[][][][] cellViews, GameActivityListener listener, Context context) {
        this.cellViews = cellViews;
        this.listener = listener;
        this.context = context;
        initializeGame();
    }

    public void initializeGame() {
        state = new int[3][3][3][3];
        largerBoardState = new int[3][3];
        currentPlayer = 1;
        activeBoardRow = -1;
        activeBoardCol = -1;
        clearAllWinAnimations();
        updateUI();
    }

    public void updateState(int boardRow, int boardCol, int cellRow, int cellCol) {
        if (isMoveValid(boardRow, boardCol, cellRow, cellCol)) {
            state[boardRow][boardCol][cellRow][cellCol] = currentPlayer;
            updateUI();

            if (checkForSmallBoardWinner(boardRow, boardCol)) {
                largerBoardState[boardRow][boardCol] = currentPlayer;
                showWinAnimation(boardRow, boardCol);
            }

            if (checkForLargerBoardWinner()) {
                listener.onGameOver(currentPlayer);
                return;
            }

            currentPlayer = (currentPlayer == 1) ? 2 : 1;
            updateActiveBoard(cellRow, cellCol);
        }
    }

    private void updateActiveBoard(int cellRow, int cellCol) {
        int nextBoardRow = cellRow;
        int nextBoardCol = cellCol;

        if (largerBoardState[nextBoardRow][nextBoardCol] != 0 || isSmallBoardFull(nextBoardRow, nextBoardCol)) {
            activeBoardRow = -1;
            activeBoardCol = -1;
        } else {
            activeBoardRow = nextBoardRow;
            activeBoardCol = nextBoardCol;
        }
    }

    private boolean isMoveValid(int boardRow, int boardCol, int cellRow, int cellCol) {
        return state[boardRow][boardCol][cellRow][cellCol] == 0 &&
                (activeBoardRow == -1 || (activeBoardRow == boardRow && activeBoardCol == boardCol)) &&
                largerBoardState[boardRow][boardCol] == 0;
    }

    private boolean isSmallBoardFull(int boardRow, int boardCol) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[boardRow][boardCol][i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkForSmallBoardWinner(int boardRow, int boardCol) {
        int[][] smallBoard = state[boardRow][boardCol];

        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (checkLine(smallBoard[i][0], smallBoard[i][1], smallBoard[i][2])) return true;
            if (checkLine(smallBoard[0][i], smallBoard[1][i], smallBoard[2][i])) return true;
        }

        // Check diagonals
        return checkLine(smallBoard[0][0], smallBoard[1][1], smallBoard[2][2]) ||
                checkLine(smallBoard[0][2], smallBoard[1][1], smallBoard[2][0]);
    }

    private boolean checkForLargerBoardWinner() {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (checkLine(largerBoardState[i][0], largerBoardState[i][1], largerBoardState[i][2])) return true;
            if (checkLine(largerBoardState[0][i], largerBoardState[1][i], largerBoardState[2][i])) return true;
        }

        // Check diagonals
        return checkLine(largerBoardState[0][0], largerBoardState[1][1], largerBoardState[2][2]) ||
                checkLine(largerBoardState[0][2], largerBoardState[1][1], largerBoardState[2][0]);
    }

    private boolean checkLine(int a, int b, int c) {
        return a != 0 && a == b && a == c;
    }

    private void showWinAnimation(int boardRow, int boardCol) {
        ViewGroup smallBoardRoot = (ViewGroup) cellViews[boardRow][boardCol][0][0].getParent();
        WinAnimationView animationView = new WinAnimationView(context);

        animationView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        int drawableId = currentPlayer == 1 ? R.drawable.win_x_animation : R.drawable.win_o_animation;
        animationView.setWinAnimation(ContextCompat.getDrawable(context, drawableId));
        smallBoardRoot.addView(animationView);
    }

    private void clearAllWinAnimations() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ViewGroup smallBoardRoot = (ViewGroup) cellViews[i][j][0][0].getParent();
                for (int k = 0; k < smallBoardRoot.getChildCount(); k++) {
                    View child = smallBoardRoot.getChildAt(k);
                    if (child instanceof WinAnimationView) {
                        smallBoardRoot.removeView(child);
                        k--;
                    }
                }
            }
        }
    }

    private void updateUI() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        ImageView cell = cellViews[i][j][k][l];
                        int player = state[i][j][k][l];

                        if (player == 1) {
                            cell.setImageResource(R.drawable.ximage);
                        } else if (player == 2) {
                            cell.setImageResource(R.drawable.oimage);
                        } else {
                            cell.setImageDrawable(null);
                        }
                    }
                }
            }
        }
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }
}