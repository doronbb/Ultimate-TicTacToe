package com.example.TicTacToe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.TicTacToe.services.BackgroundMusicService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.SharedPreferences;


public class SettingsActivity extends AppCompatActivity {

    Button btnSave;
    AlertDialog.Builder builder;
    EditText etEmail;
    fbController auth = new fbController(SettingsActivity.this);
    Switch  musicSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent saveExit = new Intent(SettingsActivity.this, MainActivity.class);
        builder = new AlertDialog.Builder(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        // Make sure to replace R.id.root_layout with the actual ID of your root layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnSave = findViewById(R.id.btnSave);
        etEmail = findViewById(R.id.etEmailU);


        musicSwitch = findViewById(R.id.music_switch);
        SharedPreferences preferences = getSharedPreferences("TicTacToePrefs", MODE_PRIVATE);
        boolean isMusicOn = preferences.getBoolean("musicSwitchState", false);  // Default to false
        musicSwitch.setChecked(isMusicOn);

        //Check for Switch state
        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isMusicOn) {
                // Save the switch state to SharedPreferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("musicSwitchState", isMusicOn);
                editor.apply();

                if (isMusicOn) {
                    // Start the music service when the switch is ON
                    Intent musicIntent = new Intent(SettingsActivity.this, BackgroundMusicService.class);
                    startService(musicIntent);
                } else {
                    // Stop the music service when the switch is OFF
                    Intent musicIntent = new Intent(SettingsActivity.this, BackgroundMusicService.class);
                    stopService(musicIntent);
                }
            }
        });




        // Get the current user's UID
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseUser user = auth.getCurrentUser();
        etEmail.setText(user.getEmail());

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String email = dataSnapshot.child("email").getValue(String.class);
                    etEmail.setHint(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error, e.g., log the error or show a message
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Confirm Changes")
                        .setMessage("Are you sure?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SettingsActivity.this.startActivity(saveExit);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == R.id.Settings) {
            openSettings();
        }
        if (itemID == R.id.LogOut) {
            LogOut();
        }
        if (itemID == R.id.Main) {
            gotoMain();
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoMain() {
        Intent i = new Intent(this, MainActivity.class);
        this.startActivity(i);
    }

    private void openSettings() {
        Intent i = new Intent(this, SettingsActivity.class);
        this.startActivity(i);
    }

    private void LogOut() {
        Intent i = new Intent(this, LoginActivity.class);
        this.startActivity(i);
    }
}