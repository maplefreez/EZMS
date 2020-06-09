/**
 * Author : ez
 * Date : 2019/10/23
 * Description : 服务器启动类实现。
 */

package ez.game.ezms;

import ez.game.ezms.conf.ServerConfig;
import ez.game.ezms.exception.LoginServerException;
import ez.game.ezms.server.login.LoginServer;
import ez.game.ezms.server.world.WorldServerSet;
import ez.game.ezms.sql.DBDataSource;
import ez.game.ezms.wz.MapleDataProviderFactory;
import ez.game.ezms.wz.MapleWZDataCache;

/**
 * 服务器启动类。
 * 1、加载数据（wz、MySQL）
 * 2、初始化（配置，运行实例）
 * 3、启动进程（Login，World，Channel）
 */
public class Start {
    /* LoginServer 服务器 */
    private static LoginServer loginServer;

    /* 主配置文件，文件名为“srvconfig.conf”。 */
    private static ServerConfig mainConfig;

    /**
     * 所有初始化工作的开始接口。
     * 此方法要么成功，要么失败（直接宕机），不抛出异常。
     * 初始化顺序：
     * 1、部分全局变量初始化。
     * 2、主配置文件加载。
     * 3、数据源加载（MySQL数据库）
     * 4、登录服务器（LoginServer）
     * 5、世界服务器（WorldServer）
     * 6、频道服务器组（ChannelServer）
     *
     * 注：此方法只能调用一次。
     */
    private static void initialize () {
        /* 退出线程。 */
        Runtime.getRuntime().addShutdownHook (
                new Thread (new Shutdown ()));

        loadMainConfigFile ();
        initializeDataSource ();
        initializeLoginServer ();
        initializeWorldServers ();
    }

    /**
     * 初始化数据源，最初此处只有MySQL的数据源。
     * 后续可能会有一些全局的数据结构，都是与游戏
     * 数据缓存相关。
     */
    private static void initializeDataSource () {
        /* MySQL数据库连接池。 */
        if (null == DBDataSource.getOrInitializeDBDataSource ()) {
            System.out.println ("Cannot initalize database pool, Process will exit.");
            System.exit (0);
        }

        /* WZ 数据 */
        try {
            MapleDataProviderFactory.initialize (mainConfig);
        } catch (Exception ex) {
            /* 在失败的情况下应该直接结束，
            * 服务器运行不正常。 */
            ex.printStackTrace ();
            System.err.println ("Cannot load WZ Data. Process will exit.");
            System.exit (0);
        }

        /* 从XML加载WZ数据。 */
        MapleWZDataCache.loadAllItemsWZData();
        MapleWZDataCache.loadMapWZData ();
    }

    /**
     * LoginServer初始化
     */
    private static void initializeLoginServer () {
        try {
            loginServer = LoginServer.initializeSingleton (mainConfig);
        } catch (LoginServerException ex) {
            /* 异常直接退出。 */
            ex.printStackTrace ();
            System.err.println ("Cannot initialize LoginServer, process will exit.");
            System.exit (0);
        }
    }

    /**
     * 初始化所有的ChannelServer。
     */
    private static void initializeWorldServers () {
        // 此处必须配置。即必须提供文件。目前放置在worldconf.d下
        String confDir = mainConfig.getString ("server.world.config.dir", null);
        if (null == confDir) {
            // 服务器不能没有世界服务器，连配置文件都没有，进程还是结束吧！
            System.err.println ("No \"server.world.config.dir\" config section. Process exits.");
            loginServer.dispose ();
            System.exit (0);
        }

        WorldServerSet.initializeServers (confDir);

        /* 注意，整个服务器不能没有ChannelServer，至少必须有一个。
         * 否则进程直接结束。 */
        if (0 >= WorldServerSet.getInitializedCount ()) {
            System.out.println ("Not one of a WorldServer has been " +
                    "successfully initialized. Process exits.");
            loginServer.dispose ();
            System.exit (0);
        }
    }


    /**
     * 加载主配置文件。
     */
    private static void loadMainConfigFile () {
        mainConfig = ServerConfig.openConfigFile ("srvconfig.conf");
        if (null == mainConfig) {
            /* 直接退出。 */
            System.err.println ("Failed in load main configuration file, process exit...");
            System.exit(0);
        }
    }

    /**
     * 所有实例启动工作的开始接口。
     * 此方法要么成功，要么失败（直接宕机），不抛出异常。
     */
    private static void runEntity () {
        /* LoginServer启动。 */
        {
            try {
                loginServer.run ();
            } catch (LoginServerException ex) {
                // 日志输出。退出进程。
                ex.printStackTrace ();
                System.exit (0);
            }
        }

        /* WorldServer启动。 */
        {
            if (! WorldServerSet.run ()) {
                // 日志输出，此时应该着手进程退出了。
                System.err.println ("Failed in running all the WorldServer," +
                        " process exit...");
                System.exit (0);
            }
        }

    }

    /**
     * 入口函数。
     * @param args  参数暂不使用。
     */
    public static void main (String [] args) {
        initialize ();
        runEntity ();

        /* TODO ...
         * 在下希望此处是个命令行解析器，主
         * 进程在完成所有的任务后，在此处
         * 等待命令调试， 也可以直接作为World
         * 服务器实体。 只是假设而已。 */
    }

    /**
     * 退出线程
     */
    static class Shutdown implements Runnable {

        @Override
        public void run () {

        }

        public Shutdown () { }
    }


}

