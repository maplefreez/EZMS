package ez.game.ezms.server.login.handle;

import ez.game.ezms.mina.MaplePacketDecoder;
import ez.game.ezms.server.login.handle.func.Nop;
import ez.game.ezms.server.packet.OptionFunc;
import ez.game.ezms.server.login.LoginServerOptionFuncs;
import ez.game.ezms.server.client.MapleClient;
import ez.game.ezms.server.login.LoginServer;
import ez.game.ezms.server.packet.LoginServerPacketCreator;
import ez.game.ezms.server.packet.MaplePacket;
import ez.game.ezms.server.world.WorldServer;
import ez.game.ezms.tools.MapleAESOFB;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import ez.game.ezms.tools.RandomHelper;
import ez.game.ezms.constance.ServerConstants;
import org.apache.mina.transport.socket.SocketSessionConfig;

/**
 * Login Server的执行类
 */
public class LoginHandler extends IoHandlerAdapter {

    /**
     * 当用户在大区选择界面（选择“风之大陆”、“云之大陆”等等）
     * 点击任意区时，触发此事件。标志客户端开始和服务端进行交互，
     * 最初的会话从此处建立。
     */
    @Override
    public void sessionOpened (final IoSession session) throws Exception {
        System.out.println ("New session open on LoginServer");
        // 以下是握手操作，握手成功后，将进入登录界面（输入账号和密码的界面）.
        final byte serverRecv[] = new byte[] {70, 114, 122, (byte) RandomHelper.nextInt(255)};
        final byte serverSend[] = new byte[] {82, 48, 120, (byte) RandomHelper.nextInt(255)};
        final byte ivRecv[] = ServerConstants.Use_Fixed_IV ? new byte[]{9, 0, 0x5, 0x5F} : serverRecv;
        final byte ivSend[] = ServerConstants.Use_Fixed_IV ? new byte[]{1, 0x5F, 4, 0x3F} : serverSend;

        MaplePacketDecoder.DecoderState decoderState = new MaplePacketDecoder.DecoderState();
        session.setAttribute(MaplePacketDecoder.DECODER_STATE_KEY, decoderState);

        // 此处两个对象是与AES加密解密有关的，用于
        // 传输，但目前鄙人没有能力改动它们俩。
        MapleAESOFB sendCypher = new MapleAESOFB(ivSend, 0xFFFF - ServerConstants.MAPLE_VERSION, false);
        MapleAESOFB recvCypher = new MapleAESOFB(ivRecv, ServerConstants.MAPLE_VERSION, false);
        MapleClient client = new MapleClient (sendCypher, recvCypher, session);

        session.setAttribute (ServerConstants.CLIENT_ENTITY_KEY, client);

        // 创建握手报文
        MaplePacket packet = LoginServerPacketCreator.createShakeHands();
        // 写入
        session.write (packet.getByteArray ());

        session.setAttribute(IdleStatus.READER_IDLE, 60);
        session.setAttribute(IdleStatus.WRITER_IDLE, 60);
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
        System.out.println ("LoginServer session close");

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
        OptionFunc func = LoginServerOptionFuncs.mapOptionCode2Function(optCode);
        /* 此处应该有未实现功能码的输出。 */
        if  (func == Nop.nop)
            System.out.println ("Missing implementation option code:" + optCode);
        /* 响应处理。 */
        func.process (body, session);
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
//    @Override
//    public void messageReceived(final IoSession session, final Object message) {
//        try {
//            final SeekableLittleEndianAccessor slea = new GenericSeekableLittleEndianAccessor(new ByteArrayByteStream((byte[]) message));
//            if (slea.available() < 2) {
//                return;
//            }
//            final short header_num = slea.readShort();
//            // Console output part
//            for (final RecvPacketOpcode recv : RecvPacketOpcode.values()) {
//                if (recv.getValue() == header_num) {
//
//                    if (debugMode) {//&& !RecvPacketOpcode.isSpamHeader(recv)
//                        final StringBuilder sb = new StringBuilder("Received data 已處理 :" + String.valueOf(recv) + "\n");
//                        sb.append(tools.HexTool.toString((byte[]) message)).append("\n").append(tools.HexTool.toStringFromAscii((byte[]) message));
//                        System.out.println(sb.toString());
//                    }
//                    final MapleClient c = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);
//                    if (!c.isReceiving()) {
//                        return;
//                    }
//                    if (recv.NeedsChecking()) {
//                        if (!c.isLoggedIn()) {
//                            return;
//                        }
//                    }
//                    if (c.getPlayer() != null && c.isMonitored()) {
//                        if (!blocked.contains(recv)) {
//                            FileoutputUtil.log("日志/logs/Monitored/" + c.getPlayer().getName() + ".txt", String.valueOf(recv) + " (" + Integer.toHexString(header_num) + ") Handled: \r\n" + slea.toString() + "\r\n");
//
////                            FileWriter fw = new FileWriter(new File("日志/logs/MonitorLogs/" + c.getPlayer().getName() + "_log.txt"), true);
////                            fw.write(String.valueOf(recv) + " (" + Integer.toHexString(header_num) + ") Handled: \r\n" + slea.toString() + "\r\n");
////                            fw.flush();
////                            fw.close();
//                        }
//                    }
//                    if (Log_Packets) {
//                        log(slea, recv, c, session);
//                    }
//                    handlePacket(recv, slea, c, cs);
//
//                    //Log after the packet is handle. You'll see why =]
//                    FileWriter fw = isLoggedIP(session);
//                    if (fw != null && !blocked.contains(recv)) {
//                        if (recv == RecvPacketOpcode.PLAYER_LOGGEDIN && c != null) { // << This is why. Win.
//                            fw.write(">> [AccountName: "
//                                    + (c.getAccountName() == null ? "null" : c.getAccountName())
//                                    + "] | [IGN: "
//                                    + (c.getPlayer() == null || c.getPlayer().getName() == null ? "null" : c.getPlayer().getName())
//                                    + "] | [Time: "
//                                    + FileoutputUtil.CurrentReadable_Time()
//                                    + "]");
//                            fw.write(nl);
//                        }
//                        fw.write("[" + recv.toString() + "]" + slea.toString(true));
//                        fw.write(nl);
//                        fw.flush();
//                    }
//                    return;
//                }
//            }
//            if (debugMode) {
//                final StringBuilder sb = new StringBuilder("Received data 未處理 : ");
//                sb.append(tools.HexTool.toString((byte[]) message)).append("\n").append(tools.HexTool.toStringFromAscii((byte[]) message));
//                System.out.println(sb.toString());
//            }
//        } catch (RejectedExecutionException ex) {
//        } catch (Exception e) {
//            FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void sessionIdle(final IoSession session, final IdleStatus status) throws Exception {
//        final MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);
//
//        /*
//         * if (client != null && client.getPlayer() != null) {
//         * System.out.println("玩家 "+ client.getPlayer().getName() +" 正在掛網"); }
//         */
//        if (client != null) {
//            client.sendPing();
//        } else {
//            session.close();
//            return;
//        }
//        super.sessionIdle(session, status);
//    }

}
