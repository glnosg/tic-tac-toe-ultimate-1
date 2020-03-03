package com.glnosg.tictactoeultimate.game.network_game;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class UsersMonitor {

    private Map<String, Boolean> mIsUserActive;
    private Map<String, DatabaseReference> mUserDbReferences;
    private Map<String, ChildEventListener> mUserDbListeners;

    public UsersMonitor() {
        mIsUserActive = new HashMap<>();
        mUserDbReferences = new HashMap<>();
        mUserDbListeners = new HashMap<>();
    }

    public void addUser(String userId) {

    }

    public void clear() {}
}
