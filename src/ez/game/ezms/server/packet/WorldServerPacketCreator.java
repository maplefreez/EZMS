package ez.game.ezms.server.packet;

import ez.game.ezms.SendPacketOptCode;
import ez.game.ezms.client.MapleClient;
import ez.game.ezms.client.MapleRoleState;
import ez.game.ezms.client.model.MapleAccount;
import ez.game.ezms.constance.ServerConstants;
import ez.game.ezms.client.model.MapleEquipment;
import ez.game.ezms.client.model.MapleItem;
import ez.game.ezms.client.MapleRole;
import ez.game.ezms.server.packet.MaplePacket.PacketStreamLEWriter;
import ez.game.ezms.tools.RandomHelper;
import ez.game.ezms.wz.model.cache.MapleWZMap;
import ez.game.ezms.wz.model.cache.MapleWZMapPortal;

import java.util.List;

public class WorldServerPacketCreator extends PacketCreator {

    /**
     * 创建握手报文。
     * @param recvIV  解密秘钥
     * @param sndIV   加密秘钥
     *
     * @return 报文
     */
    public static MaplePacket createShakeHands (byte [] recvIV, byte [] sndIV) {
        PacketStreamLEWriter packet = new PacketStreamLEWriter (16);
        packet.writeShort ((short) 0x0E); // 13 = MSEA, 14 = GlobalMS, 15 = EMS
        packet.writeShort (ServerConstants.MAPLE_VERSION);
        packet.writeMapleStoryASCIIString (ServerConstants.MAPLE_PATCH);
        packet.writeByteArray (recvIV);
        packet.writeByteArray (sndIV);
//        packet.writeByteArray (client.getReceiveIV ());
//        packet.writeByteArray (client.getSendIV ());
        packet.writeByte (ServerConstants.MAPLE_LOCATECODE); // 7 = MSEA, 8 = GlobalMS, 5 = Test Server

        /* 组装登录验证的握手分组，并且发回给用户。 */
        return packet.generate ();
    }

    /**
     * 玩家登录进入世界服务器。当玩家点选某个角色时
     * 由此报文响应。
     *
     * @param client   主要提供当前角色登录的世界ID和频道ID。
     * @param role  角色的各种属性及背包信息等。
     *
     * @return
     */
    public static MaplePacket enterWorldServer (MapleClient client, MapleRole role) {
        PacketStreamLEWriter writer = new PacketStreamLEWriter (256);

        writer.writeByte ((byte) SendPacketOptCode.LOGIN_WORLDSERVER.getCode ());
        writer.writeInt (client.getChannelID ());   // 频道ID。暂时我还无法获取到这个ID。
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
     * 发送消息到客户端，创建消息报文。
     *
     * 开发者ez注：type参数应该做成枚举。
     *
     * @param client  客户端实体。
     * @param toBeSent  欲发送的消息字符串。非空
     * @param type   类型，目前知道的类型如下：
     *               0 -- 通知
     *               1 -- 弹窗
     *               2 -- Mega, 蓝白色广播
     *               3 -- smega
     *               4 -- 头顶说话？
     *               5 -- 聊天栏红色字。
     * @return 报文实体。
     */
    public static MaplePacket sendMessage (MapleClient client, String toBeSent, int type) {
        PacketStreamLEWriter writer = new PacketStreamLEWriter (4 + toBeSent.length ());
        writer.writeByte ((byte) SendPacketOptCode.MESSAGE_SEND.getCode ());
        writer.writeByte ((byte) type);
//        if (type == 4) {
//            mplew.write(1); // 1-开启 0-关闭
//        }
        writer.writeMapleStoryASCIIString (toBeSent);
//        switch (type) {
//            case 3:
//                mplew.write(channel - 1);
//                mplew.write(megaEar ? 1 : 0);
//                break;
//            default:break;
//        }
        return writer.generate ();
    }

    /**
     * 更新角色当前背包持有的冒险币数。
     *
     * @param value  冒险币增加数量。
     * @return  报文实体。
     */
    public static MaplePacket updateRoleMeso (int value) {
        PacketStreamLEWriter writer = new PacketStreamLEWriter (10);

        writer.writeByte ((byte) SendPacketOptCode.UPDATE_ROLE_STATUS.getCode ()); // 25 = 0x19
        writer.writeByte ((byte) (0)); // 常量。
        writer.writeInt (MapleRoleState.MESOS.getFlag ()); // flag，标志位。
        writer.writeInt (value);  // 增加的金币数值。
        return writer.generate ();
    }

    /**
     * 角色进入地图。
     *
     * @param role       角色实体。
     * @param fromPortal 从此传送口实例进入地图。
     * @param fromMap    从此地图离开。
     * @return
     */
    public static MaplePacket enterMap (MapleClient client, MapleRole role, MapleWZMap toMap) {
        // v062
//        mplew.writeShort(SendPacketOpcode.WARP_TO_MAP.getValue()); // 0x49
//        mplew.writeInt(chr.getClient().getChannel() - 1);
//        mplew.writeShort(0x2);
//        mplew.writeShort(0);
//        mplew.writeInt(to.getId());
//        mplew.write(spawnPoint);
//        mplew.writeShort(chr.getHp()); // hp (???)
//        mplew.write(0);
//        long questMask = 0x1ffffffffffffffL;
//        mplew.writeLong(questMask);

        // v027
        PacketStreamLEWriter writer = new PacketStreamLEWriter (23);
        writer.writeByte ((byte) SendPacketOptCode.WARP_MAP.getCode ());
        writer.writeInt(client.getChannelID ());
        writer.writeByte ((byte) toMap.getPortalList ().length);
        writer.writeByte ((byte) 0);
        writer.writeInt (toMap.getWZID ());
        writer.writeByte ((byte) 0); // 暂时如此，初始start point怎么选择？
        writer.writeShort (role.getHP ());

//        writer.writeByte ((byte) SendPacketOptCode.LOGIN_WORLDSERVER.getCode ());
//        writer.writeInt (2);   // 频道ID。暂时我还无法获取到这个ID。
//        writer.writeByte ((byte) 0);
//        writer.writeByte ((byte) 1);
//        writer.writeInt (RandomHelper.nextInt());
//        writer.writeInt (RandomHelper.nextInt());
//        writer.writeInt (RandomHelper.nextInt());
//        writer.writeInt (0);
//        writer.writeShort ((short) 0xFFFF);    // 二进制 111

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
