package com.example.TicTacToe;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class fbController {
    private static final String TAG = "FirebaseController";
    private static final String USERS_PATH = "Users"; // Constant for database path

    private FirebaseAuth mAuth; // No longer static
    private final Context context;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private ValueEventListener userDataListener; // Store the listener

    public fbController(Context context) {
        this.context = context.getApplicationContext();
        initializeFirebase();
    }

    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return mAuth != null ? mAuth.getCurrentUser() : null;
    }

    public DatabaseReference getDatabaseReference(String path) {
        return db.getReference(path);
    }

    public void createUser(String email, String password, User user) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        if (firebaseUser != null) {
                            // Combine user creation and database write
                            user.setUid(firebaseUser.getUid()); // Set UID *before* writing

                            getDatabaseReference(USERS_PATH).child(firebaseUser.getUid()).setValue(user)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            navigateToMainActivity();
                                            showToast("Registration Successful");
                                        } else {
                                            // More specific error handling
                                            handleDatabaseError(dbTask.getException());
                                        }
                                    });
                        }
                    } else {
                        // More specific error handling
                        handleAuthError(task.getException());
                    }
                });
    }

    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        navigateToMainActivity();
                        showToast("Login Successful");
                    } else {
                        // More specific error handling
                        handleAuthError(task.getException());
                    }
                });
    }
    // Interface for navigation
    public interface NavigationListener {
        void navigateToMain();
        void navigateToLogin();
    }

    private NavigationListener navigationListener;

    public void setNavigationListener(NavigationListener listener) {
        this.navigationListener = listener;
    }
    public void logoutUser() {
        if (mAuth != null) {
            mAuth.signOut();
            if (navigationListener != null){
                navigationListener.navigateToLogin();
            }
        }
    }

    public boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    void navigateToMainActivity() {
        if (navigationListener != null){
            navigationListener.navigateToMain();
        }
    }

    private void navigateToLoginActivity() { // Added for consistency
        if(navigationListener != null){
            navigationListener.navigateToLogin();
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public void fetchUserData(String userId, UserDataCallback callback) {
        DatabaseReference userRef = getDatabaseReference(USERS_PATH + "/" + userId);
        // Store the listener so we can remove it later
        userDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                callback.onUserDataReceived(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error fetching user data", error.toException());
                callback.onError(error.toException());
            }
        };
        userRef.addValueEventListener(userDataListener);
    }

    public void removeUserDataListener(String userId) {
        if (userDataListener != null) {
            getDatabaseReference(USERS_PATH + "/" + userId).removeEventListener(userDataListener);
            userDataListener = null; // Clear the reference
        }
    }

    public interface UserDataCallback {
        void onUserDataReceived(User user);
        void onError(Exception e);
    }

    // Helper method for handling authentication errors
    private void handleAuthError(Exception exception) {
        if (exception != null) {
            Log.e(TAG, "Authentication error", exception);
            showToast("Authentication failed: " + exception.getMessage());
            // You could add more specific error handling here based on the exception type
        } else {
            showToast("Authentication failed: Unknown error");
        }
    }

    // Helper method for handling database errors
    private void handleDatabaseError(Exception exception) {
        if (exception != null) {
            Log.e(TAG, "Database error", exception);
            showToast("Database operation failed: " + exception.getMessage());
        } else {
            showToast("Database operation failed: Unknown error");
        }
    }
    // Method to be called when the component using fbController is destroyed
    public void cleanup() {
        // Remove any remaining listeners to prevent leaks
        if (userDataListener != null) {
            //remove the listener on the appropriate ref, this will differ.
            //userDataListener = null; //removed, as reference needs clearing
        }
    }
}