package ez.game.ezms.server.client;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色背包实例
 */
public class MapleKnapsack {
    /**
     * 装备，此集合不包含已经穿在身上的装备。
     */
    private List <MapleEquipment> equipments;
    /**
     * 装备的背包空间（最大值）
     */
    private byte equipmentCapacity;

    /**
     * 其他
     */
    private List <MapleItem> others;
    /**
     * 其他的背包空间（最大值）
     */
    private byte othersCapacity;

    /**
     * 消耗
     */
    private List <MapleItem> consumables;
    /**
     * 消耗的背包空间（最大值）
     */
    private byte consumablesCapacity;

    /**
     * 宠物或者其他现金道具。
     */
    private List <MapleEquipment> cashItems;
    /**
     * 宠物或者其他现金道具的背包空间（最大值）
     */
    private byte cashItemsCapacity;

    /**
     * 设置
     */
    private List <MapleItem> sets;
    /**
     * 设置的背包空间（最大值）
     */
    private byte setupCapacity;

    public MapleKnapsack () {
        equipments = new ArrayList<>();
        others = new ArrayList<>();
        consumables = new ArrayList<>();
        cashItems = new ArrayList<>();
        sets = new ArrayList<>();

        /* 暂且用初始值。 */
        setupCapacity = equipmentCapacity =
                cashItemsCapacity = consumablesCapacity =
                        othersCapacity = 100;
    }

    public List<MapleEquipment> getEquipments() {
        return equipments;
    }

    public List <MapleItem> getOthers () { return others; }

    public List <MapleItem> getConsumables () { return consumables; }

    public List <MapleEquipment> getCashItems () { return cashItems; }

    public List <MapleItem> getSets () { return sets; }

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
