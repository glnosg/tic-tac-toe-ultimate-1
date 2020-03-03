package com.glnosg.tictactoeultimate.main_menu.play_online_settings;

import android.util.Log;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.glnosg.tictactoeultimate.main_menu.play_online_settings.PlayOnlineMVP.SettingsPresenter;
import com.glnosg.tictactoeultimate.main_menu.play_online_settings.PlayOnlineMVP.SettingsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import androidx.annotation.NonNull;

public class PlayOnlineModel implements SettingsModel {

    private final String LOG_TAG = PlayOnlineModel.class.getSimpleName();

    private SettingsPresenter mPresenter;

    private DatabaseReference mGamesDbReference, mUsersDbReference;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private OpenGamesMonitor mOpenGamesMonitor;
    private GameJoinHelper mGameJoinHelper;


    public PlayOnlineModel(SettingsPresenter presenter) {
        mPresenter = presenter;
        initDbVariables();
        initUserAuth();
    }

    private void initDbVariables() {
        FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();
        mGamesDbReference = firebaseDb.getReference().child(NetworkGame.GAMES);
        mUsersDbReference = firebaseDb.getReference().child(NetworkGame.USERS);
    }

    @Override
    public void initGamesDbHelpers(GamesDbEventsObserver observer) {
        mGameJoinHelper = new GameJoinHelper(mGamesDbReference);
        mOpenGamesMonitor = new OpenGamesMonitor();
        mOpenGamesMonitor.attachGamesDbEventsObserver(observer);
        mOpenGamesMonitor.fetchInitialData();
    }

    @Override
    public void attachGamesDbListeners() {
        mOpenGamesMonitor.attachListeners();
    }

    private void initUserAuth() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        attachAuthListener();
    }

    @Override
    public void attachAuthListener() {
        if (mAuthStateListener == null) {
            mAuthStateListener = (@NonNull FirebaseAuth firebaseAuth) -> {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
//                    checkForUnfinishedGame(user.getUid());
                    attachStatsToUser(user.getUid(), user.getDisplayName());
                } else {
                    mPresenter.onUserSignedOut();
                }
            };
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        }
    }

    private void attachStatsToUser(String userId, String userName) {
        mUsersDbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int gamesPlayed, gamesWon, gamesLeft;
                Integer gamesPlayedReceived = dataSnapshot.child(NetworkGame.GAMES_PLAYED).getValue(Integer.class);
                if (gamesPlayedReceived == null) gamesPlayed = 0;
                else gamesPlayed = gamesPlayedReceived;
                Integer gamesWonReceived = dataSnapshot.child(NetworkGame.GAMES_WON).getValue(Integer.class);
                if (gamesWonReceived == null) gamesWon = 0;
                else gamesWon = gamesWonReceived;
                Integer gamesLeftReceived = dataSnapshot.child(NetworkGame.GAMES_LEFT).getValue(Integer.class);
                if (gamesLeftReceived == null) gamesLeft = 0;
                else gamesLeft = gamesLeftReceived;
                mPresenter.onUserSignedIn(userName, gamesPlayed, gamesWon, gamesLeft);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void checkForUnfinishedGame(String userId) {
        DatabaseReference unfinishedGameRef =
                mUsersDbReference.child(userId).child(NetworkGame.GAME_PLAYED_ID);
        unfinishedGameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String unfinishedGameId = dataSnapshot.getValue(String.class);
                if (unfinishedGameId != null) {
                    openUnfinishedGameIfExists(unfinishedGameId, userId);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void openUnfinishedGameIfExists(String gameId, String userId) {
        mGamesDbReference.child(gameId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(NetworkGame.STATE)) {
                    mPresenter.onHasUnfinishedGame(gameId);
                } else {
                    mUsersDbReference.child(userId).child(NetworkGame.GAME_PLAYED_ID).removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    public void detachListeners() {
        mOpenGamesMonitor.detachListeners();
        mGameJoinHelper.cancelJoining();
        detachAuthListener();
    }

    private void detachAuthListener() {
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
            mAuthStateListener = null;
        }
    }

    @Override
    public void createNewGame(Map<String, Object> gameMap, NetworkGamePlayer creator) {
        String gameId = (String) gameMap.get(NetworkGame.GAME_ID);
        if (gameId != null) {
            mGamesDbReference.child(gameId).setValue(gameMap);
            mGamesDbReference.child(gameId).child(NetworkGame.PLAYERS).child(creator.getUserId())
                    .setValue(creator);
            mPresenter.onGameCreated(gameId);
        } else {
            Log.e(LOG_TAG, "[createNewGame] method received a null gameId");
        }
    }

    @Override
    public void joinGame(NetworkGamePlayer joiner, String gameId, GameJoinEventsObserver observer) {
        mGameJoinHelper.joinGame(joiner, gameId, observer);
    }

    @Override
    public void cancelJoining() {
        mGameJoinHelper.cancelJoining();
    }

    @Override
    public String getNewGameId() {
        return mGamesDbReference.push().getKey();
    }

    @Override
    public String getCurrentUserId() {
        if (mFirebaseAuth.getCurrentUser() != null) {
            return mFirebaseAuth.getCurrentUser().getUid();
        } else {
            return null;
        }
    }
}
