package ez.game.ezms.wz.model.cache;

import java.awt.Point;

/**
 * WZ数据定义的地图中的传送口数据。
 */
public class MapleWZMapPortal {
    /**
     * 落脚点类型传送口的toMapID字段。
     * 是个常熟。
     */
    public final static int STARTPOINT_2_MAPID = 999999999;

    /**
     * 位置坐标。
     */
    private Point location;

    /**
     * 传送口名称，在映射表中有用。
     * 此字段也是在WZ中定义的。
     */
    private String name;

    /**
     * 传送口类型：
     * 0 -> start point，即用户在进入地图时的
     *      初始站立点（位置）。
     * 1 -> 本图快捷传送或者隐藏地图传送口。一般不可
     *      见。
     * 2 -> 正常的传送口，可见，光柱类型。
     */
    private byte type;

    /**
     * 进入此传送门能到达的地图的WZID。
     */
    private int toMapID;

    /**
     * 进入此传送门后能到达的地图的落脚点
     * 名称。即name字段值。
     */
    private String toMapPortalName;
}
