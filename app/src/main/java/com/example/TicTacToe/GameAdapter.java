package com.example.TicTacToe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private ArrayList<Game> Games ;

    public GameAdapter(ArrayList<Game> games) {
        this.Games = games;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View gameView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvitem_gamelist,parent,false);
        return new GameViewHolder(gameView);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game currentGame = Games.get(position);
        holder.tvGameName.setText(currentGame.getGameName());
        holder.tvHostName.setText(currentGame.getHostName());

    }

    @Override
    public int getItemCount() {
        return Games.size();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder{

        public TextView tvGameName;
        public TextView tvHostName;
        public ImageView ivIcon;
        public Button btnJoinGame;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGameName = itemView.findViewById(R.id.tvGameName);
            tvHostName = itemView.findViewById(R.id.tvHostName);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            btnJoinGame = itemView.findViewById(R.id.btnJoinGame);
        }
    }
}
