package com.glnosg.tictactoeultimate.game.network_game;

import android.util.Log;

import com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game.NetworkGame;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.NonNull;

public final class InitialGameDataFetcher {

    private static final String LOG_TAG = InitialGameDataFetcher.class.getSimpleName();

    public static void fetchData(DatabaseReference gameDbReference, GameEventsObserver observer) {
        gameDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer boardType =
                        dataSnapshot.child(NetworkGame.BOARD_TYPE).getValue(Integer.class);
                String creatorId =
                        dataSnapshot.child(NetworkGame.CREATOR_ID).getValue(String.class);
                if (boardType != null && creatorId != null) {
                    observer.setInitialData(creatorId, boardType);
                } else {
                    Log.e(LOG_TAG, "[fetchData] getBoardType = " + boardType);
                    Log.e(LOG_TAG, "[fetchData] creatorId = " + creatorId);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
