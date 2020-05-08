package ez.game.ezms.server.packet;

import ez.game.ezms.SendPacketOptCode;
import ez.game.ezms.constance.ServerConstants;
import ez.game.ezms.server.client.MapleClient;
import ez.game.ezms.server.client.MapleEquipment;
import ez.game.ezms.server.client.MapleItem;
import ez.game.ezms.server.client.MapleRole;
import ez.game.ezms.server.packet.MaplePacket.PacketStreamLEWriter;
import ez.game.ezms.tools.RandomHelper;

import java.util.List;

public class WorldServerPacketCreator extends PacketCreator {

    /**
     * 创建握手报文。
     *
     * @return
     */
    public static MaplePacket createShakeHands (MapleClient client) {
        PacketStreamLEWriter packet = new PacketStreamLEWriter (16);
        packet.writeShort ((short) 0x0E); // 13 = MSEA, 14 = GlobalMS, 15 = EMS
        packet.writeShort (ServerConstants.MAPLE_VERSION);
        packet.writeMapleStoryASCIIString (ServerConstants.MAPLE_PATCH);
        packet.writeByteArray (client.getReceiveIV ());
        packet.writeByteArray (client.getSendIV ());
        packet.writeByte (ServerConstants.MAPLE_LOCATECODE); // 7 = MSEA, 8 = GlobalMS, 5 = Test Server

        /* 组装登录验证的握手分组，并且发回给用户。 */
        return packet.generate ();
    }

    /**
     * 玩家登录进入世界服务器。当玩家点选某个角色时
     * 由此报文响应。
     *
     * @return
     */
    public static MaplePacket enterWorldServer (MapleRole role, int WorldID) {
        PacketStreamLEWriter writer = new PacketStreamLEWriter (256);

        writer.writeByte ((byte) SendPacketOptCode.LOGIN_WORLDSERVER.getCode ());
        writer.writeInt (2);   // 频道ID。暂时我还无法获取到这个ID。
        writer.writeByte ((byte) 0);
        writer.writeByte ((byte) 1);
        writer.writeInt (RandomHelper.nextInt());
        writer.writeInt (RandomHelper.nextInt());
        writer.writeInt (RandomHelper.nextInt());
        writer.writeInt (0);
        writer.writeShort ((short) 0xFFFF);    // 二进制 111

        appendRoleData (writer, role);

        return writer.generate ();
    }

    /**
     * 此函数由enterWorldServer调用，将所有的角色信息
     * 追加到报文后。
     *
     * @param writer  包含了部分字段的报文
     * @param role    角色信息实体。
     */
    private static void appendRoleData (PacketStreamLEWriter writer, MapleRole role) {
        writeRoleInfoLoginWorld (writer, role);  // 角色状态  (116 - 25 = 91)
        writer.writeByte ((byte) 0); // 好友数相关，貌似是当前好友数量。
        writer.writeInt ((int) role.getMesos ());   // 鄙人实在不懂此处来个金币数是什么意思。

        appendKnapsackItems (writer, role);  // 背包物品信息  (325 - 121 = 204才对，目前是321, 少4byte.)
        appendSkillInfo (writer, role);     // 技能信息 (2Byte)
        appendMissionInfo (writer, role);  // 任务信息  (2Byte)
        writer.writeShort ((short) 0); // Mini Games ? 未知
        appendRingInfo (writer, role);  // 指环信息。 2Byte

        // 加入“自由市场入口”地图的编码。目前是999999999；
        // 暂时不清楚这个有什么用。
        for (int i = 0; i < 5; ++ i) {
            writer.writeInt (999999999);
        }
    }

    private static void appendKnapsackItems (PacketStreamLEWriter writer, MapleRole role) {
        /* 已经穿在身上的非现金装备。 */
        List <MapleEquipment> commonEquipments =
                role.getArmedEquipped ().getArmedEquipmentList (false);
        for (MapleEquipment eq : commonEquipments)
            eq.writeVerbosePacketEntityInto (writer);
//            writeEquipmentItemInfo (writer, eq);
        writer.writeByte ((byte) 0);

        /* 已经穿在身上的现金装备。 */
        List <MapleEquipment> cashEquipments =
                role.getArmedEquipped ().getArmedEquipmentList (true);
        for (MapleEquipment cashEq : cashEquipments)
            cashEq.writeVerbosePacketEntityInto (writer);
//            writeEquipmentItemInfo (writer, cashEq);
        writer.writeByte ((byte) 0);

        /* 背包中的装备。 */
        writer.writeByte (role.getKnapsack().getEquipmentCapacity ());
        List <MapleEquipment> equipments = role.getKnapsack ().getEquipments ();
        for (MapleEquipment equipment : equipments)
            equipment.writePacketEntityInto (writer);
//            writeEquipmentItemInfo (writer, equipment);
        writer.writeByte ((byte) 0);

        /* 背包中的消耗。 */
        writer.writeByte (role.getKnapsack().getConsumablesCapacity ());
        List <MapleItem> consumables = role.getKnapsack ().getConsumables ();
        for (MapleItem consumable : consumables)
            writeCommonItem (writer, consumable);
        writer.writeByte ((byte) 0);

        /* 背包中的设置。 */
        writer.writeByte (role.getKnapsack().getSetupCapacity ());
        List <MapleItem> sets = role.getKnapsack ().getSets ();
        for (MapleItem setup : sets)
            writeCommonItem (writer, setup);
        writer.writeByte ((byte) 0);

        /* 背包中的其他。 */
        writer.writeByte (role.getKnapsack().getOthersCapacity ());
        List <MapleItem> others = role.getKnapsack ().getOthers ();
        for (MapleItem item : others)
            writeCommonItem (writer, item);
        writer.writeByte ((byte) 0);

        /* 背包中的现金道具。 */
        writer.writeByte (role.getKnapsack().getCashItemsCapacity ());
        List <MapleEquipment> cashItems = role.getKnapsack().getCashItems();
        for (MapleEquipment cashItem : cashItems)
            writeEquipmentItemInfo (writer, cashItem);
        writer.writeByte ((byte) 0);
    }

    private static void appendSkillInfo (PacketStreamLEWriter writer, MapleRole role) {
        // TODO... 技能数据. 目前什么都没有，写入0即可。0个技能。
        writer.writeShort ((short) 0);
//        Map<Skill, SkillEntry> skills = chr.getSkills(true);
//        if (skills != null) {
//            mplew.writeShort(skills.size());
//            for (Map.Entry<Skill, SkillEntry> skill : skills.entrySet()) {
//                mplew.writeInt((skill.getKey()).getId());
//                mplew.writeInt((skill.getValue()).skillLevel);
//            }
//        } else {
//            mplew.writeShort(0);
//        }
    }

    private static void appendMissionInfo (PacketStreamLEWriter writer, MapleRole role) {
        // TODO... 任务数据，是可以开始的任务？ 还是所有的？
        // 目前什么都没有，写入0即可。0个任务
        writer.writeShort ((short) 0);
//        List<MapleQuestStatus> started = chr.getStartedQuests();
        /*
        mplew.writeShort(4);
        mplew.writeInt(100);
        mplew.writeMapleAsciiString("info"); // quest值 info不行
        mplew.writeInt(1001100);
        mplew.writeMapleAsciiString("s");
        mplew.writeInt(1000100);
        mplew.writeMapleAsciiString("w");
        mplew.writeInt(1001800);
        mplew.writeMapleAsciiString("2s");
        */
//        mplew.writeShort(started.size());
//        for (MapleQuestStatus q : started) {
//            mplew.writeInt(q.getQuest().getId());
//            mplew.writeMapleAsciiString(q.getCustomData() == null ? "" : q.getCustomData());
//        }
    }

    private static void appendRingInfo (PacketStreamLEWriter writer, MapleRole role) {
        // TODO... 指环信息。
        // 目前一个也没有，写入数量0.
        writer.writeShort ((short) 0);
//        Triple aRing = chr.getRings(true);
//        List<MapleRing> cRing = (List) aRing.getLeft();
//        mplew.writeShort(cRing.size());
//        for (MapleRing ring : cRing) { // 39个字节
//            mplew.writeInt(ring.getPartnerChrId());
//            mplew.writeAsciiString(ring.getPartnerName(), 0x13);
//            mplew.writeInt(ring.getRingId());
//            mplew.writeInt(0);
//            mplew.writeInt(ring.getPartnerRingId());
//            mplew.writeInt(0);
//        }
    }

}
