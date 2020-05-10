package ez.game.ezms.server.login.handle.func;

import ez.game.ezms.constance.ServerConstants;
import ez.game.ezms.client.model.MapleAccount;
import ez.game.ezms.client.MapleClient;
import ez.game.ezms.client.model.MapleEquipment;
import ez.game.ezms.client.MapleRole;
import ez.game.ezms.server.packet.LoginServerPacketCreator;
import ez.game.ezms.server.packet.MaplePacket;
import ez.game.ezms.server.packet.OptionFunc;
import ez.game.ezms.sql.Business;
import org.apache.mina.core.session.IoSession;

import java.util.List;

/**
 * 当用户点击了某个世界服务器的某个频道时，
 * 此处是响应处理。
 */
public class RoleListReq implements OptionFunc {

    /**
     * 报文格式：
     *   <1B: 功能码4> <1B: 用户点选的世界服务器ID> <1B:用户点选的频道ID>
     *
     *   世界服务器ID从0开始。
     *   频道ID从1开始，貌似到20结束。
     *
     * @param message  去掉了功能码（1Byte）的报文体。
     * @param session  会话实体。
     */
    @Override
    public void process (byte [] message, IoSession session) {
        /* 获取用户点选的世界服务器和频道。都是ID. */
        int serverID = message [0];
        int channelID = message [1];

        MapleClient client = null;

        try {
            client = (MapleClient) session.getAttribute (ServerConstants.CLIENT_ENTITY_KEY);
        } catch (Exception ex) {
            ex.printStackTrace ();
            return;
        }

        MapleAccount account = client.getAccountEntity ();
        /* 两者写入客户端实体。 */
        client.enterWorldAndChannel ((byte) serverID, (byte) channelID);

        /* 查找数据库中当前服务器用户创建的角色信息。 */
        List<MapleRole> roles = searchRolesInfo (account.getId (), serverID);
        account.setRoles (roles);

        MaplePacket packet = LoginServerPacketCreator.createListCharacter (roles);
        session.write (packet.getByteArray ());
    }

    /**
     * 从数据库获取特定账号ID在指定世界服务器中创建的
     * 角色信息，包括：
     *  角色基本信息；
     *  穿戴在身上的装备。
     *
     * @param accountID   账号数据库ID
     * @param serverID    世界服务器ID
     * @return
     */
    private List <MapleRole> searchRolesInfo (int accountID, int serverID) {
        List <MapleRole> list = Business.searchRolesFromAccount (accountID, serverID);

        for (MapleRole role : list) {
            List <MapleEquipment> armed = Business.searchArmedEquipmentsFromRoleID (role.getID ());
            for (MapleEquipment eqp : armed) {
                role.wearEquipment(eqp, eqp.getIsCash ());
            }
        }

        return list;
    }

    /**
     * 这只是个测试用函数，不能调用。
     * @return
     */
    @Deprecated
    private MapleRole createTestRole () {
        MapleRole role = new MapleRole ();

        role.setRoleName ("eztest1");
        role.setID (1);
        role.setGender (true);
        role.setSkin ((byte) 1);
        role.setFace (20000);
        role.setHair (30030);
        role.setUnknown (0);
        role.setLevel ((byte) 80);
        role.setJob ((byte) 100);

        role.setStrength ((short) 300);
        role.setDexterity ((short) 20);
        role.setIntelligence ((short) 10);
        role.setLuck ((short) 70);

        role.setHP ((short) 10000);
        role.setMaxHP ((short) 12031);
        role.setMP ((short) 1000);
        role.setMaxMP ((short) 1532);

        role.setRemainingAP ((short) 0);
        role.setRemainingSP ((short) 0);

        role.setExperience (1024);

        role.setFame ((byte) 1);
        role.setMapID (102000000);
        role.setInitSpawnPoint ((byte) 0);
        return role;
    }

}
