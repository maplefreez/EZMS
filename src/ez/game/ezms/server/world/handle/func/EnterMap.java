package ez.game.ezms.server.world.handle.func;

import ez.game.ezms.client.MapleClient;
import ez.game.ezms.client.MapleRole;
import ez.game.ezms.constance.ServerConstants;
import ez.game.ezms.server.packet.MaplePacket;
import ez.game.ezms.server.packet.OptionFunc;
import ez.game.ezms.server.packet.PacketStreamLEReader;
import ez.game.ezms.server.packet.WorldServerPacketCreator;
import ez.game.ezms.wz.MapleWZDataCache;
import ez.game.ezms.wz.model.cache.MapleWZMap;
import ez.game.ezms.wz.model.cache.MapleWZMapPortal;
import org.apache.mina.core.session.IoSession;

/**
 * 进入地图，触发操作：
 * 进入传送口，进入快捷传送口，
 * 一些传送命令，NPC对话导致的传送等。
 */
public class EnterMap implements OptionFunc {

    // 注意：自由市场ID= 910000000
    @Override
    public void process (byte [] message, IoSession session) {
        PacketStreamLEReader reader = new PacketStreamLEReader (message);
        byte type = reader.readByte ();
        int toMapID = reader.readInt ();
        String portalName = reader.readMapleASCIIString ();

//        {  // DEBUG.
//            System.out.println("type: " + type);
//            System.out.println("toMapID: " + toMapID);
//            System.out.println("portalName: " + portalName);
//        }

        MapleClient client = (MapleClient) session.getAttribute (ServerConstants.CLIENT_ENTITY_KEY);
        if (client == null) return;

        MapleRole role = client.getAccountEntity ().getCurrentLoginRole ();
        int mapID = role.getMapID ();

        MapleWZMap current = MapleWZDataCache.getMapByWZID (mapID);
        MapleWZMapPortal portalFrom = current.getPortalByName (portalName);
        MapleWZMap toMap = null;
        if (toMapID == -1) { // 由传送口定义得出去向地图的ID。
            /* 若去向地图 id是 999999999，角色通过快捷传送点在本地图传送。 */
            if (portalFrom.getToMapID () == MapleWZMapPortal.STARTPOINT_2_MAPID)
                toMap = current;
            else
                toMap = MapleWZDataCache.getMapByWZID (portalFrom.getToMapID());
        } else {
            /* 此处可能是命令的地图传送吧，或者NPC触发的传送什么的。 */
            toMap = MapleWZDataCache.getMapByWZID (toMapID);
        }

        /* 回发报文。 */
        MaplePacket packet = WorldServerPacketCreator.enterMap (client, role, toMap);
        session.write (packet.getByteArray ());

        /* 角色状态更改。 */
        role.enterMap (current, toMap);
    }


}
