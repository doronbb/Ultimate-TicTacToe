package com.example.TicTacToe;

import java.util.ArrayList;

public interface IFirebaseCallback {
    void onCallbackUser(User user);
    void onCallbackListUsers(ArrayList<User> users);

}
