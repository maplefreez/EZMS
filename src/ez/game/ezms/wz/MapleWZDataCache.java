package ez.game.ezms.wz;

import ez.game.ezms.wz.model.cache.MapleWZEquipment;
import ez.game.ezms.wz.model.cache.MapleWZItem;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * WZ数据的缓存，尽可能都加载。
 */
public final class MapleWZDataCache {
    /**
     * 所有的items信息全存储于此。
     * items包括：设置、其他、消耗及部分现金道具（非装备类）。
     */
    private static HashMap <Long, MapleWZItem> items;

    /**
     * 所有的装备信息，包括现金装备及普通装备。
     */
    private static HashMap <Long, MapleWZEquipment> equipments;
    /**
     * 装备类的数据访问接口。
     */
    private static MapleDataProvider equipmentDataInterface;

    /**
     * 初始化数据接口。若已经完成初始化，则不再初始化。
     * 注意： 必须在先调用了MapleDataProviderFactory的initialize()后才能调用此函数。
     *     各数据接口的数据路径如下
     *     1、装备类： ./<server.wzpath>/Character.wz/<各类装备目录>/
     *
     * @return
     */
    public static void initializeDataInterfaces () {
        if (equipmentDataInterface == null) equipmentDataInterface
                = MapleDataProviderFactory.getDataProvider ("Character.wz/");
    }

    /**
     * 从WZ文件（XML格式）加载所有的数据到
     * 内存。内存会消耗得非常快。
     *
     */
    public static void loadAllWZData () {
        long loadEntityCount = 0;
        /* 目前只加载如下目录的装备信息。 */
        final String [] subDirToBeLoad = {
                "Weapon", "Shoes", "Shield", "PetEquip", "Ring",
                "Pants", "Longcoat", "Glove", "Coat", "Cape",
                "Cap", "Accessory"
        };
        if (equipments != null) return;
        equipments = new LinkedHashMap <> (97);

        for (String subDir : subDirToBeLoad) {
            System.out.print ("Start loading " + subDir + " Data from WZ XML...");
            loadEntityCount = loadEquipmentsDataFromSubDir (subDir);
            System.out.println (loadEntityCount + " finished!");
        }
    }

    /**
     * 从WZ文件（XML格式）加载所有的装备数据。
     * 若已经加载过，则不再重复加载。
     * 此方法可能会非常耗时。
     *
     * @return  加载的装备实例数。
     */
    private static long loadEquipmentsDataFromSubDir (String subDirName) {
        // Character.wz/<subDirName>/目录实体。
        MapleDataDirectoryEntry entry  = equipmentDataInterface.getFile ().getSubDirByName (subDirName);

        // 没有查到WZ数据，报错并通过。在正式运行时不能触发这条。
        if (entry == null) {
            System.err.println ("Error in loading " + subDirName + " data from WZ, missing WZ XML file?");
            return 0;
        }

        // 目录下的文件列表。
        List<MapleDataFileEntry> files = entry.getFiles ();
        for (MapleDataFileEntry file : files) {
            // 路径为    ./<server.wzpath>/Character.wz/<subDirName>/$file.getName ()，
            // 此处得到DOM对象。
            long wzID = getWZIDByFileName(file.getName ());
            MapleData xmlDom = equipmentDataInterface.getData (entry.getName ()
                    + "/" + file.getName ());
            MapleWZEquipment equipmentEntity = new MapleWZEquipment (wzID, xmlDom);
            equipments.put (wzID, equipmentEntity);
        }

        return equipments.size ();
    }

    private static long getWZIDByFileName (String fileName) {
        String [] splitPair = fileName.split ("\\.");
        if (splitPair.length >= 1)
            return Long.parseLong (splitPair [0]);
        return 0L;  // 暂时不知道有什么更好.
    }

}
