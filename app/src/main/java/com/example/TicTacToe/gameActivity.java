package com.example.TicTacToe;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class gameActivity extends AppCompatActivity {

    fbController authHelper;  // If you're using Firebase
    gameBoard board;
    private ImageView[] imageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);  // Call setContentView FIRST

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        imageViews = new ImageView[] { // Initialize AFTER setContentView
                findViewById(R.id.cell_00), findViewById(R.id.cell_01), findViewById(R.id.cell_02),
                findViewById(R.id.cell_10), findViewById(R.id.cell_11), findViewById(R.id.cell_12),
                findViewById(R.id.cell_20), findViewById(R.id.cell_21), findViewById(R.id.cell_22)
        };

        board = new gameBoard(imageViews); // Initialize board AFTER imageViews

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ImageView img = imageViews[i * 3 + j];
                final int position = i * 3 + j;

                img.setOnClickListener(v -> {
                    board.updateState(position);
                });
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
            openSettings();
        } else if (itemId == R.id.LogOut) {
            LogOut();
        } else if (itemId == R.id.Main) {
            gotoMain();
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoMain() {
        Intent i = new Intent(this, mainActivity.class);
        startActivity(i);
    }

    private void openSettings() {
        Intent i = new Intent(this, settingsActivity.class);
        startActivity(i);
    }

    private void LogOut() {
        Intent i = new Intent(this, loginActivity.class);
        startActivity(i);
    }
}