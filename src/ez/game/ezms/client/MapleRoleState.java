package ez.game.ezms.client;

import ez.game.ezms.server.packet.MaplePacket;

/**
 * 角色状态枚举，主要是用于报文中的
 * 标志位。状态掩码使用32bit表示，每一位
 * 代表修改某个角色状态。
 */
public enum MapleRoleState {
    /**
     * 肤色
     */
    SKIN (0x1,1),

    /**
     * 脸型
     */
    FACE (0x2,4),

    /**
     * 头发
     */
    HAIR (0x4,4),

    /**
     * 宠物状态
     */
    PET (0x8,8),

    /**
     * 人物等级
     */
    LEVEL (0x10,1),

    /**
     * 职业
     */
    JOB (0x20,2),

    /**
     * 力量
     */
    STRENGTH (0x40,2),

    /**
     * 敏捷
     */
    DEXTERITY (0x80,2),

    /**
     * 智力
     */
    INTELLIGENCE (0x100,2),

    /**
     * 运气
     */
    LUCK (0x200,2),

    /**
     * 当前HP
     */
    HP (0x400,2),

    /**
     * HP上限.
     */
    MAXHP (0x800,2),

    /**
     * 当前MP
     */
    MP (0x1000,2),

    /**
     * MP上限
     */
    MAXMP (0x2000,2),

    /**
     * 能力点
     */
    ABILITY_POINT (0x4000,2),

    /**
     * 技能点
     */
    SKILL_POINT (0x8000,2),

    /**
     * 当前等级持有的经验值
     */
    EXP (0x10000,4),

    /**
     * 人气
     */
    FAME (0x20000,2),

    /**
     * 金币。
     */
    MESOS (0x40000,4),

    /**
     * 此枚举指示最大的掩码，不使用作指示的标志位。
     */
    MAX_FLAG (0x40000, 0);


    /**
     * 标志位。
     */
    int flag;

    /**
     * 数据允许的长度。
     */
    byte dataLen;

    /**
     * 构造
     * @param flag  标志位，标识修改此状态需要修改掩码的某位。
     * @param len   数据可以接受的长度，根据更新状态不同，报文中
     *              只能接受相应长度的值写入。如HP、MP都是用2Byte
     *              表示。
     */
    MapleRoleState (int flag, int len) {
        this.flag = flag;
        this.dataLen = (byte) len;
    }

    public int getFlag() {
        return flag;
    }

    public byte getDataLen() {
        return dataLen;
    }
}
