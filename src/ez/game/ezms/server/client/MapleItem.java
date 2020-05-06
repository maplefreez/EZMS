package ez.game.ezms.server.client;

/**
 * 冒险岛中的各种物品，包括
 * 装备、其他、消耗、设置、现金。
 * 各类物品都要继承自此。
 */
public class MapleItem {

    /**
     * 存储在数据库中的ID，这个
     * ID是由数据库生成的，与
     * WZ中定义的物品代码无关。
     */
    private long DBID;

    /**
     * WZID，即物品代码。
     */
    private long WZID;

    /**
     * 物品的类别。
     * 装备、其他、设置、消耗、现金或者
     * 已装备（穿在身上的装备）
     */
    private MapleItemType type;

    /**
     * 数量
     */
    private int count;

    public MapleItem () {}

    public MapleItem (long WZID) {
        this.WZID = WZID;
    }

    public MapleItem (long DBID, long WZID) {
        this.WZID = WZID;
        this.DBID = DBID;
    }

    public MapleItemType getType () {
        return this.type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public void setType (MapleItemType type) {
        this.type = type;
    }

    public long getDBID() {
        return DBID;
    }

    public void setDBID(long DBID) {
        this.DBID = DBID;
    }

    public long getWZID() {
        return WZID;
    }

    public void setWZID(long WZID) {
        this.WZID = WZID;
    }

}
