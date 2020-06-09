package ez.game.ezms.server.world;

import ez.game.ezms.conf.ServerConfig;
import ez.game.ezms.conf.DefaultConf;
import ez.game.ezms.conf.WorldServerConfig;
import ez.game.ezms.exception.WorldServerException;
import ez.game.ezms.server.channel.WorldChannel;
import ez.game.ezms.client.model.MapleAccount;
import ez.game.ezms.client.MapleClient;
import ez.game.ezms.client.MapleRole;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 世界服务器实例。比如“蓝蜗牛”、“白雪人”、“章鱼怪”等。
 * 目前服务器支持的倍率计算皆在世界服务器上实现，即不应该出现
 * 同一服务器有不同倍率的经验、金币等。
 *
 * 世界服务器不是处理角色事务的实体，真正的实体是频道服务器，即
 * 每个世界服务器都会设置多个频道，每个频道可以绑定在不同的端口，
 * 甚至不同的IP。WorldServer实体只是其下众多频道服务器的一个容器。
 * 处理公有的配置信息等等。
 */
public class WorldServer {

    private WorldChannel [] channelList;

    /**
     * 登录此世界服务器的角色。角色ID -> 客户端实体。
     * 最初由LoginServer收到客户端登入世界服务器请求后加入此
     * 表中。
     */
    private ConcurrentHashMap <Integer, MapleClient> loginRoles;

    /**
     * 世界服务器的中文名字，比如“蓝蜗牛”，“红螃蟹”;
     */
    private String serverName;

    /**
     * 服务器在进程中的ID。
     * 从0开始。此配置由调用者设置。加载。
     */
    private int serverID;

    /**
     * 是否使用此服务器。若不使用此服务器，将不会初始化
     * 此服务器。 READONLY
     */
    private volatile boolean isUsage;

    /**
     * 是否正在运行。READONLY，由
     * 初始化方法设置。
     */
    private volatile boolean isRunning;

    /**
     * 是否已经完成初始化。指示单次进行初始化
     * 工作。
     */
    private volatile boolean isInitialized;

    /**
     * 世界服务器的配置参数。
     */
    private WorldServerConfig confData;

    /**
     * @param isUsage  是否启用此服务器，true将会初始化此服务器。
     * @param serverID  服务器的ID。
     * @param name      服务器名称
     */
    public WorldServer (boolean isUsage, int serverID, String name) {
        this.isUsage = isUsage;
        this.serverID = serverID;
        this.isRunning = false;
        this.isInitialized = false;
        this.serverName = name;
        this.loginRoles = new ConcurrentHashMap <> ();
    }

    /**
     * 初始化此服务器，此处的配置文件来自
     *
     * @param mainConf  配置文件加载出的数据。
     *               配置文件名是ServerID，比如
     *               1号服务器，文件是1.properties.
     * @return
     */
    public boolean initialize (ServerConfig mainConf) throws NumberFormatException, WorldServerException {
        if (isInitialized) return true;
        /* 初始化世界服务器的配置参数。 */
        this.confData = new WorldServerConfig (mainConf);
        /* 各个频道初始化。 */
        initializeChannels (confData.getChannelCount (), mainConf);
        return isInitialized = true;
    }

    /**
     * 这个不可能失败，Channel理论上只是各种
     * 结构的容器而已。
     */
    private void initializeChannels (int count, ServerConfig mainConf) {
        int channelID = 0;
        count = count <= 0 ? DefaultConf.DEF_CHANNEL_COUNT : count;
        channelList = new WorldChannel [count];

        /* 初始化各个频道服务器。 */
        while (channelID < count) {
            channelList [channelID] = new WorldChannel (channelID, mainConf);
            ++ channelID;
        }
    }

    /**
     * 运行此实例，开始监听端口，世界服务器启动成功后，可以开始接收角色
     * 在游戏中的报文。
     */
    public void run () throws IOException {
        boolean hasRunningChannel = false;
        for (WorldChannel channel : this.channelList) {
            channel.run ();
            System.out.println ("World " + this.serverName +
                    " channel" + channel.getID () + " started!");
            hasRunningChannel |= true;
        }
        this.isRunning = hasRunningChannel; // 只要有一个频道运行，则此世界服务器是运行的。
    }

    /**
     * 统计服务器所有频道的在线人数。
     *
     * @return
     */
    public long countLoginRole () {
        long count = 0L;
        for (int i = 0; i < channelList.length; ++ i)
            count += channelList [i].getLoginRoleCount ();
        return count;
    }

    /**
     * 角色从此服务器退出。服务器将此客户端实例从服务器
     * 容器中移除。两个容器中的实例都会尝试删除。
     */
    public void roleLogoutServer (MapleClient client) {
        MapleAccount account = client.getAccountEntity ();
        MapleRole role = account.getCurrentLoginRole ();

        /* 移除 */
        this.loginRoles.remove (role.getID ());
    }

    /**
     * 客户端成功与世界服务器握手后将尝试进入地图，此时世界服务器
     * 会将暂存容器中的客户端实例移入登录容器。
     * 由角色ID作为键。
     *
     * @param client
     * @param role   欲登录的对象，由客户端选中。
     */
    public boolean roleLoginServer (MapleClient client, MapleRole role) {
        MapleAccount account = client.getAccountEntity ();

        /* 移除预登录容器。 */
        WorldServerSet.removeCacheClientByID(role.getID ());
        int channelID = client.getChannelID ();
        WorldChannel channelEntity = this.getWorldChannel (channelID);
        if (channelEntity == null) return false;

        // 账号登记。
        account.loginRole (role);
        // 世界服务器登记。
        this.loginRoles.put (role.getID (), client);
        // TODO... 频道登记。

        return true;
    }

    /**
     * 根据角色ID查找角色的客户端实体。
     * 若不存在返回NULL。
     * @return
     */
    public MapleClient getLoginRoleByRoleID (int ID) {
        return this.loginRoles.get (ID);
    }

    /**
     * 根据Channel的ID得到实例。
     * ID从0开始。最多20个。
     *
     * @param id
     * @return
     */
    public WorldChannel getWorldChannel (int id) {
        if (id > channelList.length) return null;
        return channelList [id];
    }

    /**
     * 获取Channel数。
     * @return
     */
    public int getWorldChannelCount () {
        return channelList.length;
    }

    /**
     * 释放所有的资源，中断监听，关闭服务器。
     */
    public void dispose () {
        for (WorldChannel channel : channelList) {
            channel.dispose ();
        }
    }



//    public void setServerID (int serverID) {
//        this.serverID = serverID;
//    }

//    public String getIP () {
//        return this.ip;
//    }
//
//    public int getPort () {
//        return this.port;
//    }

    public int getServerID () {
        return this.serverID;
    }

    public String getServerName () {
        return this.serverName;
    }

    private boolean getIsUsage () {
        return this.isUsage;
    }

    public boolean getIsRunning () {
        return this.isRunning;
    }

    public WorldServerConfig getConfData() {
        return confData;
    }
}
