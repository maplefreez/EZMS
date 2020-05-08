package ez.game.ezms.server.client;


import ez.game.ezms.server.world.WorldServer;
import ez.game.ezms.server.world.WorldServerSet;
import ez.game.ezms.tools.MapleAESOFB;
import org.apache.mina.core.session.IoSession;

/**
 * 此对象创建于每个对话开始时，并且结束于每个对话结束。
 * 会话中的所有相关信息将会存在于此。
 * 目前主要是用作一个包装类，包装mina的session对象。
 */
public class MapleClient {

    private MapleAESOFB sndCypher;
    private MapleAESOFB rcvCypher;
    private IoSession session;

    /**
     * 是否与LoginServer完成了握手操作。
     */
    private boolean hasShakeHand;


    /**
     * 数据库中的账号ID，表accounts的主键。
     */
    private int accountDBID;

    /**
     * 账号登记的性别，早期冒险岛
     * 的性别是挂在账号上的，一个账号
     * 上所有的人物都会是同一个性别。
     * true -> 男(male)
     * false -> 女(female)
     */
    private boolean accountGender;

    /**
     * 登录的角色账号，一个Session同一时间只能登录
     * 一个账号，当然，也可以切换，届时大部分账号属性都
     * 会更改。
     */
    private String account;

    /**
     * 即将要登录的角色或者当前正登录的角色。
     */
    private MapleRole role;

    /**
     * 角色的GM等级，目前还没有完成这部分
     * 设计。
     */
    private int accountGMLevel;

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
        WorldServer server = WorldServerSet.getWorldServer (worldID);

        /* 目前只是暂时进入。 */
        if (server != null) server.beforeRoleLogin (this);
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

    public int getAccountDBID() {
        return accountDBID;
    }

    public void setAccountDBID(int accountDBID) {
        this.accountDBID = accountDBID;
    }

    public boolean getAccountGender() {
        return accountGender;
    }

    public void setAccountGender(boolean accountGender) {
        this.accountGender = accountGender;
    }

    public int getAccountGMLevel() {
        return accountGMLevel;
    }

    public void setAccountGMLevel(int accountGMLevel) {
        this.accountGMLevel = accountGMLevel;
    }


    public MapleRole getRole() {
        return role;
    }

    public void setRole(MapleRole role) {
        this.role = role;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
