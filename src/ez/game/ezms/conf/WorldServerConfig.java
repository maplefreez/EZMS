package ez.game.ezms.conf;

import ez.game.ezms.exception.WorldServerException;

/**
 * 存储自配置文件载入的所有WorldServer配置信息。
 * 上层使用此类获取配置数据，不再使用ConfigKeys中
 * 定义的键。所有的数据都加载到此类中。
 */
public class WorldServerConfig {

    /**
     * 冒险币掉落倍率，默认1.
     * 为保证不越界，不能超过10000。
     */
    private int mesoRate;

    /**
     * 经验倍率，默认1.
     * 为保证不越界，不能超过1000。
     */
    private int expRate;

    /**
     * 掉落倍率，默认1。
     * 为保证不越界，不能超过100.
     */
    private int dropRate;

    /**
     * 每个状态值的上限。（力量，运气，敏捷，智力。）
     */
    private int statLimit;

    /**
     * 频道服务器数量。
     */
    private int channelCount;

    /**
     * 登录角色的人数上线。
     */
    private int loginMaxCount;

    public WorldServerConfig (ServerConfig conf) throws WorldServerException {
        mesoRate = conf.getInt (ConfigKeys.WORLD_MESO_RATE, 1);
        expRate = conf.getInt (ConfigKeys.WORLD_EXP_RATE,1);
        dropRate = conf.getInt (ConfigKeys.WORLD_DROP_RATE,1);
        statLimit = conf.getInt (ConfigKeys.WORLD_STATE_MAX, 999);
        channelCount = conf.getInt (ConfigKeys.WORLD_CHANNEL_COUNT, DefaultConf.DEF_CHANNEL_COUNT);
        loginMaxCount = conf.getInt (ConfigKeys.WORLD_MAX_LOGIN, 32);
    }

    public int getLoginMaxCount() {
        return loginMaxCount;
    }

    public int getMesoRate() {
        return mesoRate;
    }

    public int getExpRate() {
        return expRate;
    }

    public int getDropRate() {
        return dropRate;
    }

    public int getStatLimit() {
        return statLimit;
    }

    public int getChannelCount() {
        return channelCount;
    }

}
