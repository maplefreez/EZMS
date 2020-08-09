package ez.game.ezms.client;

import ez.game.ezms.client.model.MapleEquipment;
import ez.game.ezms.client.model.MapleItem;
import ez.game.ezms.client.panel.MapleKnapsack;
import ez.game.ezms.client.panel.MapleRoleBasicInfo;
import ez.game.ezms.client.panel.MapleRoleEquipped;
import ez.game.ezms.server.packet.MaplePacket;
import ez.game.ezms.sql.Business;
import ez.game.ezms.wz.model.cache.MapleWZMap;

import java.util.List;

/**
 * 冒险岛角色实体。
 * 一个账号可以在任意一个世界服务器（蓝蜗牛、章鱼怪等）
 * 创建多个角色。
 *
 * 关于内存与数据库的同步问题：
 *   目前如下几个字段不会经常更改，更改的场景大都是在使用
 * 现金道具或者GM的命令更改，修改频度低些，所以以下属性在
 * 修改时使用全写回策略，即修改内存后马上修改MySQL：
 *   ID、roleName，gender，skin， face， hair， level， job
 *
 */
public class MapleRole implements Cloneable {
    /**
     * 数据库ID。
     */
    private int ID;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 性别：true 男， false 女
     */
    private boolean gender;

    /**
     * 未知字段
     */
    private long unknown;


    /**
     * 账号ID
     */
    private int accountID;

    /**
     * 世界ID.
     */
    private byte worldID;


    /**
     * 角色能力面板的各项属性。
     */
    private MapleRoleBasicInfo basicInfo;

    /**
     * 穿戴装备，E键面板。
     */
    private MapleRoleEquipped armed;

    /**
     * 角色背包。
     */
    private MapleKnapsack knapsack;

    /**
     * 不清楚这个是什么，只知道是时间戳64bits。
     */
    private long date;

//    /**
//     * 用于记录此角色的属性是否有改动，此标志位只用于标志：
//     *
//     */
//    private volatile boolean isInfoWrited;


    public MapleRole () {
        this.basicInfo = new MapleRoleBasicInfo ();
        this.armed = new MapleRoleEquipped ();
        this.knapsack = new MapleKnapsack ();
    }

    public MapleRole (MapleRoleBasicInfo basicInfo) {
        this.basicInfo = basicInfo;
        this.armed = new MapleRoleEquipped();
        this.knapsack = new MapleKnapsack ();
    }

    /**
     * 从数据库加载所有的角色信息。
     * 此函数必须指定角色的数据库ID。
     *
     */
    public static MapleRole loadAllRoleDataFromDB (int roleID) {
        MapleRole role = Business.searchRoleByRoleID (roleID);

        // 获取所有物品，包括装备，其他，设置，消耗，现金。
        List <MapleItem> items = Business.searchAllItemsByRoleID (roleID);
        role.pickUpItems (items);

        List <MapleEquipment> armed = Business.searchArmedEquipmentsFromRoleID (roleID);
        role.wearEquipments (armed);

        return role;
    }

    /**
     * 将此角色的数据写入数据库，此函数并非强制所有数据
     * 写入，仅仅写入修改部分。
     * 注：非异步函数。
     */
    public void tryWriteBack2DB () {
        // 基础信息
        this.basicInfo.tryWriteBack2DB (this);

        // TODO...
        // 装备窗（E），穿戴的装备。
        // 背包信息.
    }

    /**
     * 捡起多个物品。
     *
     * @param items 物品集合。
     */
    public void pickUpItems (List <MapleItem> items) {
        // 放入背包中。
        for (MapleItem item : items)
            pickUpOneItem (item);
    }

    /**
     * 捡起一个装备。
     * @param item
     */
    public MapleItem pickUpOneItem (MapleItem item) {
        return this.knapsack.pickUpOneItem (item);
    }

    /**
     * 扔出某个物品，
     * @param category  物品的类别。包括四项，由MapleItemType定义。
     * @param pos  物品所在背包中的位置索引，从0开始。超出最大索引则什么都不做，返回null。
     *
     * @return  返回被丢弃的物品实体。
     */
    public MapleItem throwOutItem (MapleItemType category, int pos) {
        return this.knapsack.throwOutItem (category, pos);
    }

    /**
     * 从数据库加载五个种类的背包物品信息。
     */
    public void loadAllItemsFromDB () {

    }

    /**
     * 从数据库加载角色的装备信息（仅穿戴在身上的）。
     */
    public void loadArmedEquipmentFromDB () {

    }

    /**
     * 从数据库加载角色的任务信息。
     */
    public void loadQuestDataFromDB () {

    }

    /**
     * 从数据库加载角色的技能信息。
     */
    public void loadSkillDataFromDB () {

    }

    /**
     * 获取本实例的报文实体。用于传送此实例的报文，是
     * 实例的一种序列化。
     * 此报文实体只是包含角色基本信息及基本能力属性（MapleRoleAbility）的序列化
     *
     * @return 字节序列
     */
    public void getBasicPacketEntity (MaplePacket.PacketStreamLEWriter writer) {
        writer.writeInt (this.ID);
        /* 鄙人不甚清楚，这里为什么一定要固定长度字符串。不足长度部分都要添加0. */
        writer.writeFixedLengthASCIIString (this.roleName, 0x13);
        writer.writeByte (this.getGenderCode ());  // 男=0； 女= 1
//        writer.writeByte (this.skin);
//        writer.writeInt (face);
//        writer.writeInt (hair);
//        writer.writeLong (0L);  // Pet SN

        // 能力信息。
        this.basicInfo.getPacketEntity (writer);
    }

    public byte [] getBasicPacketEntity () {
        MaplePacket.PacketStreamLEWriter writer = new MaplePacket.PacketStreamLEWriter (64);
        this.getBasicPacketEntity (writer);
        return writer.generate ().getByteArray ();
    }


    /**
     * 创建一个默认的角色，有很多属性都会进行初始化。
     * 成为默认值。
     *
     * @return 新建的实例。
     */
    public static MapleRole createDefaultRole () {
        MapleRoleBasicInfo ability = MapleRoleBasicInfo.createDefaultAbilityEntity ();
        MapleRole role = new MapleRole (ability);

        role.basicInfo.setMapID (0); // 设置初始地图。
        role.basicInfo.setMesos (600); // 至少给点初始金币。

        role.basicInfo.setJob ((byte) 0);  // 设置初始职业，新手为0.
        role.basicInfo.setLevel ((byte) 1);

        return role;
    }

    /**
     * 穿上装备，根据装备的位置信息进行装备。
     * @param equipment  装备实体，必须设置了posCode，即类别代码（亦称位置代码）
     * @param isCash     是否是现金装备
     *
     * @return
     */
    public void wearEquipment (MapleEquipment equipment, boolean isCash) {
        this.armed.setEquipmentByPosCode (equipment, isCash);
    }

    /**
     * 将多个装备穿在身上。
     */
    public void wearEquipments (List <MapleEquipment> equipments) {
        for (MapleEquipment equipment : equipments)
            this.wearEquipment (equipment, equipment.getIsCash ());
    }

    /**
     * 角色进入地图。
     */
    public void enterMap (MapleWZMap fromMap, MapleWZMap toMap) {
        this.basicInfo.setMapID (toMap.getWZID ());
    }

    public void enterMap (int fromMapID, int toMapID) {
        this.basicInfo.setMapID (toMapID);
    }

    public void pickUpEquipment (MapleEquipment equipment) {
        this.knapsack.getEquipments().add (equipment);
    }

    public MapleKnapsack getKnapsack () {
        return this.knapsack;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public byte getWorldID() {
        return worldID;
    }

    public void setWorldID(byte worldID) {
        this.worldID = worldID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getRoleName() {
        return roleName;
    }


    /**
     * 新建一个指定ID的装备实例，并设置上。
     *
     * @param blouseID
     */
    public void setBlouse (int blouseID, boolean isCash) {
        MapleEquipment equipment = new MapleEquipment (blouseID);
        /* 设置状态为穿上 */
        equipment.setStatus ((byte) 1);
        armed.setBlouse(equipment, isCash);
    }

    public void setBlouse (MapleEquipment equipment, boolean isCash) {
        /* 设置状态为穿上 */
        equipment.setStatus ((byte) 1);
        this.armed.setBlouse (equipment, isCash);
    }

    public MapleEquipment getBlouse (boolean isCash) {
        return this.armed.getBlouse(isCash);
    }

    /**
     * 新建一个指定ID的装备实例，并设置上。
     *
     * @param trousersID
     */
    public void setTrousers (int trousersID, boolean isCash) {
        MapleEquipment equipment = new MapleEquipment (trousersID);
        /* 设置状态为穿上 */
        equipment.setStatus ((byte) 1);
        armed.setTrousers (equipment, isCash);
    }

    public void setTrousers (MapleEquipment equipment, boolean isCash) {
        /* 设置状态为穿上 */
        equipment.setStatus ((byte) 1);
        this.armed.setTrousers (equipment, isCash);
    }

    public MapleEquipment getTrousers (boolean isCash) {
        return this.armed.getTrousers (isCash);
    }

    /**
     * 新建一个指定ID的装备实例，并设置上。
     *
     * @param shoesID
     */
    public void setShoes (int shoesID, boolean isCash) {
        MapleEquipment equipment = new MapleEquipment (shoesID);
        /* 设置状态为穿上 */
        equipment.setStatus ((byte) 1);
        armed.setShoes (equipment, isCash);
    }

    public void setShoes (MapleEquipment equipment, boolean isCash) {
        /* 设置状态为穿上 */
        equipment.setStatus ((byte) 1);
        this.armed.setShoes (equipment, isCash);
    }

    public MapleEquipment getShoes (boolean isCash) {
        return this.armed.getShoes (isCash);
    }

    /**
     * 新建一个指定ID的装备实例，并设置上。
     *
     * @param weaponID
     */
    public void setWeapon (int weaponID, boolean isCash) {
        MapleEquipment equipment = new MapleEquipment (weaponID);
        /* 设置状态为穿上 */
        equipment.setStatus ((byte) 1);
        armed.setWeapon (equipment, isCash);
    }

    public void setWeapon (MapleEquipment equipment, boolean isCash) {
        /* 设置状态为穿上 */
        equipment.setStatus ((byte) 1);
        this.armed.setWeapon (equipment, isCash);
    }

    public MapleEquipment getWeapon (boolean isCash) {
        return this.armed.getWeapon (isCash);
    }

//    public MapleEquipment [] getArmedEquipmentList (boolean isCash) {
//        return this.waring.getArmedEquipmentList (isCash);
//    }

    public MapleRoleEquipped getArmedEquipped () {
        return this.armed;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean getGender () {
        return gender;
    }

    /**
     * 用在报文中的gender，男为0，女为1.
     * @return
     */
    public byte getGenderCode () {
        return (byte) (this.gender ? 0 : 1);
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public long getUnknown() {
        return unknown;
    }

    public void setUnknown(long unknown) {
        this.unknown = unknown;
    }

    public short getStrength() {
        return basicInfo.getStrength ();
    }

    public void setStrength(short strength) {
        this.basicInfo.setStrength(strength);
    }

    public short getLuck() {
        return basicInfo.getLuck ();
    }

    public void setLuck(short luck) {
        this.basicInfo.setLuck (luck);
    }

    public short getIntelligence() {
        return basicInfo.getIntelligence ();
    }

    public void setIntelligence(short intelligence) {
        this.basicInfo.setIntelligence(intelligence);
    }

    public short getDexterity() {
        return basicInfo.getDexterity();
    }

    public void setDexterity(short dexterity) {
        this.basicInfo.setDexterity(dexterity);
    }

    public short getHP() {
        return basicInfo.getHP ();
    }

    public void setHP(short HP) {
        this.basicInfo.setHP (HP);
    }

    public short getMaxHP() {
        return basicInfo.getMaxHP ();
    }

    public void setMaxHP(short maxHP) {
        this.basicInfo.setMaxHP (maxHP);
    }

    public short getMP() {
        return basicInfo.getMP ();
    }

    public void setMP(short MP) {
        this.basicInfo.setMP (MP);
    }

    public MapleRoleBasicInfo getBasicInfo() {
        return basicInfo;
    }

    public short getMaxMP() {
        return basicInfo.getMaxMP ();
    }

    public void setMaxMP(short maxMP) {
        basicInfo.setMaxMP (maxMP);
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
