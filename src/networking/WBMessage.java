package networking;

import game.GameMap;

import java.io.Serializable;

public class WBMessage implements Serializable {
    private static int NEXTID = 1;
    public final int id;
    public final MsgType msg;
    public final GameMap map;
    public final int keyCode;

    public WBMessage(MsgType msg, GameMap map, int keyCode) {
        this.id = NEXTID;
        NEXTID += 1;
        this.msg = msg;
        this.map = map;
        this.keyCode = keyCode;
    }

    public WBMessage(MsgType msg, GameMap map) {
        this(msg, map, 0);
    }

    public WBMessage(MsgType msg, int keyCode) {
        this(msg, null, keyCode);
    }

    public enum MsgType {
        NONE,
        OK,
        EXITED,
        PLAYER_READY,
        MAP,
        GAME_FINISHED,
        PLAYER_PRESSED,
        PLAYER_RELEASED,
        GAME_STARTED,
    }
}
