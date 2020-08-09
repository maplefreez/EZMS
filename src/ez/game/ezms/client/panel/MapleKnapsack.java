package ez.game.ezms.client.panel;

import ez.game.ezms.client.MapleItemType;
import ez.game.ezms.client.model.MapleEquipment;
import ez.game.ezms.client.model.MapleItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 角色背包实例
 */
public class MapleKnapsack {
    /**
     * 栏位已满的代码，用于标识每个
     * min*EmptySpaceIndex;
     */
    private static final byte CATEGORY_FULL = -1;
    /**
     * 装备，此集合不包含已经穿在身上的装备。
     */
//    private List <MapleEquipment> equipments;
    private MapleEquipment [] equipments;
    /**
     * 装备的背包空间（最大值）
     */
    private byte equipmentCapacity;
    /**
     * 最小的背包空缺位置索引（装备）。
     */
    private byte minEquipmentEmptySpaceIndex;

    /**
     * 其他
     */
    private MapleItem [] others;
    /**
     * 其他的背包空间（最大值）
     */
    private byte othersCapacity;
    /**
     * 最小的背包空缺位置索引（其他）。
     * 代表满
     */
    private byte minOtherEmptySpaceIndex;

    /**
     * 消耗
     */
    private MapleItem [] consumables;
    /**
     * 消耗的背包空间（最大值）
     * -1代表满
     */
    private byte consumablesCapacity;
    /**
     * 最小的背包空缺位置索引（消耗）。
     */
    private byte minConsumablesEmptySpaceIndex;

    /**
     * 宠物或者其他现金道具。
     */
    private MapleEquipment [] cashItems;
    /**
     * 宠物或者其他现金道具的背包空间（最大值）
     */
    private byte cashItemsCapacity;
    /**
     * 最小的背包空缺位置索引（现金）。
     */
    private byte minCashEmptySpaceIndex;

    /**
     * 设置
     */
    private MapleItem [] sets;
    /**
     * 设置的背包空间（最大值）
     */
    private byte setupCapacity;
    /**
     * 最小的背包空缺位置索引（设置）。
     */
    private byte minSetupsEmptySpaceIndex;

    public MapleKnapsack () {
        /* 暂且用初始值。最大值 */
        setupCapacity = equipmentCapacity =
                cashItemsCapacity = consumablesCapacity =
                        othersCapacity = 96;
        this.minCashEmptySpaceIndex = this.minConsumablesEmptySpaceIndex
                = minEquipmentEmptySpaceIndex = this.minOtherEmptySpaceIndex
                = minSetupsEmptySpaceIndex = 0;

        /* 容器 */
        equipments = new MapleEquipment [equipmentCapacity];
        others = new MapleItem [othersCapacity];
        consumables = new MapleItem [consumablesCapacity];
        cashItems = new MapleEquipment [consumablesCapacity];
        sets = new MapleItem [setupCapacity];
    }

    /**
     * 扔出物品。
     * @param category
     * @param pos
     * @return
     */
    public MapleItem throwOutItem (MapleItemType category, int pos) {
        MapleItem ret = null;
        switch (category) {
            case EQUIP :
                if (pos >= equipments.length) break;
                ret = this.equipments [pos];
                this.equipments [pos] = null;
                this.minEquipmentEmptySpaceIndex = (byte) pos;
                break;
            case CONSUMPTION :
                if (pos >= consumables.length) break;
                ret = this.consumables [pos];
                this.consumables [pos] = null;
                this.minConsumablesEmptySpaceIndex = (byte) pos;
                break;
            case OTHERS :
                if (pos >= others.length) break;
                ret = this.others [pos];
                this.others [pos] = null;
                this.minOtherEmptySpaceIndex = (byte) pos;
                break;
            case SETUP :
                if (pos >= sets.length) break;
                ret = this.sets [pos];
                this.sets [pos] = null;
                this.minSetupsEmptySpaceIndex = (byte) pos;
                break;
            case CASH :
                // 目前不能丢弃现金装备。
                break;
            default :
                break;
        }
        return ret;
    }

    /**
     * 捡起物品到背包.
     * @param item 物品实体。
     * @return  返回捡取的物品。若背包已满，不捡如。
     */
    public MapleItem pickUpOneItem (MapleItem item) {
        switch (item.getType ()) {
            case EQUIP :
                if (! isEquipmentSpaceFull ()) return null;
                checkAndModifyMinEquipmentIndex ();
                break;
            case CONSUMPTION :
                if (! isConsumptionSpaceFull ()) return null;
                checkAndModifyMinConsumableIndex ();
                break;
            case OTHERS :
                if (! isOtherSpaceFull ()) return null;
                checkAndModifyMinOtherIndex ();
                break;
            case SETUP :
                if (! isSetupSpaceFull ()) return null;
                checkAndModifyMinSetupIndex ();
                break;
            case CASH :
                if (! isCashSpaceFull ()) return null;
                checkAndModifyMinCashIndex ();
                break;
            default :
                break;
        }
        return item;
    }

    /**
     * 维护装备栏的最小空缺位索引变量逻辑。
     */
    private void checkAndModifyMinEquipmentIndex () {
        int idx = 0;
        if (null == this.equipments [this.minEquipmentEmptySpaceIndex])
            return;
        // 查找为空的位置。
        while (this.equipments [idx ++] != null) ;
        if (idx >= this.equipmentCapacity)
            this.minEquipmentEmptySpaceIndex = CATEGORY_FULL;
        else
            this.minEquipmentEmptySpaceIndex = (byte) (idx - 1);
    }

    /**
     * 维护消耗栏的最小空缺位索引变量逻辑。
     */
    private void checkAndModifyMinConsumableIndex () {
        int idx = 0;
        if (null == this.consumables [this.minConsumablesEmptySpaceIndex])
            return;
        // 查找为空的位置。
        while (this.consumables [idx ++] != null) ;
        if (idx >= this.consumables.length)
            this.minConsumablesEmptySpaceIndex = CATEGORY_FULL;
        else
            this.minConsumablesEmptySpaceIndex = (byte) (idx - 1);
    }

    /**
     * 维护其他栏的最小空缺位索引变量逻辑。
     */
    private void checkAndModifyMinOtherIndex () {
        int idx = 0;
        if (null == this.others [this.minOtherEmptySpaceIndex])
            return;
        // 查找为空的位置。
        while (this.others [idx ++] != null) ;
        if (idx >= this.others.length)
            this.minOtherEmptySpaceIndex = CATEGORY_FULL;
        else
            this.minOtherEmptySpaceIndex = (byte) (idx - 1);
    }

    /**
     * 维护设置栏的最小空缺位索引变量逻辑。
     */
    private void checkAndModifyMinSetupIndex () {
        int idx = 0;
        if (null == this.sets [this.minSetupsEmptySpaceIndex])
            return;
        // 查找为空的位置。
        while (this.sets [idx ++] != null) ;
        if (idx >= this.sets.length)
            this.minSetupsEmptySpaceIndex = CATEGORY_FULL;
        else
            this.minSetupsEmptySpaceIndex = (byte) (idx - 1);
    }

    /**
     * 维护现金栏的最小空缺位索引变量逻辑。
     */
    private void checkAndModifyMinCashIndex () {
        int idx = 0;
        if (null == this.cashItems [this.minCashEmptySpaceIndex])
            return;
        // 查找为空的位置。
        while (this.cashItems [idx ++] != null) ;
        if (idx >= this.cashItems.length)
            this.minCashEmptySpaceIndex = CATEGORY_FULL;
        else
            this.minCashEmptySpaceIndex = (byte) (idx - 1);
    }

    /**
     * 判断是否装备栏满了。
     * @return 满则返回true。
     */
    public boolean isEquipmentSpaceFull () {
        return this.minEquipmentEmptySpaceIndex == CATEGORY_FULL;
    }

    /**
     * 判断是否消耗栏满了。
     * @return 满则返回true。
     */
    public boolean isConsumptionSpaceFull () {
        return this.minConsumablesEmptySpaceIndex == CATEGORY_FULL;
    }

    /**
     * 判断是否其他栏满了。
     * @return 满则返回true。
     */
    public boolean isOtherSpaceFull () {
        return this.minOtherEmptySpaceIndex == CATEGORY_FULL;
    }

    /**
     * 判断是否设置栏满了。
     * @return 满则返回true。
     */
    public boolean isSetupSpaceFull () {
        return this.minSetupsEmptySpaceIndex == CATEGORY_FULL;
    }

    /**
     * 判断是否现金栏满了。
     * @return 满则返回true。
     */
    public boolean isCashSpaceFull () {
        return this.minCashEmptySpaceIndex == CATEGORY_FULL;
    }

    public List<MapleEquipment> getEquipments() {
        return Arrays.asList (equipments);
    }

    public List <MapleItem> getOthers () { return Arrays.asList (others); }

    public List <MapleItem> getConsumables () { return Arrays.asList (consumables); }

    public List <MapleEquipment> getCashItems () { return Arrays.asList (cashItems); }

    public List <MapleItem> getSets () { return Arrays.asList (sets); }

    public byte getEquipmentCapacity() {
        return equipmentCapacity;
    }

    public void setEquipmentCapacity(byte equipmentCapacity) {
        this.equipmentCapacity = equipmentCapacity;
    }

    public byte getOthersCapacity() {
        return othersCapacity;
    }

    public void setOthersCapacity(byte othersCapacity) {
        this.othersCapacity = othersCapacity;
    }

    public byte getConsumablesCapacity() {
        return consumablesCapacity;
    }

    public void setConsumablesCapacity(byte consumablesCapacity) {
        this.consumablesCapacity = consumablesCapacity;
    }

    public byte getCashItemsCapacity() {
        return cashItemsCapacity;
    }

    public void setCashItemsCapacity(byte cashItemsCapacity) {
        this.cashItemsCapacity = cashItemsCapacity;
    }

    public byte getSetupCapacity() {
        return setupCapacity;
    }

    public void setSetupCapacity(byte setupCapacity) {
        this.setupCapacity = setupCapacity;
    }
}
