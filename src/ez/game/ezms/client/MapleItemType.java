package ez.game.ezms.client;

/**
 * 各种物品的类别宏定义：
 * 目前分为
 * 装备、消耗、设置、其他、现金道具。
 */
public enum MapleItemType {

    /**
     * 占位用，也表示所有的物品。
     */
    ALL (0),

    /**
     * 装备
     */
    EQUIP (1),

    /**
     * 消耗
     */
    CONSUMPTION (2),

    /**
     * 设置
     */
    SETUP (3),

    /**
     * 其他
     */
    OTHERS (4),

    /**
     * 现金
     */
    CASH (5),

    /**
     * 已经装备在身上的。
     */
    ARMED (6);

    final byte type;

    MapleItemType (int type) {
        this.type = (byte) type;
    }

    public byte getType () {
        return type;
    }

}
