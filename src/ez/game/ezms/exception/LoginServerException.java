package ez.game.ezms.exception;

public class LoginServerException extends Exception {

    /**
     * LoginServer出错。
     * @param message   错误信息。
     */
    public LoginServerException (String message) {
        super (message);
    }

    private LoginServerException () {}
}
