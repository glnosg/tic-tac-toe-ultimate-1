package com.glnosg.tictactoeultimate.main_menu.play_online_settings;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;

public class OpenGamesInitialDataFetcher {

    private static final String LOG_TAG = OpenGamesInitialDataFetcher.class.getSimpleName();

    public static void fetchData(DatabaseReference gamesDbReference, GamesDbEventsObserver observer) {
        List<OpenNetworkGame> openGames = new LinkedList<>();
        gamesDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NetworkGame game = snapshot.getValue(NetworkGame.class);
                    if (game != null) {
                        if (isGameValid(game) && game.getState() == NetworkGame.STATE_OPEN) {
                            DatabaseReference creatorRef = dataSnapshot.getRef()
                                    .child(NetworkGame.PLAYERS)
                                    .child(game.getCreatorId());
                            CreatorData creatorData = getCreatorData(creatorRef);
                            if (creatorData.isValid()) {
                                OpenNetworkGame openGame = getOpenGame(game, creatorData);
                                openGames.add(openGame);
                            }
                        }
                    }
                }
                observer.setInitialData(openGames);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private static boolean isGameValid(NetworkGame game) {
        return game.getGameId() != null
                && game.getCreatorId() != null
                && game.getState() != 0
                && game.getBoardType() != 0;
    }

    private static CreatorData getCreatorData(DatabaseReference creatorRef) {
        CreatorData creatorData = new CreatorData();
        creatorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child(NetworkGamePlayer.NAME)
                        .getValue(String.class);
                Integer gamesPlayed = dataSnapshot.child(NetworkGame.GAMES_PLAYED)
                        .getValue(Integer.class);
                Integer gamesWon = dataSnapshot.child(NetworkGame.GAMES_WON)
                        .getValue(Integer.class);
                Integer gamesLeft = dataSnapshot.child(NetworkGame.GAMES_LEFT)
                        .getValue(Integer.class);
                if (name != null && gamesPlayed != null && gamesWon != null && gamesLeft != null) {
                    creatorData.setName(name);
                    creatorData.setGamesPlayed(gamesPlayed);
                    creatorData.setGamesWon(gamesWon);
                    creatorData.setGamesLeft(gamesLeft);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        return creatorData;
    }

    private static OpenNetworkGame getOpenGame(NetworkGame game, CreatorData creatorData) {
        OpenNetworkGame openGame =
                new OpenNetworkGame(game.getBoardType(), game.getGameId(), game.getCreatorId());
        openGame.setCreatorName(creatorData.getName());
        openGame.setCreatorGamesPlayed(creatorData.getGamesPlayed());
        openGame.setCreatorGamesWon(creatorData.getGamesWon());
        openGame.setCreatorGamesLeft(creatorData.getGamesLeft());
        return openGame;
    }

    private static class CreatorData {

        private final String INVALID_NAME = "invalid";
        private final int INVALID_VALUE = -1;

        private String name;
        private int gamesPlayed, gamesWon, gamesLeft;

        public CreatorData () {
            name = INVALID_NAME;
            gamesPlayed = INVALID_VALUE;
            gamesWon = INVALID_VALUE;
            gamesLeft = INVALID_VALUE;
        }

        public boolean isValid() {
            return name.equals(INVALID_NAME)
                    && gamesPlayed != INVALID_VALUE
                    && gamesWon != INVALID_VALUE
                    && gamesLeft != INVALID_VALUE;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setGamesPlayed(int gamesPlayed) {
            this.gamesPlayed = gamesPlayed;
        }

        public void setGamesWon(int gamesWon) {
            this.gamesWon = gamesWon;
        }

        public void setGamesLeft(int gamesLeft) {
            this.gamesLeft = gamesLeft;
        }

        public String getName() {
            return name;
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
    }
}
