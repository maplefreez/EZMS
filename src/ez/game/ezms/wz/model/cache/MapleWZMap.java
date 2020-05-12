package ez.game.ezms.wz.model.cache;

import java.util.List;

/**
 * WZ数据地图实体。一个实体代表一个地图，
 * 缓存在内存中用。每个地图内存中只能留
 * 一份此实例。此实例最好做成只读的。
 */
public class MapleWZMap {

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
     * 是否存在小型隐藏地图。
     */
    private boolean hasHideMiniMap;

    /**
     * 地图中的怪物列表。若是村落或者没有怪物
     * 的地图，不需要初始化此容器。
     */
    private List <MapleWZMapMonster> monsterList;

    /**
     * 地图中的NPC列表。若地图是没有NPC的地图，
     * 不需要初始化此容器。
     */
    private List <MapleWZMapNPC> npcList;

    /**
     * 地图中的传送口列表。地图中不可能没有传送口
     * 定义，至少要有落脚点（type=1）。
     */
    private List <MapleWZMapPortal> portalList;

}
