package com.glnosg.tictactoeultimate.game.network_game;

import android.util.Log;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.game_init_data.GameInitData;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkMove;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GameMonitor {

    private final String LOG_TAG = GameMonitor.class.getSimpleName();

    private String mGameId, mThisUserId;
    private DatabaseReference mGameDbReference, mMovesReference, mPlayersReference;
    private ChildEventListener mGameDbListener, mMovesListener, mPlayersListener;
    private PlayersMonitor mPlayersMonitor;
    private GameEventsObserver mGameEventsObserver;
    private int mLastActivePlayerId;

    public GameMonitor(String gameId, String thisUserId) {
        mLastActivePlayerId = GameInitData.NO_ACTIVE_PLAYER;
        mGameId = gameId;
        mThisUserId = thisUserId;
        initDbReferences();
    }

    private void initDbReferences() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        mGameDbReference = db.getReference().child(NetworkGame.GAMES).child(mGameId);
        mMovesReference = mGameDbReference.child(NetworkGame.MOVES);
        mPlayersReference = mGameDbReference.child(NetworkGame.PLAYERS);
    }

    public void attachGameEventsObserver(GameEventsObserver observer) {
        mGameEventsObserver = observer;
    }

    public void fetchGameInitData() {
        InitialGameDataFetcher.fetchData(mGameDbReference, mGameEventsObserver);
    }

    public void attachDbListeners(PlayersMonitor playersMonitor) {
        mPlayersMonitor = playersMonitor;
        mPlayersMonitor.monitorPlayer(mThisUserId);
        attachGameDbListener();
        attachPlayersListener();
        attachMovesListener();
    }

    public void detachListeners() {
        detachPlayersListener();
        detachMovesListener();
        detachGameDbListener();
    }

    private void attachGameDbListener() {
        if (mGameDbListener == null) {
            mGameDbListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    DatabaseReference snapRef = dataSnapshot.getRef();
                    if (snapRef.equals(mGameDbReference.child(NetworkGame.ACTIVE_PLAYER_ID))) {
                        Integer activePlayerId = dataSnapshot.getValue(Integer.class);
                        if (activePlayerId != null && activePlayerId != mLastActivePlayerId) {
                            mLastActivePlayerId = activePlayerId;
                            mGameEventsObserver.onActivePlayerChanged(activePlayerId);
                        }
                    } else if (snapRef.equals(mGameDbReference.child(NetworkGame.WINNER_ID))) {
                        Integer winnerId = dataSnapshot.getValue(Integer.class);
                        if (winnerId != null && winnerId != NetworkGame.GAME_NOT_FINISHED) {
                            mGameEventsObserver.onGameFinished(winnerId);
                        }
                    }
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    DatabaseReference snapRef = dataSnapshot.getRef();
                    if (snapRef.equals(mGameDbReference.child(NetworkGame.ACTIVE_PLAYER_ID))) {
                        Integer activePlayerId = dataSnapshot.getValue(Integer.class);
                        if (activePlayerId != null && activePlayerId != mLastActivePlayerId) {
                            mLastActivePlayerId = activePlayerId;
                            mGameEventsObserver.onActivePlayerChanged(activePlayerId);
                        }
                    } else if (snapRef.equals(mGameDbReference.child(NetworkGame.WINNER_ID))) {
                        Integer winnerId = dataSnapshot.getValue(Integer.class);
                        if (winnerId != null && winnerId != NetworkGame.GAME_NOT_FINISHED) {
                            mGameEventsObserver.onGameFinished(winnerId);
                        }
                    } else if (snapRef.equals(mGameDbReference.child(NetworkGame.CREATOR_ID))) {
                        String creatorId = dataSnapshot.getValue(String.class);
                        if (creatorId != null && creatorId.equals(NetworkGame.NO_CREATOR)) {
                            setNewCreator();
                        } else if (creatorId != null) {
                            mGameEventsObserver.onCreatorChanged(creatorId);
                        }
                    }
                }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    DatabaseReference snapRef = dataSnapshot.getRef();
                    if (snapRef.equals(mGameDbReference.child(NetworkGame.MOVES))) {
                        mGameEventsObserver.onMovesRemoved();
                    }
                }
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            };
        }
        mGameDbReference.addChildEventListener(mGameDbListener);
    }

    public void setNewCreator() {
        mGameDbReference.child(NetworkGame.PLAYERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String userId = snapshot.child(NetworkGamePlayer.USER_ID).getValue(String.class);
                            if (userId != null) {
                                mGameDbReference.child(NetworkGame.CREATOR_ID).setValue(userId);
                                mGameDbReference.child(NetworkGame.PLAYERS).child(userId)
                                        .child(NetworkGamePlayer.PLAYER_ID).setValue(0);
                                return;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
    }

    private void detachGameDbListener() {
        if (mGameDbListener != null) {
            mGameDbReference.removeEventListener(mGameDbListener);
            mGameDbListener = null;
        }
    }

    private void attachPlayersListener() {
        if (mPlayersListener == null) {
            mPlayersListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    NetworkGamePlayer player = dataSnapshot.getValue(NetworkGamePlayer.class);
                    if (player != null && player.getUserId() != null) {
                        if (!player.getUserId().equals(mThisUserId)) {
                            mGameEventsObserver.onPlayerJoined(player);
                            if (player.getState() == NetworkGame.ACCEPTED || player.getState() == NetworkGame.READY) {
                                mPlayersMonitor.monitorPlayer(player.getUserId());
                            }
                        }
                    }
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    NetworkGamePlayer player = dataSnapshot.getValue(NetworkGamePlayer.class);
                    if (player != null && player.getUserId() != null) {
                        if (!player.getUserId().equals(mThisUserId)) {
                            if (player.getState() == NetworkGame.ACCEPTED || player.getState() == NetworkGame.READY) {
                                mPlayersMonitor.monitorPlayer(player.getUserId());
                            }
                        }
                    }
                }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    NetworkGamePlayer player = dataSnapshot.getValue(NetworkGamePlayer.class);
                    if (player != null) {
                        if (player.getState() != 0 && player.getState() != NetworkGame.DECLINED) {
                            mPlayersMonitor.removePlayer(player.getUserId());
                        } else {
                            mGameEventsObserver.onPlayerLeft(player.getUserId());
                        }
                    }
                    removeGameIfNoMorePlayers();
                }
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            };
        }
        mPlayersReference.addChildEventListener(mPlayersListener);
    }

    private void removeGameIfNoMorePlayers() {
        mGameDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(NetworkGame.PLAYERS)) {
                    mGameDbReference.removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void detachPlayersListener() {
        if (mPlayersListener != null) {
            mPlayersReference.removeEventListener(mPlayersListener);
            mPlayersListener = null;
        }
    }

    private void attachMovesListener() {
        if (mMovesListener == null) {
            mMovesListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    NetworkMove move = dataSnapshot.getValue(NetworkMove.class);
                    if (move != null) {
                        Log.d(LOG_TAG, "onMoveAdded");
                        mGameEventsObserver.onMoveAdded(move);
                    }
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            };
        }
        mMovesReference.addChildEventListener(mMovesListener);
    }

    private void detachMovesListener() {
        if (mMovesListener != null) {
            mMovesReference.removeEventListener(mMovesListener);
            mMovesListener = null;
        }
    }
}
