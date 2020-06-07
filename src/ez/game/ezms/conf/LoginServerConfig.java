package ez.game.ezms.conf;

import ez.game.ezms.exception.LoginServerException;

/**
 * 存储自配置文件载入的所有LoginServer配置信息。
 * 上层使用此类获取配置数据，不再使用ConfigKeys中
 * 定义的键。所有的数据都加载到此类中。
 */
public class LoginServerConfig {
    /**
     * LoginServer能接受的最大在线人数。
     */
    private int userMaxOnline;

    /**
     * Server中文名。
     */
    private String serverName;

    /**
     * 是否是维护模式，
     * 此模式下只能管理员登录。
     */
    private boolean isMaintain;

    /**
     * 是否支持账号自动注册。
     */
    private boolean autoRegister;

    /**
     * 一个频道上能创建的最大角色数。
     */
    private int maxCharacters;

    /**
     * LoginServer监听的TCP端口号。
     */
    private short port;

    public LoginServerConfig (ServerConfig conf) throws LoginServerException {
        this.userMaxOnline = conf.getInt (ConfigKeys.LOGIN_MAX_ONLINE, 140);
        this.serverName = conf.getString (ConfigKeys.SERVER_NAME, "EZMS");
        this.isMaintain = conf.getBoolean (ConfigKeys.LOGIN_ISMAINTAIN, false);
        this.autoRegister = conf.getBoolean (ConfigKeys.LOGIN_AUTOREGISTER, false);
        this.port = conf.getShort (ConfigKeys.LOGIN_BIND_PORT, DefaultConf.DEF_LOGINSRV_PORT);
    }

    public int getUserMaxOnline() {
        return userMaxOnline;
    }

    public String getServerName() {
        return serverName;
    }

    public boolean isMaintain() {
        return isMaintain;
    }

    public boolean isAutoRegister() {
        return autoRegister;
    }

    public int getMaxCharacters() {
        return maxCharacters;
    }

    public short getPort() {
        return port;
    }
}
