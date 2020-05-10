package ez.game.ezms.client;

/**
 * 装备类别，是什么类别的装备。
 * 戒指、帽子、裤子、上衣、手套、
 * 双手剑、单手剑、短刀。等等。
 */
public enum MapleEquipmentType {

    /**
     * 未知，用来占位。
     */
    UNKNOWN,

    /**
     * 单手剑
     */
    ONE_HANDED_SWORD,
    /**
     * 双手剑
     */
    TWO_HANDED_SWORD,

    /**
     * 单手斧
     */
    ONE_HANDED_AXE,
    /**
     * 双手斧
     */
    TWO_HANDED_AXE,

    /**
     * 单手钝器
     */
    ONE_HANDED_BLUNT,
    /**
     * 双手钝器
     */
    TWO_HANDED_BLUNT,

    /**
     * 弓
     */
    BOW,

    /**
     * 弩
     */
    CROSSBOW,

    /**
     * 拳套
     */
    CLAW,

    /**
     * 短刀
     */
    DAGGER,

    /**
     * 长枪
     */
    SPEAR,

    /**
     * 长矛
     */
    POLEARM,

    /**
     * 短杖
     */
    WAND,
    /**
     * 长杖
     */
    STAFF,

    /**
     * 短刀
     */
    KATARA;

    /**
     * 目前是定义来与客户端交互使用。
     */
    byte code;

    /**
     * 根据装备的WZID得到装备的类别。
     * (01302000 ~ 01302022)武器单手剑： islot = vslot = Wp, sfx = swordS
     * (01312000 ~ 01312015)武器单手斧：islot = vslot = Wp, sfx = swordL
     * (01322000 ~ 01322029)武器单手钝器：islot = vslot = Wp, sfx = mace (铁瓜锤：01322000)
     * (01332000 ~ 01332024)武器短刀：islot = Wp = vslot, sfx = swordS (牙刀：01332008； 双枝短刀：01332004, sfx = swordL)
     *
     * (01372000 ~ 01372009)武器短杖： islot = vslot = Wp, sfx = mace (大魔法师短杖：01382007)
     * (01382000 ~ 01382011)武器长杖： islot = vslot = Wp, sfx = mace (01382001)
     * (01402000 ~ 01402017)武器双手剑： islot = vslot = WpSi， sfx = swordL
     * (01412000 ~ 01412010)武器双手斧：islot = vslot = WpSi, sfx = swordS
     * (01422000 ~ 01422013)武器双手钝器：islot = vslot = WpSi, sfx = mace
     * (01432000 ~ 01432010)武器长枪：islot = vslot = Wp,  sfx = spear (1432010)
     * (01442000 ~ 01442020)武器矛：islot = vslot = Wp, sfx = poleArm
     * (01452000 ~ 01452021)武器弓箭： islot = vslot = WpSi, sfx = bow (战斗弓：01452000)
     * (01462000 ~ 01462017)武器弩：  islot = vslot = WpSi, sfx = cBow (炎弩：01462007)
     * (1472000 ~ 1472031)武器拳套：islot = Wp = vslot, sfx = tGlove (01472010)
     *
     * @return  在大于1472031 小于1302000时，返回UNKNOWN.
     */
    public static MapleEquipmentType getByWZID (long wzID) {
        if (wzID > 1472031) return UNKNOWN;
        else if (wzID >= 1472000) return CLAW;
        else if (wzID >= 1462000) return CROSSBOW;
        else if (wzID >= 1452000) return BOW;
        else if (wzID >= 1442000) return POLEARM;
        else if (wzID >= 1432000) return SPEAR;
        else if (wzID >= 1422000) return TWO_HANDED_BLUNT;
        else if (wzID >= 1412000) return TWO_HANDED_AXE;
        else if (wzID >= 1402000) return TWO_HANDED_SWORD;
        else if (wzID >= 1382000) return STAFF;
        else if (wzID >= 1372000) return WAND;
        else if (wzID >= 1332000) return KATARA;
        else if (wzID >= 1322000) return ONE_HANDED_BLUNT;
        else if (wzID >= 1312000) return ONE_HANDED_AXE;
        else if (wzID >= 1302000) return ONE_HANDED_SWORD;
        else return UNKNOWN;
    }

}
