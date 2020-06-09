package ez.game.ezms.conf;

public class ChannelConfig {

    /**
     * 监听的IPv4接口。即IP地址。
     */
    private String IP;

    /**
     * 绑定的TCP端口号。
     */
    private short port;

    /**
     * 加载配置文件的数据。
     * @param ID     频道的ID，从0开始。目前一个世界服务器最多可以有20个频道。
     * @param conf  配置数据。
     */
    public ChannelConfig (int ID, ServerConfig conf) {
        this.IP = conf.getString (ConfigKeys.WORLD_CHANNEL_IP_PREFIX + ID, "127.0.0.1");
        this.port = conf.getShort (ConfigKeys.WORLD_CHANNEL_PORT_PREFIX + ID,
                DefaultConf.DEF_CHANNEL_PORT_START);
    }

    public String getIP () {
        return this.IP;
    }

    public short getPort () {
        return this.port;
    }
}
