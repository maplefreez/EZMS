/**
 * Author : ez
 * Date : 2019/10/23
 * Description : ChannelServer实现。
 */

package ez.game.ezms.server.channel;

import ez.game.ezms.conf.ChannelConfig;
import ez.game.ezms.conf.ServerConfig;
import ez.game.ezms.mina.MapleCodecFactory;
import ez.game.ezms.server.world.handle.WorldServerHandler;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 频道实体，一个世界服务器中有很多平行
 * 的频道，此作为一个频道的实体。按理说，
 * 每个频道的各种游戏数据都应该是在这个实体
 * 中进行刷新，更新，加载...
 *
 * 所以，这个类应该设计成各种结构的一个容器。
 */
public class WorldChannel {
    /**
     * 在此频道的玩家数。
     */
    private AtomicInteger loginRoleCount;

    private IoAcceptor acceptor;

    private volatile boolean isRunning;

    /**
     * 此频道的频道ID，从0开始编号。
     * 目前一个世界服务器最多支持20个频道。
     */
    private byte ID;

    /**
     * 关于此频道的所有的配置数据存储于此。
     * 配置数据来自配置文件，当前世界服务器
     * 的配置文件中，以频道ID区分的配置键。
     */
    private ChannelConfig confData;

    public WorldChannel (int ID, ServerConfig conf) {
        loginRoleCount = new AtomicInteger (0);
        this.ID = (byte) ID; this.isRunning = false;
        initialize (conf);
    }

    /**
     * 初始化配置信息。
     * @param conf  此频道所在的世界服务器的配置文件。
     */
    private void initialize (ServerConfig conf) {
        this.confData = new ChannelConfig (ID, conf);
        this.isRunning = true;
    }

    public void run () throws IOException {
        IoBuffer.setUseDirectBuffer(false);
        IoBuffer.setAllocator(new SimpleBufferAllocator());
        acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
        acceptor.getSessionConfig().setIdleTime (IdleStatus.BOTH_IDLE, 30);
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MapleCodecFactory()));
        acceptor.getFilterChain().addLast("exceutor", new ExecutorFilter(/*threadPool*/));
        acceptor.setHandler (new WorldServerHandler());

        /* 从这条语句开始绑定端口，并接收来自客户端的链接。 */
        acceptor.bind(new InetSocketAddress (confData.getPort ()));
        ((SocketSessionConfig) acceptor.getSessionConfig ()).setTcpNoDelay(true);

        this.isRunning = true;
    }

    public int getLoginRoleCount () {
        return loginRoleCount.get ();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public byte getID() {
        return ID;
    }

    public ChannelConfig getConfData() {
        return confData;
    }

    /**
     * 关闭并回收此频道的所有资源。
     */
    public void dispose () {
        this.acceptor.dispose ();
    }
}
