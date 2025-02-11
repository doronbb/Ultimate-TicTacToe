package com.example.TicTacToe;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView tvMessage;
    FloatingActionButton btnAdd;
    MenuItem Settings;
    private fbController auth; // Declare, but don't initialize here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call super.onCreate first
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize fbController *inside* onCreate
        auth = new fbController(this);
        auth.setNavigationListener(new fbController.NavigationListener() {
            @Override
            public void navigateToMain() {
                // We are already in main, so just show a Toast.
                Toast.makeText(MainActivity.this, "Already in Main Activity", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void navigateToLogin() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Prevent going back to main activity after logout
            }
        });
        ArrayList<Game> games = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            games.add(new Game("Game " + i, "Host: "));
        }
        RecyclerView recyclerView = findViewById(R.id.rvGames);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        GameAdapter gameAdapter = new GameAdapter(games);  // Make sure you have a GameAdapter class
        recyclerView.setAdapter(gameAdapter);

        tvMessage = findViewById(R.id.tvMessage);
        btnAdd = findViewById(R.id.btnHost);

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            if (email != null) {
                String username = email.substring(0, email.indexOf('@'));
                tvMessage.setText("Welcome back, " + username + "!");
            } else {
                tvMessage.setText("Welcome back!");
            }
        } else {
            tvMessage.setText("Welcome!");
        }

        btnAdd.setOnClickListener(v -> {
            Intent addIntent = new Intent(this, HostActivity.class);
            startActivity(addIntent);
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
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true; // Consume the event
        } else if (itemID == R.id.LogOut) {
            auth.logoutUser(); // Use the fbController's logout method
            return true; // Consume the event
        } else if (itemID == R.id.Main) {
            Toast.makeText(this, "Already in Main Activity", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item); // Important for other menu items
    }
}