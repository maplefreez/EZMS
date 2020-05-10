package ez.game.ezms.server.world;

import ez.game.ezms.conf.ServerConfig;
import ez.game.ezms.server.client.MapleAccount;
import ez.game.ezms.server.client.MapleClient;
import ez.game.ezms.server.client.MapleRole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 所有的世界服务器都在此处初始化和定义，
 * 目前只开放“花蘑菇”。
 */
public final class WorldServerSet {
    /**
     * 已经完成初始化的服务器数。
     */
    private static volatile int initializedCount;

    /** 角色ID -> 客户端实体。
     */
    private static ConcurrentHashMap <Integer, MapleClient> rolesWillLogin;
    static {
        rolesWillLogin = new ConcurrentHashMap <> ();
    }

    /**
     * 在登录世界以前，先将客户端实体存入此世界服务器。
     * 此时先存入暂存容器。先由账号ID作为键。
     */
    public static void beforeRoleLogin (MapleClient client) {
        MapleAccount account = client.getAccountEntity ();
        rolesWillLogin.put (account.getCurrentLoginRole ().getID (), client);
    }

    /**
     * 角色登录到指定的世界服务器。根据角色ID得到客户端实体。调用此函数将
     * 容器中的客户端实体移除。
     */
    public static MapleClient removeCacheClientByID (int roleID) {
        MapleClient client = rolesWillLogin.get (roleID);

        /* 移除 */
        rolesWillLogin.remove (roleID);
        return client;
    }

    /**
     * 存储所有服务器的实例。不要出现null占位。
     * ID与名称是一一对应的。
     */
    public static final WorldServer [] serverSet = {
            new WorldServer (true, 0, "蓝蜗牛"),
            new WorldServer (true, 1, "蘑菇仔"),
//            new WorldServer (true, 2, "绿水灵"),
//            new WorldServer (true, 3, "漂漂猪"),
//            new WorldServer (true, 4, "小青蛇"),
//            new WorldServer (true, 5, "红螃蟹"),
//            new WorldServer (true, 6, "大海龟"),
//            new WorldServer (true, 7, "章鱼怪"),
//            new WorldServer (true, 8, "顽皮猴"),
//            new WorldServer (true, 9, "星精灵"),
//            new WorldServer (true, 0xA, "胖企鹅"),
//            new WorldServer (true, 0xB, "白雪人"),
//            new WorldServer (true, 0xC, "石头人"),
//            new WorldServer (true, 0xD, "紫色猫"),
//            new WorldServer (true, 0xE, "大灰狼"),
//            new WorldServer (true, 0xF, "小白兔"),
//            new WorldServer (true, 0x10, "喷火龙"),
//            new WorldServer (true, 0x11, "火野猪"),
//            new WorldServer (true, 0x12, "青鳄鱼"),
//            new WorldServer (true, 0x13, "花蘑菇"),
    };

    /**
     * 根据世界服务器的ID得到服务器实体。若不存在或者
     * 服务器没有成功运行，则返回NULL。
     *
     * @param ID   从0开始。
     * @return  世界服务器实体
     */
    public static WorldServer getWorldServer (int ID) {
        if (ID >= serverSet.length || ID < 0) return null;

        WorldServer server = serverSet [ID];
        return server.getIsRunning () ? server : null;
    }

    /**
     * 别从外部创建此实例。用不着。
     */
    private WorldServerSet () {}

    public static int getInitializedCount () {
        return initializedCount;
    }

    /**
     * 初始化所有的服务器，目前根据代码中
     * 的硬编码（isUsage）配置启用的服务器，
     * 不启用的服务器将不进行初始化工作。
     */
    public static void initializeServers (String confDir) {
        int serverID = 0; // 从0开始的ID。
        for (; serverID < serverSet.length; ++ serverID) {
            if (initializeOne (serverID, confDir)) {
                WorldServer finishedOne = WorldServerSet.serverSet [serverID];
                System.out.println ("WorldServer "+ finishedOne.getServerName ()
                        + " finished in initializing.");
                ++ initializedCount;
            }
        }
    }

    private static boolean initializeOne (int ID, String confDir) {
        final String confFilePostfix = ".conf";
        String fileName = confDir + "/" + (ID) + confFilePostfix;
        ServerConfig conf = ServerConfig.openConfigFile (fileName);

        try {
            serverSet [ID].initialize (conf);
        } catch (Exception ex) {
            ex.printStackTrace ();
            return false;
        }
        return true;
    }

    /**
     * 尝试运行所有的服务器。
     * 若所有服务器都启动失败，返回false。
     * 若存在服务器启动成功，返回true。
     */
    public static boolean run () {
        int serverID = 0;
        int count = 0;

        for (; serverID < serverSet.length; ++ serverID) {
            WorldServer server = serverSet [serverID];
            try {
                server.run ();
//                aliveServers.add (server);
                count ++;
            } catch (IOException ex) {
                // 此WorldServer失败，不影响运行下一个。
                // 但失败的必须从容器中删除。
                server.dispose ();
            }
        }

        return count > 0;
    }


}
