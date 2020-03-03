package com.glnosg.tictactoeultimate.data.data_transfer_objects.network_game;

import java.util.List;

public class NetworkGame {

    /**
     * USERS - DB node containing users' data:
     * GAME_PLAYED_ID - node containing id of game currently played by the user
     */
    public static final String USERS = "users";
    public static final String USER_ID = "userID";
    public static final String IS_ONLINE = "isOnline", IS_ACTIVE = "isActive";
    public static final String GAME_PLAYED_ID = "gamePlayedId";
    public static final String GAMES_PLAYED = "gamesPlayed", GAMES_WON = "gamesWon", GAMES_LEFT = "gamesLeft";

    /**
     * GAMES - DB node containing currently played games:
     * GAME_ID - game's id
     * BOARD_TYPE - is board ultimate or single
     * CREATOR - user that created the game
     * JOINER - user that joined the game
     * JOINER_CANDIDATES - users asking for permission to join the game:
     * ACCEPTED - accepted
     * STATE DECLINED - declined
     * STATE - current state of the game:
     * STATE_OPEN - still accepting users to join
     * STATE_CLOSED - game in progress, not accepting joiners
     * WINNER_ID: playerId of player that won the game:
     * DRAW - value of 'winnerId' node when game finishes with no winnerId
     */
    public static final String GAMES = "games";
    public static final String GAME_ID = "gameId";
    public static final String BOARD_TYPE = "boardType";
    public static final int SINGLE = 1, ULTIMATE = 2;
    public static final String PLAYERS = "players";
    public static final String CREATOR_ID = "creatorId";
    public static final String NO_CREATOR = "noCreator";
    public static final int DECLINED = 1, ACCEPTED = 2, READY = 3, WANTS_NEXT = 4, OUT_OF_TIME = 5;
    public static final String STATE = "state";
    public static final int STATE_OPEN = 1, STATE_CLOSED = 2;
    public static final String ACTIVE_PLAYER_ID = "activePlayerId";
    public static final String WINNER_ID = "winnerId";
    public static final int DRAW = -1, GAME_NOT_FINISHED = 101;
    public static final String MOVES = "moves";

    private int state;
    private int boardType;
    private String gameId, creatorId;
    private int activePlayerId, winnerId;

    public NetworkGame() {
    }

    public NetworkGame(String creatorId, int boardType, String gameId) {
        state = STATE_OPEN;
        this.creatorId = creatorId;
        this.boardType = boardType;
        this.gameId = gameId;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }


    public int getState() {
        return state;
    }

    public int getBoardType() {
        return boardType;
    }

    public String getGameId() {
        return gameId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public int getActivePlayerId() {
        return activePlayerId;
    }

    public int getWinnerId() {
        return winnerId;
    }

}

