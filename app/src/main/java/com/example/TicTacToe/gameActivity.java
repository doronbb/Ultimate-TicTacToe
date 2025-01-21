package com.example.TicTacToe;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class gameActivity extends AppCompatActivity {

    fbController authHelper;
    Dialog dialog;
    gameBoard board; // Add the gameBoard member variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dialog = new Dialog(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize gameBoard after setting the content view
        ImageView[] imageViews = {
                findViewById(R.id.topLeft), findViewById(R.id.top), findViewById(R.id.topRight),
                findViewById(R.id.midLeft), findViewById(R.id.mid), findViewById(R.id.midRight),
                findViewById(R.id.bottomLeft), findViewById(R.id.bottom), findViewById(R.id.bottomRight)
        };
        board = new gameBoard(imageViews); // Initialize the board

        // Set click listeners for each cell
        for (int i = 0; i < 3; i++) {

                ImageView img = imageViews[i];
                final int bigBoardPosition = i * 3 + j; // Calculate the big board position

                img.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // ... existing code ...
                        dialog.findViewById(R.id.ConfirmChoice).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = -1;
                                switch (v.getId()) {
                                    // Set position based on the clicked ImageView's ID
                                    case R.id.topLeft: position = 0; break;
                                    // ... add cases for other ImageViews ...
                                    case R.id.bottomRight: position = 8; break;
                                }
                                if (position != -1) {
                                    board.updateState(position); // Update the game state
                                }
                                dialog.hide();
                            }
                        });
                    }
                });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemID = item.getItemId();
        if(itemID == R.id.Settings){
            openSettings();
        }
        if(itemID == R.id.LogOut){
            LogOut();
        }
        if(itemID == R.id.Main){
            gotoMain();
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoMain(){

        Intent i;
        i = new Intent(this, mainActivity.class);
        this.startActivity(i);
    }

    private void openSettings(){

        Intent i;
        i = new Intent(this, settingsActivity.class);
        this.startActivity(i);

    }

    private void LogOut(){
        Intent i;
        i = new Intent(this, loginActivity.class);
        this.startActivity(i);
    }

    // Helper method to determine the small board position based on the clicked view's ID
    private int getSmallBoardPosition(int viewId) {
        switch (viewId) {
            case R.id.topLeft: return 0;
            case R.id.top: return 1;
            case R.id.topRight: return 2;
            case R.id.midLeft: return 3;
            case R.id.mid: return 4;
            case R.id.midRight: return 5;
            case R.id.bottomLeft: return 6;
            case R.id.bottom: return 7;
            case R.id.bottomRight: return 8;
            default: return -1;
        }
    }
}