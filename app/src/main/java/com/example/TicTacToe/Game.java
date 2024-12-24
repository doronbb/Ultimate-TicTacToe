package com.example.TicTacToe;

public class Game {

    private String gameName;
    private String hostName;
    private String gameId; // Add this for a unique game ID

    public Game() {
        // Default constructor required for Firebase
    }

    public Game(String gameName, String hostName) {
        this.gameName = gameName;
        this.hostName = hostName;
        this.gameId = generateGameId(); // Generate a unique ID when creating a new game
    }

    // Getters and setters
    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    // Method to generate a unique game ID
    private String generateGameId() {
        // You can implement your own logic here to generate unique IDs
        // For example, you can use UUIDs or Firebase's push() method
        return "game-" + System.currentTimeMillis();
    }
}