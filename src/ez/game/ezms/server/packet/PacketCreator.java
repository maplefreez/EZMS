package ez.game.ezms.server.packet;


import ez.game.ezms.server.client.MapleEquipment;
import ez.game.ezms.server.client.MapleItem;
import ez.game.ezms.server.client.MapleRole;
import ez.game.ezms.server.client.MapleRoleEquipped;
import ez.game.ezms.server.packet.MaplePacket.PacketStreamLEWriter;

/**
 * 一些创建报文的公共方法写入此处。
 */
public class PacketCreator {

    /**
     * 此函数用于角色登录世界服务器时使用。
     * 与writeRoleInfo()头部一模一样，除了没有写入装备信息，所以
     * 鄙人认为两者可以合并成同一个函数。
     *
     * @param writer
     * @param role
     */
    public static void writeRoleInfoLoginWorld (MaplePacket.PacketStreamLEWriter writer, MapleRole role) {
        writer.writeInt(role.getID ());
        /* 鄙人不甚清楚，这里为什么一定要固定长度字符串。不足长度部分都要添加0. */
        writer.writeFixedLengthASCIIString (role.getRoleName (), 0x13);
        writer.writeByte (role.getGenderCode ());
        writer.writeByte (role.getSkin());
        writer.writeInt (role.getFace());
        writer.writeInt (role.getHair());
        writer.writeLong (0);  // // Pet SN
        writer.writeByte (role.getLevel ());
        writer.writeShort (role.getJob());
        writer.writeShort (role.getStrength ());
        writer.writeShort (role.getDexterity ());
        writer.writeShort (role.getIntelligence ());
        writer.writeShort (role.getLuck ());
        writer.writeShort (role.getHP ());
        writer.writeShort (role.getMaxHP ());
        writer.writeShort (role.getMP ());
        writer.writeShort (role.getMaxMP ());
        writer.writeShort (role.getRemainingAP ());
        writer.writeShort (role.getRemainingSP ());
        writer.writeInt (role.getExperience ());
        writer.writeShort (role.getFame());
        writer.writeInt (role.getMapID ());
        writer.writeByte (role.getInitSpawnPoint ());
        writer.writeReversedLong (System.currentTimeMillis());
        writer.writeInt (0);
        writer.writeInt (0);
    }

    /**
     * 此方法在createListCharacter()中被调用，主要
     * 是在报文中追加角色数据。包括基础属性、HP、经验、
     * 外观、能力点等。
     * 另外还有穿戴在身上的装备。
     * 这个函数用于LoginServer。
     */
    public static void writeRoleInfo (MaplePacket.PacketStreamLEWriter writer, MapleRole role) {
        writer.writeInt(role.getID ());
        /* 鄙人不甚清楚，这里为什么一定要固定长度字符串。不足长度部分都要添加0. */
        writer.writeFixedLengthASCIIString (role.getRoleName (), 0x13);
        writer.writeByte (role.getGenderCode ());  // 男=0； 女= 1
        writer.writeByte (role.getSkin());
        writer.writeInt (role.getFace());
        writer.writeInt (role.getHair());
        writer.writeLong (0);  // // Pet SN
        writer.writeByte (role.getLevel ());
        writer.writeShort (role.getJob());
        writer.writeShort (role.getStrength ());
        writer.writeShort (role.getDexterity ());
        writer.writeShort (role.getIntelligence ());
        writer.writeShort (role.getLuck ());
        writer.writeShort (role.getHP ());
        writer.writeShort (role.getMaxHP ());
        writer.writeShort (role.getMP ());
        writer.writeShort (role.getMaxMP ());
        writer.writeShort (role.getRemainingAP ());
        writer.writeShort (role.getRemainingSP ());
        writer.writeInt (role.getExperience ());
        writer.writeShort (role.getFame());
        writer.writeInt (role.getMapID ());
        writer.writeByte (role.getInitSpawnPoint ());
        writer.writeReversedLong (System.currentTimeMillis());
        writer.writeInt(0);
        writer.writeInt(0);

        // 加入穿戴在身上的非现金装备，不用考虑顺序。在下只做了四个装备的信息发送。最好做成循环
        writeEquippedInfo (MapleRoleEquipped.BLOUSE_POSITION_CODE, writer, role.getBlouse(false));
        writeEquippedInfo (MapleRoleEquipped.SHOES_POSITION_CODE, writer, role.getShoes (false));
        writeEquippedInfo (MapleRoleEquipped.TROUSERS_POSITION_CODE, writer, role.getTrousers (false));
        writeEquippedInfo (MapleRoleEquipped.WEAPON_POSITION_CODE, writer, role.getWeapon (false));
        writer.writeByte ((byte) 0);
        // 加入穿戴在身上的现金装备，不用考虑顺序。在下只做了四个装备的信息发送。最好做成循环。
        writeEquippedInfo (MapleRoleEquipped.BLOUSE_POSITION_CODE, writer, role.getBlouse(true));
        writeEquippedInfo (MapleRoleEquipped.SHOES_POSITION_CODE, writer, role.getShoes (true));
        writeEquippedInfo (MapleRoleEquipped.TROUSERS_POSITION_CODE, writer, role.getTrousers (true));
        writeEquippedInfo (MapleRoleEquipped.WEAPON_POSITION_CODE, writer, role.getWeapon (true));
        writer.writeByte ((byte) 0);
    }

    /**
     * 写入一个穿在身上的装备信息。若
     * 传入的装备是null，则什么也不做。
     *
     * 这个函数用于LoginServer，只传入最简单的装备信息。
     *
     * @param posCode   装备所在的位置编码，定义在MapleRoleEquipped.
     * @param writer    创建报文的空间
     * @param equipment  装备实体。
     */
    public static void writeEquippedInfo (int posCode, MaplePacket.PacketStreamLEWriter writer, MapleEquipment equipment) {
        if (equipment != null) {
            writer.writeByte((byte) posCode);
            writer.writeInt((int) equipment.getWZID());
        }
    }

    /**
     * 写入装备类物品的具体信息，包括各项属性值什么的。
     */
    public static void writeEquipmentItemInfo (PacketStreamLEWriter writer, MapleEquipment equipment) {
        writer.writeByte (equipment.getPosCode ());

        // 头信息。
        writeCommonEquipmentItemInfoHeader (writer, equipment);
        writer.writeByte (equipment.getRemainingenhance ());
        writer.writeByte ((byte) equipment.getLevel ()); // 装备等级，貌似是需要几级才能戴在身上？
        writer.writeShort(equipment.getExtrastr ());
        writer.writeShort(equipment.getExtradex ());
        writer.writeShort(equipment.getExtraint ());
        writer.writeShort(equipment.getExtraluk ());
        writer.writeShort(equipment.getExtraHP ());
        writer.writeShort(equipment.getExtraMP ());

        writer.writeShort(equipment.getExtraattack ());// 物理攻击
        writer.writeShort(equipment.getExtraMagic ());// 魔法攻击
        writer.writeShort(equipment.getExtraphydef ());// 物理防御
        writer.writeShort(equipment.getExtramgcdef ());// 魔法防御

        writer.writeShort (equipment.getHitPercentage ());   // 不知道是什么。可能是命中率
        writer.writeShort (equipment.getExtradodge ()); // 回避率
        writer.writeShort ((short) 0);  // TODO... equipment.getHands()不知道是什么。
        writer.writeShort (equipment.getExtraspeed ());  // 速度
        writer.writeShort (equipment.getExtrajump ());   // 跳跃
        writer.writeMapleStoryASCIIString (equipment.getOwnerName ());  // 制作者信息。
    }

    private static void writeCommonEquipmentItemInfoHeader (PacketStreamLEWriter writer, MapleEquipment equipment) {
        writer.writeInt ((int) equipment.getWZID());
        writer.writeByte ((byte) 0);

//        writer.writeInt ((int) equipment.getWZID ());
//        if (item.getUniqueId() > 0) {
//            mplew.write(1);
//            mplew.writeLong(item.getUniqueId());
//        } else {
//            mplew.write(0);
//        }

        /* 装备耐久度。目前置位最大值。  */
        long time = getItemElapseTime (-1);
        writer.writeLong (time);
    }

    /**
     * 写入基本物品信息，非装备类。
     * @param writer
     * @param equipment
     */
    public static void writeCommonItem (PacketStreamLEWriter writer, MapleItem equipment) {
        // TODO...
    }

    public static long MAX_TIME = 150842304000000000L;
    public static long ZERO_TIME = 94354848000000000L;
    public static long PERMANENT = 150841440000000000L;

    private static long getItemElapseTime (long timestamp) {
        if (timestamp == -1L) {
            return MAX_TIME;
        }
        if (timestamp == -2L) {
            return ZERO_TIME;
        }
        if (timestamp == -3L) {
            return PERMANENT;
        }
//        return DateUtil.getFileTimestamp(timestamp);
        return -1L; // 暂且先这样处理。
    }

}
