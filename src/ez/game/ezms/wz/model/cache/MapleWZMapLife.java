package ez.game.ezms.wz.model.cache;

import java.awt.Point;

/**
 * 地图内的生物实体，包括NPC和怪物。
 * 此数据来自WZ的地图数据中定义，而非
 * NPC和怪物WZ定义。
 */
public class MapleWZMapLife {
    /**
     * WZ数据的ID。怪物ID或者NPC的ID。
     */
    private int WZID;

    /**
     * 位置信息。
     */
    private Point location;
}
