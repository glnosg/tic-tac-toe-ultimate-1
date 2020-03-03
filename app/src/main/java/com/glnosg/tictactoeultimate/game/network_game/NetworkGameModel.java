package com.glnosg.tictactoeultimate.game.network_game;

import android.util.Log;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkMove;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class NetworkGameModel {

    private final String LOG_TAG = NetworkGameModel.class.getSimpleName();

    private NetworkGamePresenter mGamePresenter;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String mGameId, mThisUserId;
    private DatabaseReference mGameDbReference, mUsersDbReference;

    private GameMonitor mGameMonitor;
    private PlayersMonitor mPlayersMonitor;
    private UsersMonitor mUsersMonitor;

    public NetworkGameModel(NetworkGamePresenter gamePresenter, String gameId) {
        mGamePresenter = gamePresenter;
        mGameId = gameId;
        initDbReferences(gameId);
        initUserAuth();
    }

    private void initDbReferences(String gameId) {
        FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();
        mGameDbReference = firebaseDb.getReference().child(NetworkGame.GAMES).child(gameId);
        mUsersDbReference = firebaseDb.getReference().child(NetworkGame.USERS);
    }

    private void initUserAuth() {
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    public void attachAuthListener() {
        if (mAuthStateListener == null) {
            mAuthStateListener = (FirebaseAuth firebaseAuth) -> {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    mGamePresenter.onLeaveClicked();
                } else {
                    mThisUserId = user.getUid();
                    initGameMonitor();
                }
            };
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        }
    }

    private void initGameMonitor() {
        if (mGameMonitor == null) {
            mGameMonitor = new GameMonitor(mGameId, mThisUserId);
            mGameMonitor.attachGameEventsObserver(mGamePresenter.getGameEventsObserver());
            mGameMonitor.fetchGameInitData();
        }
    }

    public void detachListeners() {
        mGameMonitor.detachListeners();
        detachAuthListener();
    }

    private void detachAuthListener() {
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
            mAuthStateListener = null;
        }
    }

    public String getThisUserId() {
        return mThisUserId;
    }

    public void attachDbListeners() {
        mPlayersMonitor = new PlayersMonitor(mGameDbReference, mGamePresenter.getGameEventsObserver());
        mGameMonitor.attachDbListeners(mPlayersMonitor);
    }

    public void setGameState(int state) {
        mGameDbReference.child(NetworkGame.STATE).setValue(state);
    }

    public void setThisPlayerState(int state) {
        if (state ==  NetworkGame.READY || state == NetworkGame.WANTS_NEXT || state == 0) {
            mGameDbReference.child(NetworkGame.PLAYERS).child(mThisUserId).child(NetworkGame.STATE)
                    .setValue(state);
        } else {
            Log.e(LOG_TAG, "[setThisPlayerState] method received invalid value: " + state);
        }
    }

    public void setPlayerOutOfTime(String userId) {
        mGameDbReference.child(NetworkGame.PLAYERS).child(userId).child(NetworkGame.STATE)
                .setValue(NetworkGame.OUT_OF_TIME);
    }

    public void setAllPlayersState(int state) {
        mGameDbReference.child(NetworkGame.PLAYERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            DatabaseReference snapRef = snap.getRef();
                            snapRef.child(NetworkGame.STATE).setValue(state);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
    }

    public void declinePlayer(String userId) {
        mGameDbReference.child(NetworkGame.PLAYERS)
                .child(userId).child(NetworkGame.STATE)
                .setValue(NetworkGame.DECLINED);
    }

    public void acceptPlayer(String userId, int playerId) {
        mGameDbReference.child(NetworkGame.PLAYERS).child(userId).child(NetworkGamePlayer.PLAYER_ID)
                .setValue(playerId);
        mGameDbReference.child(NetworkGame.PLAYERS).child(userId).child(NetworkGame.STATE)
                .setValue(NetworkGame.ACCEPTED);
    }

    public void incrementThisUserGamesPlayed() {
        DatabaseReference gamesPlayedRef =
                mUsersDbReference.child(mThisUserId).child(NetworkGame.GAMES_PLAYED);
        gamesPlayedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer gamesPlayed = dataSnapshot.getValue(Integer.class);
                if (gamesPlayed == null) gamesPlayedRef.setValue(1);
                else gamesPlayedRef.setValue(gamesPlayed + 1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void incrementUserGamesWon(String userId) {
        DatabaseReference gamesWonRef =
                mUsersDbReference.child(userId).child(NetworkGame.GAMES_WON);
        gamesWonRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer gamesWon = dataSnapshot.getValue(Integer.class);
                if (gamesWon == null) gamesWonRef.setValue(1);
                else gamesWonRef.setValue(gamesWon + 1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void incrementThisUserGamesLeft() {
        DatabaseReference gamesLeftRef =
                mUsersDbReference.child(mThisUserId).child(NetworkGame.GAMES_LEFT);
        gamesLeftRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer gamesLeft = dataSnapshot.getValue(Integer.class);
                if (gamesLeft == null) gamesLeftRef.setValue(1);
                else gamesLeftRef.setValue(gamesLeft + 1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void setThisUserGamePlayed() {
        mUsersDbReference.child(mThisUserId).child(NetworkGame.GAME_PLAYED_ID).setValue(mGameId);
    }

    public void removeThisPlayerFromGame() {
        Log.d(LOG_TAG, "[removeThisPlayerFromGame]");
        removeUserGamePlayed(mThisUserId);
        mGameDbReference.child(NetworkGame.PLAYERS).child(mThisUserId).removeValue();
        if (mGamePresenter.isThisCreator()) {
            mGameDbReference.child(NetworkGame.CREATOR_ID).setValue(NetworkGame.NO_CREATOR);
        }
        removeGameIfNoMorePlayers();
    }

    public void removePlayerFromGame(String userId) {
        removeUserGamePlayed(userId);
        mGameDbReference.child(NetworkGame.PLAYERS).child(userId).removeValue();
        if (mGamePresenter.isCreator(userId)) {
            mGameDbReference.child(NetworkGame.CREATOR_ID).setValue(NetworkGame.NO_CREATOR);
        }
        removeGameIfNoMorePlayers();
    }

    private void removeUserGamePlayed(String userId) {
        mUsersDbReference.child(userId).child(NetworkGame.GAME_PLAYED_ID).removeValue();
    }

    public void removeGameIfNoMorePlayers() {
        mGameDbReference.child(NetworkGame.PLAYERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    mGameDbReference.removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void removeThisGameFromDb() {
        mGameDbReference.removeValue();
    }

    public void activatePlayer(int playerId) {
        mGameDbReference.child(NetworkGame.ACTIVE_PLAYER_ID).setValue(playerId);
    }

    public void addMove(NetworkMove move) {
        mGameDbReference.child(NetworkGame.MOVES).push().setValue(move);
    }

    public void setWinnerId(int winnerId) {
        mGameDbReference.child(NetworkGame.WINNER_ID).setValue(winnerId);
    }

    public void removeMoves() {
        mGameDbReference.child(NetworkGame.MOVES).removeValue();
    }
}
