package ez.game.ezms.exception;

public class WorldServerException extends Exception {

    /**
     * ChannelServer出错。
     * @param message   错误信息。
     */
    public WorldServerException(String message) {
        super (message);
    }

    private WorldServerException() {}
}
