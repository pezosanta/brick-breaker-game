package networking;

import java.io.Serializable;

public class WBMessage implements Serializable {
    private static int NEXTID = 1;
    public final int id;
    public MsgType msg;
    public Serializable payload;

    public WBMessage(MsgType msg, Serializable payload) {
        id = NEXTID;
        NEXTID += 1;
        this.msg = msg;
        this.payload = payload;
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
