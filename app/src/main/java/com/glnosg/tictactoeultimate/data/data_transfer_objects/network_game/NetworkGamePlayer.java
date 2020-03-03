package com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game;


public class NetworkGamePlayer implements Comparable<NetworkGamePlayer> {

    public static final String NAME = "name", USER_ID = "userId", PLAYER_ID = "playerId";

    private String name, userId;
    private int gamesPlayed, gamesWon, gamesLeft;
    private int playerId;
    private int state;

    public NetworkGamePlayer() {}

    public NetworkGamePlayer(
            String name,
            String userId,
            int gamesPlayed,
            int gamesWon,
            int gamesLeft) {
        this.name = name;
        this.userId = userId;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.gamesLeft = gamesLeft;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLeft() {
        return gamesLeft;
    }

    public int getState() {
        return state;
    }

    @Override
    public int compareTo(NetworkGamePlayer otherPlayer) {
        return (this.getPlayerId() - otherPlayer.getPlayerId());
    }
}
