/**
 * Author : ez
 * Date : 2019/10/23
 * Description : LoginServer实现。
 */

package ez.game.ezms.server.login;

import ez.game.ezms.conf.ServerConfig;
import ez.game.ezms.constance.DefaultConf;
import ez.game.ezms.exception.LoginServerException;
import ez.game.ezms.mina.MapleCodecFactory;
import ez.game.ezms.server.client.MapleClient;
import ez.game.ezms.server.login.handle.LoginHandler;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;

/**
 * Login服务器，主要负责登录用户事务处理。
 * 单例模式运行。
 */
public class LoginServer {
//    /**
//     * 此键用于存储client实例，每个Client实例保存在session中，
//     * 暂且在此处定义这个键值。
//     */
//    public final static String CLIENT_ENTITY_KEY = "ClientEntity";

    /**
     * 单例
     */
    private static LoginServer singleton;

    /**
     * 最大在线人数。
     */
    private int userMaxOnline;

    /**
     * Server中文名。
     */
    private String serverName;

    /**
     * 是否是维护模式，
     * 此模式下只能管理员登录。
     */
    private boolean isMaintain;

    /**
     * 是否支持账号自动注册。
     */
    private boolean autoRegister;

    /**
     * 一个频道上能创建的最大角色数。
     */
    private int maxCharacters;

    /**
     * LoginServer监听的TCP端口号。
     */
    private int port;

    /**
     * 用于存储所有链接上LoginServer的会话。
     * 在没有成功登录前，对话
     * 将暂时存储在这个位置。
     *
     * key是在SessionOpened()事件中生成的随机键,
     * 目前暂且存储在其Session中的“ClientID”键上。
     */
//    private HashMap <Integer, MapleClient> clientList;

    /**
     * 接收链接的accept.
     */
    private IoAcceptor acceptor;

    private LoginServer () {
//        clientList = new HashMap <> ();
    }

//    public MapleClient getClient (int key) {
//        return clientList.get (key);
//    }

    /**
     * 将新的client插入表中。
     * @param key
     * @param client
     */
//    public void addClient (Integer key, MapleClient client) {
//        clientList.put (key, client);
//    }

    /**
     * 初始化LoginServer服务器，只能进行一次初始化工作。
     * 多次初始化抛出Exception。
     *
     * @param conf   来自配置文件"srvconfig.conf"
     * @return   初始化成功时返回实例引用。
     * @throws LoginServerException
     */
    public static LoginServer initialize (ServerConfig conf) throws LoginServerException {
        if (singleton != null)
            throw new LoginServerException ("Error in initialize LoginServer. Shouldn't be twice!");
        singleton = new LoginServer ();

        // 配置工作。
        singleton.setUserMaxOnline (conf.getInt ("user.maxonline", 140));
        singleton.setServerName(conf.getString ("server.name", "EZMS"));
//        flag = ServerProperties.getProperty("flag", (byte) 3);
        singleton.setMaintain(conf.getBoolean ("server.maintain", false));
        singleton.setMaxCharacters (conf.getInt ("user.maxcharacter", 30));
        singleton.setAutoRegister (conf.getBoolean ("server.autoreg", false));
//        checkMacs = Boolean.parseBoolean(ServerProperties.getProperty("checkMacs", "false"));
        singleton.setPort (conf.getShort ("server.login.port", DefaultConf.DEF_LOGINSRV_PORT));

        return singleton;
    }

    /**
     * 获取实例引用，必须在初始化完成后才能正确返回，否则抛出异常。
     * 初始化例程请调用initialize().
     * @return    实例引用。
     * @throws LoginServerException
     */
    public static LoginServer getEntity () throws LoginServerException {
        if (null == singleton)
            throw new LoginServerException ("Error in getting LoginServer entity. Should be initialize first.");
        return singleton;
    }

    /**
     * 运行此实例，开始监听端口，接收登录请求。
     *
     * @throws LoginServerException
     */
    public void run () throws LoginServerException {
//        LoginServer server = LoginServer.getEntity ();

        //// 此处使用Apache mina库。
        IoBuffer.setUseDirectBuffer(false);
        IoBuffer.setAllocator(new SimpleBufferAllocator());

        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain ().addLast ("codec", new ProtocolCodecFilter(new MapleCodecFactory ()));
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);

        try {
            acceptor.setHandler(new LoginHandler ());
            acceptor.bind(new InetSocketAddress (port));
            ((SocketSessionConfig) acceptor.getSessionConfig()).setTcpNoDelay(true);

            System.out.println ("LoginServer is now listening at port " + port);
        } catch (IOException e) {
            System.err.println("Cannot bind port " + port + ": " + e);
        }
    }

    /**
     * 释放所有的资源，中断监听，关闭服务器。
     */
    public void dispose () {
        if (null == singleton) return;
        // TODO...

        singleton = null;
    }


    public int getUserMaxOnline() {
        return userMaxOnline;
    }

    private void setUserMaxOnline(int userMaxOnline) {
        this.userMaxOnline = userMaxOnline;
    }


    public String getServerName () {
        return serverName;
    }

    private void setServerName (String serverName) {
        this.serverName = serverName;
    }

    public boolean isMaintain() {
        return isMaintain;
    }

    private void setMaintain(boolean maintain) {
        isMaintain = maintain;
    }

    public boolean isAutoRegister() {
        return autoRegister;
    }

    private void setAutoRegister(boolean autoRegister) {
        this.autoRegister = autoRegister;
    }

    public int getMaxCharacters() {
        return maxCharacters;
    }

    private void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    public int getPort() {
        return port;
    }

    private void setPort(int port) {
        this.port = port;
    }
}
