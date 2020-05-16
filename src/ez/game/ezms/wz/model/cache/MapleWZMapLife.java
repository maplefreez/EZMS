package ez.game.ezms.wz.model.cache;

import ez.game.ezms.wz.MapleData;
import ez.game.ezms.wz.MapleDataTool;

import java.awt.Point;

/**
 * 地图内的生物实体，包括NPC和怪物。
 * 此数据来自WZ的地图数据中定义，非
 * NPC和怪物WZ定义。
 */
public class MapleWZMapLife {
    /**
     * 两种定义在地图中的生物的类型字符串，
     * WZ文件中的写法。
     */
    public final static String monsterTypeStr = "m";
    public final static String NPCTypeStr = "n";

    /**
     * WZ数据的ID。怪物ID或者NPC的ID。
     */
    private int WZID;

    /**
     * 位置信息。
     */
    private Point location;

    /**
     * 在WZ数据文件中的name属性，可以简单理解为
     * 索引，即当前节点的name属性，一个数字，
     * 暂且分配2Byte。
     */
    private short code;

    /**
     * 是否是怪物。true是。
     */
    private boolean isMonster;

    public int getWZID() {
        return WZID;
    }

    public Point getLocation() {
        return location;
    }

    public short getCode() {
        return code;
    }

    public boolean isMonster() {
        return isMonster;
    }

    public MapleWZMapLife (MapleData data) {
        this.location = new Point ();

        // isMonster
        MapleData value = data.getChildByPath ("type");
        if (value != null) {
            String typeCh = MapleDataTool.getString(value);
            this.isMonster = typeCh.equals (monsterTypeStr);
        } else
            System.err.println ("Error occurred in Map life");

        // WZID
        value = data.getChildByPath ("id");
        if (value != null)
            this.WZID = Integer.parseInt (MapleDataTool.getString (value));
        else
            System.err.println ("Error occurred in Map life");

        // LOCATION
        value = data.getChildByPath ("x");
        if (value != null)
            this.location.x = MapleDataTool.getInt (value);
        else
            System.err.println ("Error occurred in Map life");
        value = data.getChildByPath ("y");
        if (value != null)
            this.location.y = MapleDataTool.getInt (value);
        else
            System.err.println ("Error occurred in portal");

        // CODE
        this.code = Short.parseShort (data.getName ());
    }
}
