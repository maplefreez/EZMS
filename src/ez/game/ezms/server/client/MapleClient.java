package ez.game.ezms.server.client;


import ez.game.ezms.server.world.WorldServer;
import ez.game.ezms.server.world.WorldServerSet;
import ez.game.ezms.tools.MapleAESOFB;
import org.apache.mina.core.session.IoSession;

/**
 * 此对象是客户端的抽象，一个客户端包含一个已经登录的
 * 账号，并且能包含服务器与客户端的通信会话信息。是服务器
 * 与客户端交互的接口。
 */
public final class MapleClient {

    /* 通信用加密解密秘钥。AES */
    private MapleAESOFB sndCypher;
    private MapleAESOFB rcvCypher;

    /**
     * 通信会话。
     */
    private IoSession session;

    /**
     * 是否与LoginServer完成了握手操作。
     */
    private boolean hasShakeHand;

    /**
     * 代表客户端登录的账号。角色信息都包含于此。
     */
    public MapleAccount account;

//    /**
//     * 数据库中的账号ID，表accounts的主键。
//     */
//    private int accountDBID;

    /**
     * 账号登记的性别，早期冒险岛
     * 的性别是挂在账号上的，一个账号
     * 上所有的人物都会是同一个性别。
     * true -> 男(male)
     * false -> 女(female)
     */
//    private boolean accountGender;

//    /**
//     * 登录的角色账号，一个Session同一时间只能登录
//     * 一个账号，当然，也可以切换，届时大部分账号属性都
//     * 会更改。
//     */
//    private String account;

    /**
     * 即将要登录的角色或者当前正登录的角色。
     */
//    private MapleRole role;

    /**
     * 角色的GM等级，目前还没有完成这部分
     * 设计。
     */
//    private int accountGMLevel;

    /**
     * 当前登录到的世界服务器ID。默认0xFF
     * 表示仍然没有登录。取值从0开始。
     * 必须和worldID一起设置。
     */
    private byte worldID;

    /**
     * 当前登录到的频道ID。
     * 取值范围从0到20；
     * 必须和channelID一起设置。
     * 初始值0xff，代表没有登录进任意
     * 世界服务器下的频道。
     */
    private byte channelID;

    public MapleClient (MapleAESOFB sndCypher, MapleAESOFB rcvCypher, IoSession session) {
        this.rcvCypher = rcvCypher;
        this.sndCypher = sndCypher;
        this.session = session;
        this.hasShakeHand = false;
        this.worldID = (byte) 0xFF;
        this.channelID = (byte) 0xFF;
    }

    public byte [] getReceiveIV () {
        return this.rcvCypher.getIv();
    }

    public byte [] getSendIV () {
        return this.sndCypher.getIv ();
    }

    /**
     * 用户点选某个世界服务器的某个频道，
     * 会话状态进入，设置相应属性。
     */
    public void enterWorldAndChannel (byte serverID, byte channelID) {
        this.worldID = serverID;
        this.channelID = channelID;
    }

    public byte getWorldID () {
        return worldID;
    }

    public byte getChannelID () {
        return this.channelID;
    }

    public IoSession getSession () {
        return session;
    }

    public boolean getHasShakeHand () {
        return hasShakeHand;
    }

    public void setHasShakeHand (boolean op) {
        this.hasShakeHand = op;
    }

    public MapleAESOFB getSendCrypto() {
        return sndCypher;
    }

    public void setSndCypher(MapleAESOFB sndCypher) {
        this.sndCypher = sndCypher;
    }

    public void setRcvCypher(MapleAESOFB rcvCypher) {
        this.rcvCypher = rcvCypher;
    }

    public void setAccountEntity (MapleAccount account) {
        this.account = account;
    }

    public MapleAccount getAccountEntity () {
        return this.account;
    }
}
