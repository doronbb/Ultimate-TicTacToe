package com.example.TicTacToe;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class hostActivity extends AppCompatActivity {

    FloatingActionButton fabCreateGame;

    fbController authHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent createGame = new Intent(hostActivity.this, gameActivity.class);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_host);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fabCreateGame = findViewById(R.id.btnHostG);

        fabCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hostActivity.this.startActivity(createGame);
            }
        });

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
}