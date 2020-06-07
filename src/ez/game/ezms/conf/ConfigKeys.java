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

    /* 不可实例化。 */
    private ConfigKeys () {}
}
