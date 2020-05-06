package ez.game.ezms.server.world;

import ez.game.ezms.conf.ServerConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 所有的世界服务器都在此处初始化和定义，
 * 目前只开放“花蘑菇”。
 */
public final class WorldServerSet {
    /**
     * 已经完成初始化的服务器数。
     */
    private static volatile int initializedCount;

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
     * 当前正在运行的或者已经完成初始化的所有服务器。
     */
    private static volatile List<WorldServer> aliveServers
            = new ArrayList <> (serverSet.length);

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

        for (; serverID < serverSet.length; ++ serverID) {
            WorldServer server = serverSet [serverID];
            try {
                server.run ();
                aliveServers.add (server);
            } catch (IOException ex) {
                // 此WorldServer失败，不影响运行下一个。
                // 但失败的必须从容器中删除。
                server.dispose ();
            }
        }

        return ! aliveServers.isEmpty ();
    }


}
