package com.glnosg.tictactoeultimate.main_menu.play_online_settings;

import android.util.Log;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OpenGamesMonitor {

    private final String LOG_TAG = OpenGamesMonitor.class.getSimpleName();

    private DatabaseReference mGamesDbReference;
    private GamesDbEventsObserver mGamesDbEventsObserver;
    private ChildEventListener mGamesDbListener;

    public OpenGamesMonitor() {
        initDbReferences();
    }

    private void initDbReferences() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        mGamesDbReference = db.getReference().child(NetworkGame.GAMES);
    }

    public void attachGamesDbEventsObserver(GamesDbEventsObserver observer) {
        mGamesDbEventsObserver = observer;
    }

    public void fetchInitialData() {
        OpenGamesInitialDataFetcher.fetchData(mGamesDbReference, mGamesDbEventsObserver);
    }

    public void attachListeners() {
        if (mGamesDbListener == null) {
            mGamesDbListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    NetworkGame game = dataSnapshot.getValue(NetworkGame.class);
                    if (game != null) {
                        if (isGameValid(game) && game.getState() == NetworkGame.STATE_OPEN) {
                            DatabaseReference creatorRef = dataSnapshot.getRef()
                                    .child(NetworkGame.PLAYERS)
                                    .child(game.getCreatorId());
                            completeOpenGameData(game, creatorRef);
                        }
                    }
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    NetworkGame game = dataSnapshot.getValue(NetworkGame.class);
                    if (game != null) {
                        if (isGameValid(game) && game.getState() == NetworkGame.STATE_OPEN) {
                            DatabaseReference creatorRef = dataSnapshot.getRef()
                                    .child(NetworkGame.PLAYERS)
                                    .child(game.getCreatorId());
                            completeOpenGameData(game, creatorRef);
                        } else if (isGameValid(game) && game.getState() == NetworkGame.STATE_CLOSED) {
                            mGamesDbEventsObserver.onGameClosed(game.getGameId());
                        }
                    }
                }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    NetworkGame game = dataSnapshot.getValue(NetworkGame.class);
                    if (game != null && game.getGameId() != null) {
                        mGamesDbEventsObserver.onGameClosed(game.getGameId());
                    }
                }
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            };
        }
        mGamesDbReference.addChildEventListener(mGamesDbListener);
    }

    private boolean isGameValid(NetworkGame game) {
        return game.getGameId() != null
                && game.getCreatorId() != null
                && game.getState() != 0
                && game.getBoardType() != 0;
    }

    private void completeOpenGameData(NetworkGame game, DatabaseReference creatorRef) {
        OpenNetworkGame openGame =
                new OpenNetworkGame(game.getBoardType(), game.getGameId(), game.getCreatorId());
        creatorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child(NetworkGamePlayer.NAME).getValue(String.class);
                Integer gamesPlayed = dataSnapshot.child(NetworkGame.GAMES_PLAYED).getValue(Integer.class);
                Integer gamesWon = dataSnapshot.child(NetworkGame.GAMES_WON).getValue(Integer.class);
                Integer gamesLeft = dataSnapshot.child(NetworkGame.GAMES_LEFT).getValue(Integer.class);
                if (name != null && gamesPlayed != null && gamesWon != null && gamesLeft != null) {
                    openGame.setCreatorName(name);
                    openGame.setCreatorGamesPlayed(gamesPlayed);
                    openGame.setCreatorGamesWon(gamesWon);
                    openGame.setCreatorGamesLeft(gamesLeft);
                    mGamesDbEventsObserver.onGameOpened(openGame);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void detachListeners() {
        if (mGamesDbListener != null) {
            mGamesDbReference.removeEventListener(mGamesDbListener);
            mGamesDbListener = null;
        }
    }
}
