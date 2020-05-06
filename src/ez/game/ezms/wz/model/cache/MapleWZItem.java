package ez.game.ezms.wz.model.cache;

/**
 * WZ数据实例，表示物品。
 * 任意物品都必须从这个类继承。
 */
public class MapleWZItem {
    /**
     * WZ的ID编码。
     */
    private long WZID;

    public long getWZID() {
        return WZID;
    }

    public MapleWZItem (long WZID) {
        this.WZID = WZID;
    }
}
