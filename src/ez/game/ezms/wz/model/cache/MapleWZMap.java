package ez.game.ezms.wz.model.cache;

import ez.game.ezms.wz.MapleData;
import ez.game.ezms.wz.MapleDataTool;
import ez.game.ezms.wz.MapleDataType;

import java.util.List;

/**
 * WZ数据地图实体。一个实体代表一个地图，
 * 缓存在内存中用。每个地图内存中只能留
 * 一份此实例。此实例最好做成只读的。
 */
public class MapleWZMap {

    /**
     * 地图的WZ编码，ID。
     */
    private int WZID;

    /**
     * 强制回退到某个地图的ID。
     */
    private int forcedReturnMapID;

    /**
     * 使用回城卷轴时回到的地图ID。
     */
    private int returnMapID;

    /**
     * 是否是安全区，即村落。
     */
    private boolean isTown;

    /**
     * 怪物刷新率。
     */
    private float monsterRate;

    /**
     * 是否存在小型隐藏地图。
     */
    private boolean hasHideMiniMap;

    /**
     * 地图中的怪物列表。若是村落或者没有怪物
     * 的地图，不需要初始化此容器。
     */
    private MapleWZMapMonster [] monsterList;

    /**
     * 地图中的NPC列表。若地图是没有NPC的地图，
     * 不需要初始化此容器。
     */
    private MapleWZMapNPC [] npcList;

    /**
     * 地图中的传送口列表。地图中不可能没有传送口
     * 定义，至少要有落脚点（type=1）。
     */
    private MapleWZMapPortal [] portalList;

    /**
     * 构造函数
     * @param wzID  此地图实例的WZ编码，ID
     * @param data  此地图实例的xml数据实体。
     */
    public MapleWZMap (int wzID, MapleData data) {
        this.WZID = (wzID);
        MapleData value = data.getChildByPath ("info/town");
        if (value != null)
            this.isTown = MapleDataTool.getInt (value) > 0;

        value = data.getChildByPath ("info/mobRate");
        if (value != null)
            this.monsterRate = MapleDataTool.getFloat (value);

        value = data.getChildByPath ("info/returnMap");
        if (value != null)
            this.returnMapID = MapleDataTool.getInt (value);

        value = data.getChildByPath ("info/hideMinimap");
        if (value != null)
            this.hasHideMiniMap = MapleDataTool.getInt (value) > 0;

        value = data.getChildByPath ("info/forcedReturn");
        if (value != null)
            this.forcedReturnMapID = MapleDataTool.getInt (value);

        loadPortalData (data);
        loadNPC (data);
        loadMonster (data);
    }

    private int loadPortalData (MapleData data) {
        MapleData entities = data.getChildByPath ("portal");

        if (entities != null) {
            List<MapleData> entityList = entities.getChildren();
            int count = entityList.size ();
            if (count > 0) {
                int idx = 0;
                if (this.portalList == null)
                    this.portalList = new MapleWZMapPortal[count];
                for (MapleData entity : entityList) {
                    MapleWZMapPortal portal = new MapleWZMapPortal (data);
                    this.portalList [idx ++] = portal;
                }
            } else return 0;
        } else return 0;

        return this.portalList.length;
    }

    private int loadNPC (MapleData data) {
        // TODO...
        return 0;
    }

    private int loadMonster (MapleData data) {
        // TODO...
        return 0;
    }

    public int getForcedReturnMapID() {
        return forcedReturnMapID;
    }

    public int getReturnMapID() {
        return returnMapID;
    }

    public boolean isTown() {
        return isTown;
    }

    public boolean isHasHideMiniMap () {
        return hasHideMiniMap;
    }

    public MapleWZMapMonster[] getMonsterList() {
        return monsterList;
    }

    public MapleWZMapNPC[] getNpcList() {
        return npcList;
    }

    public MapleWZMapPortal[] getPortalList() {
        return portalList;
    }

}
