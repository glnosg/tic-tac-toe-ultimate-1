package com.glnosg.tictactoeultimate.game.network_game;

import android.util.Log;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.GameInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PlayersMonitor {

    private final String LOG_TAG = PlayersMonitor.class.getSimpleName();

    private GameEventsObserver mGameEventsObserver;
    private DatabaseReference mGameDbReference;

    private int mNumberOfPlayers;
    private Map<String, DatabaseReference> mPlayersReferences;
    private Map<String, ChildEventListener> mPlayersListeners;
    private List<String> mPlayersReady;

    public PlayersMonitor(DatabaseReference gameDbReference, GameEventsObserver observer) {
        mGameDbReference = gameDbReference;
        mGameEventsObserver = observer;
        initVariables();
    }

    private void initVariables() {
        mNumberOfPlayers = GameInitData.PLAYERS_MAX_STANDARD;
        mPlayersReferences = new HashMap<>();
        mPlayersListeners = new HashMap<>();
        mPlayersReady = new LinkedList<>();
    }

    public void monitorPlayer(String userId) {
        createPlayerReference(userId);
        attachPlayerListener(userId);
    }

    private void createPlayerReference(String userId) {
        DatabaseReference playerRef = mGameDbReference.child(NetworkGame.PLAYERS).child(userId);
        mPlayersReferences.put(userId, playerRef);
    }

    private void attachPlayerListener(String userId) {
        DatabaseReference playerRef = mPlayersReferences.get(userId);
        if (playerRef != null) {
            ChildEventListener listener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    DatabaseReference snapRef = dataSnapshot.getRef();
                    if (snapRef.equals(playerRef.child(NetworkGame.STATE))) {
                        Integer state = dataSnapshot.getValue(Integer.class);
                        if (state != null && state == NetworkGame.READY) {
                            addReadyPlayer(userId);
                        }
                    }
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    DatabaseReference snapRef = dataSnapshot.getRef();
                    if (snapRef.equals(playerRef.child(NetworkGame.STATE))) {
                        Integer state = dataSnapshot.getValue(Integer.class);
                        if (state != null && state == NetworkGame.READY) {
                            addReadyPlayer(userId);
                        } else if (state != null && state == NetworkGame.OUT_OF_TIME) {
                            mGameEventsObserver.onPlayerOutOfTime(userId);
                        }
                    }
                }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            playerRef.addChildEventListener(listener);
        } else {
            Log.e(LOG_TAG, "[playerListener] player reference is null for uId: " + userId);
        }
    }

    private void addReadyPlayer(String userId) {
        if (!mPlayersReady.contains(userId)) mPlayersReady.add(userId);
        if (mPlayersReady.size() == mNumberOfPlayers) {
            sendPlayersToObserver();
        }
    }

    private void sendPlayersToObserver() {
        mGameDbReference.child(NetworkGame.PLAYERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<NetworkGamePlayer> players = new ArrayList<>();
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            NetworkGamePlayer player = snap.getValue(NetworkGamePlayer.class);
                            if (player != null && player.getState() == NetworkGame.READY) {
                                players.add(player);
                            }
                        }
                        if (players.size() == mNumberOfPlayers) {
                            mGameEventsObserver.onAllPlayersReady(players);
                            mPlayersReady = new LinkedList<>();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
    }

    public void removePlayer(String userId) {
        removeFromReadyPlayers(userId);
        detachPlayerListener(userId);
        removePlayerReference(userId);
        mGameEventsObserver.onPlayerLeft(userId);
    }

    private void removeFromReadyPlayers(String userId) {
        for (int i = mPlayersReady.size() - 1; i >= 0; i--) {
            if (mPlayersReady.get(i).equals(userId)) mPlayersReady.remove(i);
        }
    }

    private void detachPlayerListener(String userId) {
        ChildEventListener listener = mPlayersListeners.get(userId);
        if (listener != null) {
            mPlayersReferences.get(userId).removeEventListener(listener);
        }
        mPlayersListeners.remove(userId);
    }

    private void removePlayerReference(String userId) {
        mPlayersReferences.remove(userId);
    }
}
