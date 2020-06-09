package ez.game.ezms.conf;

public class DefaultConf {

    /**
     * 默认的登录服务器端口。
     */
    public static short DEF_LOGINSRV_PORT = 8484;

    /**
     * 默认的频道服务器监听端口号，起始号从此开始。
     * 每增加一个频道，端口号+1。
     */
    public static short DEF_CHANNEL_PORT_START = 7575;

    /**
     * 一个世界服务器下默认开启多少频道服务器。
     */
    public static byte DEF_CHANNEL_COUNT = 3;
}
