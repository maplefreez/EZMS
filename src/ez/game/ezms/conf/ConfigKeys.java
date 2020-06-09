/**
 * Author : ez
 * Created Date : 2020/6/7
 */

package ez.game.ezms.conf;

/**
 * 此处定义配置文件中可以配置的所有配置项
 * 的键。通过键可以找到配置值。此类只定义
 * 字符串常量，用于获取配置信息。不能实例化
 * 此类。
 */
public final class ConfigKeys {

    /* ------------- 公共配置节 ---------------*/
    /* 服务器名称。 */
    public static final String SERVER_NAME = "server.name";


    /* ------------- LoginServer 配置键 ------------- */
    /* 能允许的客户端最大链接数。 */
    public static final String LOGIN_MAX_ONLINE = "server.login.maxonline";

    /* 是否当前正在进行维护。 */
    public static final String LOGIN_ISMAINTAIN = "server.login.maintain";

    /* 若登录时账号不存在，是否自动注册账号。 */
    public static final String LOGIN_AUTOREGISTER = "server.login.autoreg";

    /* 登入服务器监听的TCP端口。 */
    public static final String LOGIN_BIND_PORT = "server.login.port";

    /* ------------- WorldServer 配置键 ------------- */
    public static final String WORLD_MESO_RATE = "server.world.mesorate";

    public static final String WORLD_EXP_RATE = "server.world.exprate";

    public static final String WORLD_DROP_RATE = "server.world.droprate";

    /* 角色单个能力值属性的最大值。 */
    public static final String WORLD_STATE_MAX = "server.world.statmax";

    /* 世界服务器下的频道总数。 */
    public static final String WORLD_CHANNEL_COUNT = "server.world.channelnum";

    /* 此世界服务器允许登录的最大用户数。目前给予各个频道服务器
    * 的名额策略是平分。 */
    public static final String WORLD_MAX_LOGIN = "server.world.maxlogin";

    /* ------- ChannelServer相关 ----------- */
    /* 频道服务器所监听的IP， 这里只定义键前缀，不同频道将频道ID追加在
    * 此键后，如频道ID为5（第六频道），则键为"server.world.interface.5" */
    public static final String WORLD_CHANNEL_IP_PREFIX = "server.channel.interface.";

    /* 频道服务器所监听的端口号， 这里只定义键前缀，不同频道将频道ID追加在
     * 此键后，如频道ID为4（第五频道），则键为"server.world.interface.4" */
    public static final String WORLD_CHANNEL_PORT_PREFIX = "server.channel.port.";

    /* 不可实例化。 */
    private ConfigKeys () {}
}
