package ez.game.ezms.server.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 角色穿戴的装备。面板E的信息。
 */
public class MapleRoleEquipped {

    public static final String [] WZEquipmentTypeString = {
            "UN"/*占位*/, "Cp", "Af", "Ay", "Ae",
            "Ma", "Pn", "So", "Gv", "Sr",
            "Si", "Wp", "Ri", "Ri", ""/*宠物装备*/,
            "Ri", "Ri", "WpSi",/*双手武器暂时放在此处, idx = 17*/
            "MaPn"/* 长袍暂时放在此处。 */
    };

    /**
     * 帽子装备的位置代码（主要用于报文中，标识一件
     * 装备是穿在什么位置。）
     */
    public static final byte CAP_POSITION_CODE = 1;
    /**
     * 脸饰装备的位置代码（主要用于报文中，标识一件
     * 装备是穿在什么位置。），脸饰也属于配饰类装备
     */
    public static final byte FACE_POSITION_CODE = 2;
    /**
     * 眼饰装备的位置代码（主要用于报文中，标识一件
     * 装备是穿在什么位置。），眼饰也属于配饰类装备。
     */
    public static final byte EYES_POSITION_CODE = 3;
    /**
     * 耳环装备的位置代码（主要用于报文中，标识一件
     * 装备是穿在什么位置。），耳环也属于配饰类装备
     */
    public static final byte EARS_POSITION_CODE = 4;
    /**
     * 上衣装备的位置代码（主要用于报文中，标识一件
     * 装备是穿在什么位置。）
     */
    public static final byte BLOUSE_POSITION_CODE = 5;
    /**
     * 下装装备的位置代码（主要用于报文中，标识一件
     * 装备是穿在什么位置。）
     */
    public static final byte TROUSERS_POSITION_CODE = 6;
    /**
     * 鞋子装备的位置代码（主要用于报文中，标识一件
     * 装备是穿在什么位置。）
     */
    public static final byte SHOES_POSITION_CODE = 7;
    /**
     * 手套装备的位置代码（主要用于报文中，标识一件
     * 装备是穿在什么位置。）
     */
    public static final byte GLOVE_POSITION_CODE = 8;
    /**
     * 披风装备的位置代码（主要用于报文中，标识一件
     * 装备是穿在什么位置。）
     */
    public static final byte CLOAK_POSITION_CODE = 9;
    /**
     * 盾牌装备的位置代码（主要用于报文中，标识一件
     * 装备是穿在什么位置。）
     */
    public static final byte SHIELD_POSITION_CODE = 10;
    /**
     * 武器装备的位置代码（主要用于报文中，标识一件
     * 装备是穿在什么位置。）
     */
    public static final byte WEAPON_POSITION_CODE = 11;
    /**
     * 戒指1装备的位置代码（主要用于报文中，标识一件
     * 装备是穿在什么位置。）
     */
    public static final byte RING1_POSITION_CODE = 12;
    /**
     * 戒指2装备的位置代码（主要用于报文中，标识一件
     * 装备是穿在什么位置。）
     */
    public static final byte RING2_POSITION_CODE = 13;
    /**
     * 宠物装备的位置代码，不是宠物！（主要用于报文中，标识一件
     * 装备是穿在什么位置。）
     */
    public static final byte PETEQUIP_POSITION_CODE = 14;
    /**
     * 戒指3装备的位置代码（主要用于报文中，标识一件
     * 装备是穿在什么位置。）
     */
    public static final byte RING3_POSITION_CODE = 15;
    /**
     * 戒指4装备的位置代码（主要用于报文中，标识一件
     * 装备是穿在什么位置。）
     */
    public static final byte RING4_POSITION_CODE = 16;

    /**
     * 所有穿戴在身上的装备都在此处存储，
     * 以位置编码作为索引。
     */
    private MapleEquipment [] equipped;
    /**
     * 所有穿戴在身上的现金道具，都在此处
     * 存储，以位置代码作为索引。
     */
    private MapleEquipment [] equippedCash;

    public MapleRoleEquipped() {
        int i = 0;
        /* 留一个空元作为0占位，因位置代码是从1开始的。 */
        equipped = new MapleEquipment [17];
        equippedCash = new MapleEquipment [17];
//        while (i < equipped.length) {
//            this.equipped [i] = empty;
//            this.equippedCash [i ++] = empty;
//        }
    }


    /**
     * 根据WZ文件中定义的类别字段得到道具的装备位置编码。
     * @return  未查询到返回0.否则都是大于0小于等于18的。
     *
     * 注意： 返回17代表是双手武器，但不反悔17并不代表不是，
     * 因为次函数根据WZ中的islot字段得到，特别是长枪和长矛并不
     * 会将此字段定义为"WpSi"，而是直接定义为"Wp"，所以判断不出，
     * 本函数近最大可能通过type得到位置代码。
     *
     * 返回18代表长袍，目前长袍没有发现有只定义Ma的情况。但返回仍然是18。
     */
    public static byte tryGetPositionCodeByWZString(String type) {
        for (byte i = 1; i < WZEquipmentTypeString.length; ++ i) {
            if (WZEquipmentTypeString [i].equals (type)) {
                /* 此处不做修正，双手武器(type.contain("Si"))不修正为武器。 */
//                if (i == 17) return WEAPON_POSITION_CODE;
//                if (i == 18) return BLOUSE_POSITION_CODE;
                return i;
            }
        }
        return 0;
    }

    /**
     * 返回穿在身上的装备列表。
     *
     * @param isCash 是否是现金装备。是则只返回现金装备。
     *
     * @return
     */
    public List <MapleEquipment> getArmedEquipmentList (boolean isCash) {
        List <MapleEquipment> retList = new ArrayList<> (17);
        if (isCash) {
            for (MapleEquipment eq: this.equippedCash)
                if (eq != null) retList.add (eq);
        } else {
            for (MapleEquipment eq: this.equipped)
                if (eq != null) retList.add (eq);
        }
        return retList;
    }


    /**
     * 根据位置编码得到装备实体。
     *
     * @param code  位置编码
     * @param isCash  是否是现金装备
     * @return  装备实体，若此位置没有装备什么，则返回null。
     */
    public MapleEquipment getEquipmentByPosCode (int code, boolean isCash) {
        if (code >= equipped.length || code <= 0) return null;
        else return equipped [code];
    }


    /**
     * 根据位置信息穿戴装备。
     *
     * @param equipment
     * @param isCash
     */
    public void setEquipmentByPosCode (MapleEquipment equipment, boolean isCash) {
        if (equipment == null || (equipment.getPosCode () >= equipped.length
                || equipment.getPosCode () <= 0)) return;
        if (isCash)
            equippedCash [equipment.getPosCode ()] = equipment;
        else
            equipped [equipment.getPosCode ()] = equipment;
    }

    public MapleEquipment getShoes (boolean isCash) {
        return isCash ? equippedCash [SHOES_POSITION_CODE] :
                equipped [SHOES_POSITION_CODE];
    }


    public void setShoes(MapleEquipment shoes, boolean isCash) {
        if (isCash)
            equippedCash [SHOES_POSITION_CODE] = shoes;
        else
            equipped [SHOES_POSITION_CODE] = shoes;
    }

    public MapleEquipment getWeapon(boolean isCash) {
        return isCash ? equippedCash [WEAPON_POSITION_CODE] :
                equipped [WEAPON_POSITION_CODE];
    }

    public void setWeapon(MapleEquipment weapon, boolean isCash) {
        if (isCash)
            equippedCash [WEAPON_POSITION_CODE] = weapon;
        else
            equipped [WEAPON_POSITION_CODE] = weapon;
    }

    public MapleEquipment getTrousers(boolean isCash) {
        return isCash ? equippedCash [TROUSERS_POSITION_CODE] :
                equipped [TROUSERS_POSITION_CODE];
    }

    public void setTrousers(MapleEquipment trousers, boolean isCash) {
        if (isCash)
            equippedCash [TROUSERS_POSITION_CODE] = trousers;
        else
            equipped [TROUSERS_POSITION_CODE] = trousers;
    }

    public MapleEquipment getBlouse(boolean isCash) {
        return isCash ? equippedCash [BLOUSE_POSITION_CODE] :
                equipped [BLOUSE_POSITION_CODE];
    }

    public void setBlouse(MapleEquipment blouse, boolean isCash) {
        if (isCash)
            equippedCash [BLOUSE_POSITION_CODE] = blouse;
        else
            equipped [BLOUSE_POSITION_CODE] = blouse;
    }

    public MapleEquipment getCap(boolean isCash) {
        return isCash ? equippedCash [CAP_POSITION_CODE] :
                equipped [CAP_POSITION_CODE];
    }

    public void setCap(MapleEquipment cap, boolean isCash) {
        if (isCash)
            equippedCash [CAP_POSITION_CODE] = cap;
        else
            equipped [CAP_POSITION_CODE] = cap;
    }

}
