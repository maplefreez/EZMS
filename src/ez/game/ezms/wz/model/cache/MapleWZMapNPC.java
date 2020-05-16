package ez.game.ezms.wz.model.cache;

import ez.game.ezms.wz.MapleData;
import ez.game.ezms.wz.MapleDataTool;

/**
 * 地图中的NPC实体。
 */
public class MapleWZMapNPC extends MapleWZMapLife {
    /**
     * 类型字符。NPC的类型字符为n
     */
    public final static String typeStr = "n";

    public MapleWZMapNPC (MapleData data) {
        super (data);
    }

}
