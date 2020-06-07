package networking;

import java.io.Serializable;

public class WBMessage implements Serializable {
    public MsgType msg;
    public Serializable payload;

    public WBMessage(MsgType msg, Serializable payload) {
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
        PLAYER_RELEASED2,
    }
}
