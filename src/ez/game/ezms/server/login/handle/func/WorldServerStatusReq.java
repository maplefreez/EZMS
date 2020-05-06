package ez.game.ezms.server.login.handle.func;

import ez.game.ezms.server.packet.LoginServerPacketCreator;
import ez.game.ezms.server.packet.MaplePacket;
import ez.game.ezms.server.packet.OptionFunc;
import ez.game.ezms.server.packet.PacketStreamLEReader;
import ez.game.ezms.server.world.WorldServer;
import ez.game.ezms.server.world.WorldServerSet;
import org.apache.mina.core.session.IoSession;

/**
 * 0x03 用户请求世界服务器状态。
 * 当用户点击“蓝蜗牛”、“红螃蟹”等服务器
 * 时，客户端界面会弹出所有频道在线状况的
 * 下拉列表，在弹出之前客户端会发送一个
 * 0x03功能码的报文，服务端对应处理函数
 * 定义于此。
 */
public class WorldServerStatusReq implements OptionFunc {

    @Override
    public void process (byte [] message, IoSession session) {
        PacketStreamLEReader reader = new PacketStreamLEReader (message);
        short serverId = reader.readByte ();  // 用户点选的世界服务器

        /* 一般不会出现这个情况吧，除非client出错了。以防万一。 */
        if (serverId >= WorldServerSet.serverSet.length)
            serverId = 0;

        WorldServer server = WorldServerSet.serverSet [serverId];

        long loginedPlayerCount = server.countLoginRole ();
        long playerCountLimit = server.getLoginLimit ();
        int statusCode = 0;  // 正常登陆的状态是0.

        if (loginedPlayerCount >= playerCountLimit) {
            /* 在线人数大于最大值 */
            statusCode = 2;
        } else if (loginedPlayerCount >= playerCountLimit * 0.8f) {
            /* 如果在线人数大于最大值的80%，判定为繁忙。 */
            statusCode = 1;
        }
        /* 可正常登陆。 */
        MaplePacket packet = LoginServerPacketCreator.createWorldServerStatus (statusCode);
        session.write (packet.getByteArray ());
    }
}
