package ez.game.ezms.server.login.handle.func;

import ez.game.ezms.constance.ServerConstants;
import ez.game.ezms.client.model.MapleAccount;
import ez.game.ezms.client.MapleClient;
import ez.game.ezms.server.packet.LoginServerPacketCreator;
import ez.game.ezms.server.packet.MaplePacket;
import ez.game.ezms.server.packet.OptionFunc;
import ez.game.ezms.server.packet.PacketStreamLEReader;
import ez.game.ezms.server.world.WorldServer;
import ez.game.ezms.server.world.WorldServerSet;
import org.apache.mina.core.session.IoSession;

/**
 * 用户点击选择某个角色时，此处处理响应。
 * 接收功能码为0x05
 */
public class SelectRole implements OptionFunc {

    /**
     * 报文格式：
     *
     * @param message
     * @param session
     */
    @Override
    public void process (byte [] message, IoSession session) {
        PacketStreamLEReader reader = new PacketStreamLEReader (message);
        int roleID = reader.readInt (); // 角色ID，数据库ID。

        MapleClient client = null;
        try {
            client = (MapleClient) session.getAttribute (ServerConstants.CLIENT_ENTITY_KEY);
        } catch (Exception ex) {
            ex.printStackTrace ();
            return;
        }

        // 世界服务器编码。
        int worldID = client.getWorldID ();
        WorldServer worldServer = WorldServerSet.getWorldServer (worldID);

        if (worldServer == null) {
            /* 此处是失败报文。不应该会运行于此。 */
            return;
        }

        MapleAccount account = client.getAccountEntity ();
        account.loginRole (roleID);

        /* 准备登录进世界服务器。先暂存在此。 */
        WorldServerSet.beforeRoleLogin (client);

        /* 创建世界服务器地址报文并发送。 */
        MaplePacket packet = LoginServerPacketCreator.createWorldServerAddress (
                worldServer.getIP(), worldServer.getPort(), roleID);
        session.write (packet.getByteArray ());
    }
}
