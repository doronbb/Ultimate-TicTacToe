package com.example.TicTacToe;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends AppCompatActivity implements GameBoard.GameActivityListener {

    private static final String TAG = "GameActivity";
    private GameBoard board;
    private ImageView[][][][] cellViews;
    private fbController fbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdge();
        initializeViews();
        setupCellListeners();
    }

    private void setupEdgeToEdge() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        try {
            fbController = new fbController(this);
        } catch (Exception e) {
            Log.e(TAG, "Firebase initialization failed", e);
            Toast.makeText(this, "Firebase setup error", Toast.LENGTH_SHORT).show();
        }

        cellViews = new ImageView[3][3][3][3];
        boolean allViewsFound = true;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                GridLayout boardGrid = findViewById(getResources().getIdentifier("board_" + i + j, "id", getPackageName()));

                if (boardGrid == null) {
                    Log.e(TAG, "Board GridLayout not found for: board_" + i + j);
                    allViewsFound = false;
                    continue;
                }

                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        String cellId = String.format("cell_%d%d", k, l);
                        int resId = getResources().getIdentifier(cellId, "id", getPackageName());

                        if (resId == 0) {
                            Log.e(TAG, "Resource ID not found for: " + cellId + " in board_" + i + j);
                            allViewsFound = false;
                            continue;
                        }

                        View view = boardGrid.findViewById(resId);

                        if (view == null) {
                            Log.e(TAG, "View not found: " + cellId + " in board_" + i + j);
                            allViewsFound = false;
                        } else if (!(view instanceof ImageView)) {
                            Log.e(TAG, "Expected ImageView but found " + view.getClass().getSimpleName() + " for ID " + cellId);
                            allViewsFound = false;
                        } else {
                            cellViews[i][j][k][l] = (ImageView) view;
                        }
                    }
                }
            }
        }

        if (!allViewsFound) {
            Toast.makeText(this, "Game board initialization failed", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Pass the Context as the third argument
        board = new GameBoard(cellViews, this, getApplicationContext());
        board.initializeGame();
    }

    private void setupCellListeners() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        final int boardRow = i;
                        final int boardCol = j;
                        final int cellRow = k;
                        final int cellCol = l;

                        if (cellViews[i][j][k][l] != null) {
                            Log.d(TAG, "ClickListener added to cell: " + i + j + " at " + k + l);
                            cellViews[i][j][k][l].setOnClickListener(v -> board.updateState(boardRow, boardCol, cellRow, cellCol));
                        } else {
                            Log.e(TAG, "ClickListener NOT added to cell: " + i + j + " at " + k + l + " because it's null!");
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.Settings) {
            navigateTo(SettingsActivity.class);
        } else if (itemId == R.id.LogOut) {
            navigateTo(LoginActivity.class);
        } else if (itemId == R.id.Main) {
            navigateTo(MainActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigateTo(Class<?> activityClass) {
        try {
            Intent intent = new Intent(this, activityClass);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Navigation error", e);
            Toast.makeText(this, "Navigation failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGameOver(int winner) {
        runOnUiThread(() -> showGameOverDialog(winner));
    }

    private void showGameOverDialog(int winner) {
        String message = (winner != 0)
                ? "Player " + winner + " wins!"
                : "It's a draw!";

        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage(message)
                .setPositiveButton("Play Again", (dialog, id) -> board.initializeGame())
                .setNegativeButton("Leave", (dialog, id) -> finish())
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fbController != null) {
            fbController.cleanup();
        }
    }
}