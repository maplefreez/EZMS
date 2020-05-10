package ez.game.ezms.server.packet;

import ez.game.ezms.SendPacketOptCode;
import ez.game.ezms.constance.ServerConstants;
import ez.game.ezms.server.channel.WorldChannel;
import ez.game.ezms.client.*;
import ez.game.ezms.client.model.MapleAccount;
import ez.game.ezms.server.world.WorldServer;
import ez.game.ezms.tools.HexTool;
import ez.game.ezms.tools.RandomHelper;
import ez.game.ezms.server.packet.MaplePacket.PacketStreamLEWriter;

import java.util.List;

/**
 * 此类是LoginServer所涉及的回传报文（发送给客户端）
 * 的构造类。
 */
public final class LoginServerPacketCreator extends PacketCreator {

    /**
     * 创建握手报文。
     *
     * @return
     */
    public static MaplePacket createShakeHands () {
        final byte serverRecv[] = new byte[] {70, 114, 122, (byte) RandomHelper.nextInt(255)};
        final byte serverSend[] = new byte[] {82, 48, 120, (byte) RandomHelper.nextInt(255)};
        final byte ivRecv[] = ServerConstants.Use_Fixed_IV ? new byte [] {9, 0, 0x5, 0x5F} : serverRecv;
        final byte ivSend[] = ServerConstants.Use_Fixed_IV ? new byte [] {1, 0x5F, 4, 0x3F} : serverSend;

        PacketStreamLEWriter packet = new PacketStreamLEWriter (16);
        packet.writeShort ((short) 0x0E); // 13 = MSEA, 14 = GlobalMS, 15 = EMS
        packet.writeShort (ServerConstants.MAPLE_VERSION);
        packet.writeMapleStoryASCIIString (ServerConstants.MAPLE_PATCH);
        packet.writeByteArray (ivRecv);
        packet.writeByteArray (ivSend);
        packet.writeByte (ServerConstants.MAPLE_LOCATECODE); // 7 = MSEA, 8 = GlobalMS, 5 = Test Server

        /* 组装登录验证的握手分组，并且发回给用户。 */
        return packet.generate ();
    }

    /**
     * 若登录失败，创建此报文。
     * 失败编码如下：
     *          0 - 成功登录
     *          1 - 刷新
     *          2 - 封号
     *          3 - 屏蔽了账号登录功能或者已经被删除、终止的账号
     *          4 - 屏蔽了静态密码或密码输入错误
     *          5 - 未登录的账号
     *          6 - 当前连接不稳定。请更换其它频道或世界。为您带来不便，请谅解。
     *          7 - 正在登录中的账号
     *          8 - 当前连接不稳定。请更换其它频道或世界。为您带来不便，请谅解。 (客户端直接关闭)
     *          9 - 当前连接不稳定。请更换其它频道或世界。为您带来不便，请谅解。
     *          10 - 目前因链接邀请过多 服务器未能处理。
     * @param reason  登录失败的原因编码，将返回给客户端。
     * @return
     */
    public static MaplePacket createLoginFailed (int reason) {
        PacketStreamLEWriter packet = new PacketStreamLEWriter (7);

        packet.writeByte ((byte) SendPacketOptCode.LOGIN_RESULT.getCode ());
        packet.writeByte ((byte) reason);
        packet.writeByte ((byte) 0);
        packet.writeInt ((byte) 0);

        return packet.generate ();
    }

    /**
     * 登录成功时，向客户端回发的报文。
     *
     * @return 报文实体.
     */
    public static MaplePacket createLoginSuccess (MapleClient client) {
        MapleAccount account = client.getAccountEntity ();
        /* 鄙人认为此处不应该直接操作packet，
        * 而应该使用一个Creator之类的辅助类才好。 */
        PacketStreamLEWriter packet = new PacketStreamLEWriter (13 + 2 + account.getAccount ().length ());

        packet.writeByte ((byte) SendPacketOptCode.LOGIN_RESULT.getCode ());
        packet.writeByte ((byte) 0);  // 这是原因，0为成功。

        packet.writeInt (account.getId ());
        // 早期版本角色性别由帐号控制
        packet.writeByte (account.getGender () ? (byte) 1 : (byte) 0);
        packet.writeByte (account.getGMlevel () > 0 ? (byte) 1 : (byte) 0);
        packet.writeMapleStoryASCIIString (account.getAccount ());
        packet.writeInt (account.getId ());
        packet.writeByte ((byte) 0);

        return packet.generate ();
    }

    /**
     * 创建一个世界服务器（红螃蟹、青鳄鱼、蓝蜗牛等）的负载状态报文。
     *
     * @param server  欲创建的服务器实体。
     *
     * @return  报文实体。
     */
    public static MaplePacket createServerLoadStatusPacket(WorldServer server) {
        PacketStreamLEWriter writer = new PacketStreamLEWriter (32);

        writer.writeByte ((byte) SendPacketOptCode.LIST_SERVER.getCode());
        writer.writeByte ((byte) server.getServerID ()); // 世界服务器的编码，从0开始。直到10？
        writer.writeMapleStoryASCIIString (server.getServerName ());  // 世界服务器的中文名。
        writer.writeByte((byte) server.getWorldChannelCount ()); // 所持有频道数.

        /* 循环世界服务器中的所有频道。 */
        for (int channelID = 0; channelID < server.getWorldChannelCount(); ++ channelID) {
            WorldChannel channel = server.getWorldChannel (channelID);
            /* 此处开始循环各个服务器信息。循环变量从1开始。 */
            // 貌似是世界服务器名 + '-'+循环变量
            writer.writeMapleStoryASCIIString (server.getServerName () + "-" + (channelID + 1));
            // 负载，貌似是此频道的在线人数。
            writer.writeInt (channel.getLoginRoleCount ());
            // 世界服务器ID编码。
            writer.writeByte ((byte) server.getServerID ());
            // 频道ID
            writer.writeByte ((byte) channelID);
            writer.writeByte ((byte) 0);  // 常量
        }

        return writer.generate ();
    }

    /**
     * 创建服务器信息表结束报文。
     *
     * @return 报文实体.
     */
    public static MaplePacket createServerLoadListPacketEnd () {
        PacketStreamLEWriter writer = new PacketStreamLEWriter (2);
        writer.writeByte ((byte) SendPacketOptCode.LIST_SERVER.getCode ());
        writer.writeByte ((byte) 0xFF);
        return writer.generate ();
    }

    /**
     * 创建报文：新建角色结果报文。
     *
     * @param role  创建成功的实体。
     * @param isFail  是否失败。
     *
     * @return
     */
    public static MaplePacket createRoleCreateResult (MapleRole role, boolean isFail) {
        PacketStreamLEWriter writer = new PacketStreamLEWriter (64);

        writer.writeByte ((byte) SendPacketOptCode.ROLE_CREATED.getCode ());
        writer.writeByte (isFail ? (byte) 0 : (byte) 1);

//        {
//            /* 这里是为了调试而临时加的代码。*/
//            role.setID (1);
//        }

        writeNewRoleInfo (writer, role);

        return writer.generate ();
    }

    private static void writeNewRoleInfo (PacketStreamLEWriter writer, MapleRole role) {
        writeRoleInfo (writer, role);
    }

    /**
     * 创建世界服务器状态报文。
     * 对应接收到0x03功能码时的响应。
     *
     * @param statusCode  指示服务器状态的状态码：
     *         0x00 -> 可以正常登陆
     *         0x01 -> 人数多于此世界服务器的最大限值 × 80%.
     *         0x02 -> 人数大于等于此世界服务器的最大限值。
     *
     *        注意，80%这个策略是在下写在代码中实现的，也可以是其他的
     *             参数。总之客户端配合“服务器”完成了三个档次的服务器
     *             状态识别。
     * @return  报文实体。
     */
    public static MaplePacket createWorldServerStatus (int statusCode) {
        PacketStreamLEWriter writer = new PacketStreamLEWriter (7);
        writer.writeByte ((byte) SendPacketOptCode.SERVER_STATUS.getCode ());
        writer.writeByte ((byte) statusCode);

        /* 若在线人数多于最大值的80%，加入两个域，
        * 这两个域可能是控制人数的一些参数，比如“还能
        * 再登录多少人”等等。但鄙人并没有证实。 */
        if (statusCode == 1) {
            writer.writeInt (0);
            writer.writeByte ((byte) 0);
        }
        return writer.generate ();
    }

    /**
     * 创建列举某个世界服务器下当前账号创建的所有角色信息。
     * 对应接收到0x04功能码报文时的响应。
     *
     * @param roles
     *
     * @return  报文实体。
     */
    public static MaplePacket createListCharacter (List <MapleRole> roles) {
        PacketStreamLEWriter writer = new PacketStreamLEWriter (32);

        writer.writeByte ((byte) SendPacketOptCode.LIST_CHARACTER.getCode ());
        writer.writeByte ((byte) 0);
        writer.writeInt (0);
        writer.writeByte ((byte) roles.size ());
        /* 将各个角色加入。 */
        for (MapleRole role : roles)
            writeRoleInfo (writer, role);
        return writer.generate ();
    }





    /**
     * 创建某世界服务器的地址信息报文，
     * 报文中必须指明此世界服务器的IPv4地址和端口号。
     *
     * @param IP  IP地址
     * @param port   端口号
     * @param roleID  角色在数据库中的ID
     * @return 报文实体。
     */
    public static MaplePacket createWorldServerAddress (String IP, int port, int roleID) {
        PacketStreamLEWriter writer = new PacketStreamLEWriter (14);

        writer.writeByte ((byte) SendPacketOptCode.WORLDSERVER_ADDR.getCode ());
        writer.writeShort ((short) 0);
        writer.writeByteArray (HexTool.getIPString2ByteArray (IP));  // IP
        writer.writeShort ((short) port);
        writer.writeInt (roleID);
        writer.writeByte ((byte) 0);
        return writer.generate ();
    }

    /**
     * 创建一个 用户的新建角色名检测结果响应报文。
     *
     * @param roleName  欲新建的用户名。
     * @param isUsed  true代表此名使用过，所以该用户不能使用，反之可用。
     *
     * @return  报文实体。
     */
    public static MaplePacket createCheckRoleNameResult (String roleName, boolean isUsed) {
        PacketStreamLEWriter writer = new PacketStreamLEWriter (4 + roleName.length());
        writer.writeByte ((byte) SendPacketOptCode.CHECK_NAME_RESULT.getCode ());
        writer.writeMapleStoryASCIIString (roleName);
        writer.writeByte ((byte) (isUsed ? 1 : 0));
        return writer.generate ();
    }

    /**
     * 测试用的函数，别在正式代码中调用。
     *
     * @param client
     * @return
     */
    @Deprecated
    public static MaplePacket createTestPacket (MapleClient client) {
        MaplePacket.PacketStreamLEWriter packetStreamLEWriter = new MaplePacket.PacketStreamLEWriter(12);
        return packetStreamLEWriter.generate ();
    }

}
