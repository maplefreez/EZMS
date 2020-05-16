package ez.game.ezms.wz.model.cache;

import ez.game.ezms.wz.MapleData;
import ez.game.ezms.wz.MapleDataTool;

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

    public MapleWZMapPortal (MapleData data) {
        MapleData value = data.getChildByPath ("pn");
        if (value != null)
            this.name = MapleDataTool.getString (value);
        else
            System.err.println ("Error occurred in portal");

        value = data.getChildByPath ("pt");
        if (value != null)
            this.type = (byte) MapleDataTool.getInt (value);
        else
            System.err.println ("Error occurred in portal");

        this.location = new Point ();
        value = data.getChildByPath ("x");
        if (value != null)
            this.location.x = MapleDataTool.getInt (value);
        else
            System.err.println ("Error occurred in portal");
        value = data.getChildByPath ("y");
        if (value != null)
            this.location.y = MapleDataTool.getInt (value);
        else
            System.err.println ("Error occurred in portal");

        value = data.getChildByPath ("tm");
        if (value != null)
            this.toMapID = MapleDataTool.getInt (value);
        else
            System.err.println ("Error occurred in portal");

        value = data.getChildByPath ("tn");
        if (value != null)
            this.toMapPortalName = MapleDataTool.getString (value);
        else
            System.err.println ("Error occurred in portal");
    }

    public Point getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public byte getType() {
        return type;
    }

    public int getToMapID() {
        return toMapID;
    }

    public String getToMapPortalName() {
        return toMapPortalName;
    }
}
