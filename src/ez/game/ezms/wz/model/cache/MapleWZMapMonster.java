package ez.game.ezms.wz.model.cache;

import ez.game.ezms.wz.MapleData;

/**
 * WZ地图中定义的怪物信息。
 */
public class MapleWZMapMonster extends MapleWZMapLife {
    /**
     * 生物类型字段，怪物为m。
     */
    public final static String typeStr = "m";

    public MapleWZMapMonster (MapleData data) {
        super (data);
    }
}
