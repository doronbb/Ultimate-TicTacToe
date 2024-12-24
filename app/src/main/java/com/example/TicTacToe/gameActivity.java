package com.example.TicTacToe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.TicTacToe.databinding.GameDialogBinding;

public class gameActivity extends AppCompatActivity {

    fbController authHelper;
    ImageView img;
    Dialog dialog;


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

        img = findViewById(R.id.topLeft);

        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.setContentView(R.layout.game_dialog);
                dialog.show();

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