package ez.game.ezms.server.login.handle.func;

import ez.game.ezms.constance.ServerConstants;
import ez.game.ezms.server.client.MapleClient;
import ez.game.ezms.server.client.MapleEquipment;
import ez.game.ezms.server.client.MapleRole;
import ez.game.ezms.server.client.MapleRoleEquipped;
//import ez.game.ezms.server.login.LoginServer;
import ez.game.ezms.server.packet.LoginServerPacketCreator;
import ez.game.ezms.server.packet.MaplePacket;
import ez.game.ezms.server.packet.OptionFunc;
import ez.game.ezms.server.packet.PacketStreamLEReader;
import ez.game.ezms.sql.Business;
import org.apache.mina.core.session.IoSession;

/**
 * 用于响应用户端的创建角色报文（接收功能码0x0B）。
 */
public class CreateRole implements OptionFunc {

    @Override
    public void process (byte [] body, IoSession session) {
        MapleClient client = null;
        try {
            client = (MapleClient) session.getAttribute (ServerConstants.CLIENT_ENTITY_KEY);
        } catch (Exception ex) {
            ex.printStackTrace ();
            return;
        }

        MapleRole role = fillSubmitData (body);
        vinus (role);
        // 其他重要
        role.setAccountID (client.getAccountDBID ());
        role.setGender (client.getAccountGender ());
        role.setWorldID (client.getWorldID ());

        boolean res = insertRoleInfo2DB (role);
        pushResultPacket2Client (res, role, session);
    }

    /**
     * 将客户端提交的数据整理到Role实例。
     */
    private MapleRole fillSubmitData (byte [] body) {
        PacketStreamLEReader reader = new PacketStreamLEReader (body);

        MapleRole role = MapleRole.createDefaultRole ();
        // 外观
        role.setRoleName (reader.readMapleASCIIString ());
        role.setFace (reader.readInt ());
        role.setHair (reader.readInt ());

        MapleEquipment blouse = new MapleEquipment (reader.readInt ());
        blouse.setPosCode (MapleRoleEquipped.BLOUSE_POSITION_CODE);
        blouse.setIsCash (false);

        MapleEquipment trousers = new MapleEquipment (reader.readInt ());
        trousers.setPosCode (MapleRoleEquipped.TROUSERS_POSITION_CODE);
        trousers.setIsCash (false);

        MapleEquipment shoes = new MapleEquipment (reader.readInt ());
        shoes.setPosCode (MapleRoleEquipped.SHOES_POSITION_CODE);
        shoes.setIsCash (false);

        MapleEquipment weapon = new MapleEquipment (reader.readInt ());
        weapon.setPosCode (MapleRoleEquipped.WEAPON_POSITION_CODE);
        weapon.setIsCash (false);

        // 捡起上衣，裤子，鞋子，武器。
        role.pickUpEquipment (blouse);
        role.pickUpEquipment (trousers);
        role.pickUpEquipment (shoes);
        role.pickUpEquipment (weapon);
        // 并穿上。
        role.setBlouse (blouse, false);
        role.setTrousers (trousers, false);
        role.setShoes (shoes, false);
        role.setWeapon (weapon, false);

        // 四项能力值。
        role.setStrength (reader.readByte());
        role.setDexterity (reader.readByte ());
        role.setIntelligence (reader.readByte ());
        role.setLuck (reader.readByte ());

        return role;
    }

    private boolean insertRoleInfo2DB (MapleRole role) {
        boolean flag = true;
        flag = (Business.insertRole2DB (role));

        /* 将初始装备存入。 */
        if (flag)
            Business.insertRoleEquipments2DB (role);
        return flag;
    }

    /**
     * 将角色创建结果返回给客户端。
     *
     * @param flag
     * @param role
     * @param session
     */
    private void pushResultPacket2Client (boolean flag, MapleRole role, IoSession session) {
        MaplePacket packet = LoginServerPacketCreator.createRoleCreateResult (role, flag);
        session.write (packet.getByteArray ());
    }

    /**
     * 加入其他福利数据，比如给予初始金币，
     * 初始道具或者初始点券等等。
     */
    private void vinus (MapleRole role) {
        // 目前什么都不做。
    }

}
