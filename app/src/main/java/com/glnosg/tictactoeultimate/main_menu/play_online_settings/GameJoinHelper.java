package com.glnosg.tictactoeultimate.main_menu.play_online_settings;

import android.util.Log;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGamePlayer;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GameJoinHelper {

    private final String LOG_TAG = GameJoinHelper.class.getSimpleName();

    private DatabaseReference mGamesDbReference;

    private DatabaseReference mJoinerCandidateReference;
    private ChildEventListener mJoinerCandidateListener;
    private String mJoinerCandidateId;

    public GameJoinHelper(DatabaseReference gamesDbReference) {
        mGamesDbReference = gamesDbReference;
    }

    public void joinGame(
            NetworkGamePlayer joinerCandidate,
            String gameId,
            GameJoinEventsObserver observer) {
        mJoinerCandidateId = joinerCandidate.getUserId();
        mJoinerCandidateReference =
                mGamesDbReference
                        .child(gameId)
                        .child(NetworkGame.PLAYERS)
                        .child(mJoinerCandidateId);
        attachJoinerCandidateListener(observer, gameId);
        mJoinerCandidateReference.setValue(joinerCandidate);
    }

    private void attachJoinerCandidateListener(GameJoinEventsObserver observer, String gameId) {
        mJoinerCandidateListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DatabaseReference snapRef = dataSnapshot.getRef();
                if (snapRef.equals(mJoinerCandidateReference.child(NetworkGame.STATE))) {
                    Integer state = dataSnapshot.getValue(Integer.class);
                    if (state != null && state == NetworkGame.ACCEPTED) {
                        observer.onJoinerAccepted(gameId);
                        cleanUp();
                    } else if (state != null && state == NetworkGame.DECLINED) {
                        observer.onJoinerDeclined();
                        mJoinerCandidateReference.removeValue();
                        cleanUp();
                    }
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                observer.onJoinerDeclined();
                mJoinerCandidateReference.removeValue();
                cleanUp();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
        mJoinerCandidateReference.addChildEventListener(mJoinerCandidateListener);
    }

    public void cancelJoining() {
        if (mJoinerCandidateReference != null) {
            mJoinerCandidateReference.removeValue();
        }
        cleanUp();
    }

    private void cleanUp() {
        if (mJoinerCandidateListener != null) {
            mJoinerCandidateReference.removeEventListener(mJoinerCandidateListener);
            mJoinerCandidateListener = null;
            mJoinerCandidateReference = null;
            mJoinerCandidateId = null;
        }
    }
}
