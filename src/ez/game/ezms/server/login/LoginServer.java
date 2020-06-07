/**
 * Author : ez
 * Date : 2019/10/23
 * Description : LoginServer实现。
 */

package ez.game.ezms.server.login;

import ez.game.ezms.conf.LoginServerConfig;
import ez.game.ezms.conf.ServerConfig;
import ez.game.ezms.exception.LoginServerException;
import ez.game.ezms.mina.MapleCodecFactory;
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

/**
 * Login服务器，主要负责登录用户事务处理。
 * 单例模式运行。
 */
public class LoginServer {
    /**
     * 单例
     */
    private static LoginServer singleton;

    /**
     * 来自配置文件的配置数据。若此字段不为空，
     * 则调用initialize()不会重新载入配置数据。
     */
    private LoginServerConfig confData;

    /**
     * 接收链接的accept.
     */
    private IoAcceptor acceptor;

    private LoginServer () { }

    /**
     * 初始化登入服务器。
     *
     * @return
     */
    public void initialize (ServerConfig conf) throws LoginServerException {
        if (this.confData != null) return;
        this.confData = new LoginServerConfig (conf);
    }

    /**
     * 初始化LoginServer服务器，只能进行一次初始化工作。
     * 多次调用此函数初始化将抛出Exception。
     *
     * @param conf   来自配置文件"srvconfig.conf"
     * @return   初始化成功时返回实例引用。
     * @throws LoginServerException
     */
    public static LoginServer initializeSingleton(ServerConfig conf) throws LoginServerException {
        if (singleton != null)
            throw new LoginServerException ("Error in initialize LoginServer. Shouldn't be twice!");
        singleton = new LoginServer ();

        singleton.initialize (conf);
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
        //// 此处使用Apache mina库。
        IoBuffer.setUseDirectBuffer(false);
        IoBuffer.setAllocator(new SimpleBufferAllocator());

        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain ().addLast ("codec", new ProtocolCodecFilter(new MapleCodecFactory ()));
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);

        short port = confData.getPort ();

        try {
            acceptor.setHandler(new LoginHandler ());
            acceptor.bind(new InetSocketAddress (port));
            ((SocketSessionConfig) acceptor.getSessionConfig()).setTcpNoDelay(true);
            System.out.println ("LoginServer is now listening at port " + port);
        } catch (IOException e) {
            System.err.println("LoginServer cannot bind port " + port + ": " + e);
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

    public LoginServerConfig getConfData() {
        return confData;
    }
}
