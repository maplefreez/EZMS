package ez.game.ezms.server.world.handle;

import ez.game.ezms.constance.ServerConstants;
import ez.game.ezms.mina.MaplePacketDecoder;
import ez.game.ezms.server.client.MapleClient;
//import ez.game.ezms.server.packet.LoginServerPacketCreator;
import ez.game.ezms.server.packet.MaplePacket;
import ez.game.ezms.server.packet.OptionFunc;
import ez.game.ezms.server.packet.WorldServerPacketCreator;
//import ez.game.ezms.server.world.WorldServer;
import ez.game.ezms.server.world.WorldServerOptionFuncs;
import ez.game.ezms.server.world.handle.func.Nop;
import ez.game.ezms.tools.MapleAESOFB;
import ez.game.ezms.tools.RandomHelper;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;

/**
 * World Server的mina执行类
 */
public class WorldServerHandler extends IoHandlerAdapter {
    /* SESSION键，用于暂存密钥对。之后将设置在MapleClient实体上。*/
    public final static String SENDIV_KEY = "AESSNDIVKEY";
    public final static String RECVIV_KEY = "AESRCVIVKEY";

    /**
     * 当用户在点选角色后，角色进入地图会先与WorldServer通信，这是会话开启的第一个事件。
     */
    @Override
    public void sessionOpened (final IoSession session) throws Exception {
        System.out.println("New session opened on ChannelServer");

        final byte serverRecv[] = new byte[]{70, 114, 122, (byte) RandomHelper.nextInt(255)};
        final byte serverSend[] = new byte[]{82, 48, 120, (byte) RandomHelper.nextInt(255)};

        MapleAESOFB sendCypher = new MapleAESOFB(serverSend, 0xFFFF - ServerConstants.MAPLE_VERSION, false);
        MapleAESOFB recvCypher = new MapleAESOFB(serverRecv, ServerConstants.MAPLE_VERSION, false);

        // 创建握手报文
        MaplePacket packet = WorldServerPacketCreator.createShakeHands (serverRecv, serverSend);
        // 暂时存在session。
        session.setAttribute (RECVIV_KEY, recvCypher);
        session.setAttribute (SENDIV_KEY, sendCypher);
        session.setAttribute(IdleStatus.READER_IDLE, 60);
        session.setAttribute(IdleStatus.WRITER_IDLE, 60);

        // 返回握手报文给客户端。
        session.write (packet.getByteArray ());
    }

    /**
     * 设置一些与通信相关的必要参数。
     */
    @Override
    public void sessionCreated (IoSession session) throws Exception {
        SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();
        cfg.setReceiveBufferSize (2 * 1024 * 1024);
        cfg.setReadBufferSize (2 * 1024 * 1024);
        cfg.setKeepAlive (true);
        cfg.setSoLinger (0);
    }

    @Override
    public void sessionClosed (final IoSession session) throws Exception {
        System.out.println ("ChannelServer session close");
    }

    @Override
    public void messageReceived(final IoSession session, final Object message) {
        byte [] mess = (byte []) message;

        // 获取功能码
        int optCode = mess [0];
        /* 去掉功能码后剩余的报文是主体。 */
        byte [] body = new byte [mess.length - 1];
        System.arraycopy (mess, 1, body, 0, mess.length - 1);

        /* 功能码映射到处理方法。 */
        OptionFunc func = WorldServerOptionFuncs.mapOptionCode2Function (optCode);
        if (func == Nop.nop) {
            System.out.println ("Missing implementation WorldServer Option code: " + optCode);
        }

        func.process (body, session);
    }

    @Override
    public void sessionIdle(final IoSession session, final IdleStatus status) {
        MapleClient client = (MapleClient) session.getAttribute (ServerConstants.CLIENT_ENTITY_KEY);
        System.out.println ("WorldServer idle");

        /*
         * if (client != null && client.getPlayer() != null) {
         * System.out.println("玩家 "+ client.getPlayer().getName() +" 正在掛網"); }
         */
//        if (client != null) {
//            client.sendPing();
//        } else {
//            session.close();
//            return;
//        }
//        super.sessionIdle(session, status);
    }

//
//    @Override
//    public void sessionClosed(final IoSession session) throws Exception {
//        final MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);
//
//        if (client != null) {
//            try {
//                FileWriter fw = isLoggedIP(session);
//                if (fw != null) {
//                    fw.write("=== Session Closed ===");
//                    fw.write(nl);
//                    fw.flush();
//                }
//                client.disconnect(true, cs);
//            } finally {
//                session.close();
//                session.removeAttribute(MapleClient.CLIENT_KEY);
//            }
//        }
//        super.sessionClosed(session);
//    }
//


}
