package ez.game.ezms.server.world.command.func;

import ez.game.ezms.client.MapleClient;
import ez.game.ezms.client.MapleRole;
import ez.game.ezms.client.model.MapleAccount;
import ez.game.ezms.server.packet.MaplePacket;
import ez.game.ezms.server.packet.WorldServerPacketCreator;
import ez.game.ezms.server.world.command.CommandFunc;
import ez.game.ezms.wz.MapleWZDataCache;
import ez.game.ezms.wz.model.cache.MapleWZMap;
import org.apache.mina.core.session.IoSession;

/**
 * 通过命令进入指定地图。
 */
public class EnterSpecifiedMap implements CommandFunc {

    @Override
    public String getCommandName () {
        return "@!entermap";
    }

    @Override
    public String getDescription () {
        return this.getCommandName () + " <mapID>";
    }

    @Override
    public boolean execute (String [] args, MapleClient client) {
        if (args.length <= 0) return false;

        String toMapIDStr = args [0];
        int toMapID = 0;

        try {
            toMapID = Integer.parseInt (toMapIDStr);
        } catch (Exception ex) {
            return false;
        }

        /* 获取角色及会话。 */
        MapleRole role = client.getCurrentRole();
        IoSession session = client.getSession ();
        if (role == null || session == null) return false;

        /* 获取地图。 */
        MapleWZMap toMap = MapleWZDataCache.getMapByWZID (toMapID);
        if (toMap == null) {
            // 报告客户端找不到地图。
            MaplePacket msgPacket = WorldServerPacketCreator.sendMessage (client,
                    "Cannot find map " + toMapID, 5);
            session.write (msgPacket.getByteArray ());
            return false;
        }

        /* 发送报文。 */
        MaplePacket packet = WorldServerPacketCreator.enterMap (client, role, toMap);
        session.write (packet.getByteArray ());

        /* 更改状态。 */
        role.enterMap (role.getMapID (), toMapID);
        return true;
    }
}
