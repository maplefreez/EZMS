package ez.game.ezms.wz.model.cache;

import ez.game.ezms.client.MapleEquipmentType;
import ez.game.ezms.client.panel.MapleRoleEquipped;
import ez.game.ezms.wz.*;

/**
 * WZ数据实例，代表一个装备。
 * 字段名称及值全来自xml文件。
 *
 * 此类基本做成只读，为了方便查询。只进行一次初始化。
 */
public class MapleWZEquipment extends MapleWZItem {
    /**
     * 装备在身上位置代码。
     * 此代码定义在MapleRoleEquipped，后续最好将两不同含义的
     * 字段分开，不要都用islot表示。
     *
     * islot来自WZ 的xml格式文件定义，定义的应该是装备穿在身上
     * 时是穿在什么位置，上衣？帽子？武器？还是鞋子等等。但WZ中
     * 此值不定义数字，而是字符串。此处已经将islot尽可能更改为位置
     * 信息了。
     *
     * 正常情况下取值范围为[1,16]表示正常的位置信息,17代表双手武器，
     * 应该处理为WEAPON_POSITION_CODE, 18代表长袍，应该处理为
     * BLOUSE_POSITION_CODE;
     */
    private int islot;
//        <string name="vslot" value="Wp"/>
//    <int name="walk" value="1"/>
//    <int name="stand" value="1"/>
    //        <string name="afterImage" value="swordOL"/>
//    <string name="sfx" value="swordL"/>

    /**
     * 装备加攻击数值。
     */
    private short attack;

    /**
     * 所要求职业。
     */
    private int reqJob;

    /**
     * 装备此道具所要求的人物等级。
     */
    private byte reqLevel;

    /**
     * 装备此道具所要求的人物力量。
     */
    private short reqSTR;

    /**
     * 装备此道具所要求的人物敏捷。
     */
    private short reqDEX;

    /**
     * 装备此道具所要求的人物智力。
     */
    private short reqINT;

    /**
     * 装备此道具所要求的人物运气。
     */
    private short reqLUK;
    /**
     * 装备增加的物理防御。
     */
    private short incPAD;

    /**
     * 可升级次数。
     */
    private byte tuc;

    /**
     * 卖给商店NPC的价钱。
     */
    private int price;

    /**
     * 攻击速度。
     */
    private byte attackSpeed;

    /**
     * 是否是现金道具，xml中1代表是，0代表不是。
     */
    private boolean cash;

    public MapleWZEquipment (long wzID, MapleData data) {
        super (wzID);
        MapleData value = data.getChildByPath ("info/islot");
        if (value != null) {
            String islotValue = MapleDataTool.getString(value);
            int code = MapleRoleEquipped.tryGetPositionCodeByWZString(islotValue);

            { // Debug output
                if (code <= 0)
                    System.err.println("We cannot found " + islotValue + ", wzID = " + wzID);
            }

            this.islot = (code);
        }

        value = data.getChildByPath ("info/attack");
        if (value != null)
            this.attack = MapleDataTool.getShort (value);

        value = data.getChildByPath ("info/reqJob");
        if (value != null) {
            if (value.getType () != MapleDataType.INT) {
                int x = 0;
                x = x ++;
            }
            this.reqJob = MapleDataTool.getInt(value);
        }

        value = data.getChildByPath ("info/reqLevel");
        if (value != null)
            this.reqLevel = (byte) MapleDataTool.getInt (value);

        value = data.getChildByPath ("info/reqSTR");
        if (value != null)
            this.reqSTR = (short) MapleDataTool.getInt (value);

        value = data.getChildByPath ("info/reqDEX");
        if (value != null)
            this.reqDEX = (short) MapleDataTool.getInt (value);

        value = data.getChildByPath ("info/reqINT");
        if (value != null)
            this.reqINT = (short) MapleDataTool.getInt (value);

        value = data.getChildByPath ("info/reqLUK");
        if (value != null)
            this.reqLUK = (short) MapleDataTool.getInt (value);

        value = data.getChildByPath ("info/incPAD");
        if (value != null)
            this.incPAD = (short) MapleDataTool.getInt (value);

        value = data.getChildByPath ("info/tuc");
        if (value != null)
            this.tuc = (byte) MapleDataTool.getInt (value);

        value = data.getChildByPath ("info/price");
        if (value != null)
            this.price = MapleDataTool.getInt (value);

        value = data.getChildByPath ("info/attackSpeed");
        if (value != null)
            this.attackSpeed = (byte) MapleDataTool.getInt (value);

        value = data.getChildByPath ("info/cash");
        if (value != null)
            this.cash = MapleDataTool.getInt (value) > 0;
    }

    /**
     * 是否是双手武器。若实例不是武器类，返回的值无效。
     * 若是双手武器，返回true；
     *
     * @return
     */
    public boolean isWeaponTwoHanded () {
        if (this.islot == 17)
            return true;
        else if (this.islot == MapleRoleEquipped.WEAPON_POSITION_CODE) {
            MapleEquipmentType type = MapleEquipmentType.getByWZID (super.getWZID ());
            // 长枪、长矛、长杖、双手斧、双手剑、双手钝器。
            return type == MapleEquipmentType.SPEAR
                    || type == MapleEquipmentType.POLEARM
                    || type == MapleEquipmentType.STAFF
                    || type == MapleEquipmentType.TWO_HANDED_AXE
                    || type == MapleEquipmentType.TWO_HANDED_SWORD
                    || type == MapleEquipmentType.TWO_HANDED_BLUNT;
        }
        return false;
    }

    /**
     * 此函数判断装备是否是长袍，因为长袍穿上后不能穿裤子。
     *
     * @return
     */
    public boolean isLongCoat () {
        return this.islot == 18;
    }

    public int getIslot() {
        return islot;
    }

    /**
     * 给出道具装备在身上的位置，位置代码定义在MapleRoleEquipped，
     * 此代码从islot得到，不同于islot的是，此代码不区分双手武器
     * 还是单手武器，统一返回MapleRoleEquipped.WEAPON_POSITION_CODE，
     *
     * 注意：由于islot值来自WZ xml，这个值在大部分双手武器都定义为WpSi，但
     * 小部分可能只定义为Wp，特别是长枪和长矛，个人建议最好将服务端的WZ全部手动
     * 改掉。目前代码层面只是根据isLot的值返回，若islot显示为17（即双手武器），
     * 也会返回MapleRoleEquipped.WEAPON_POSITION_CODE。
     *
     * @return
     */
    public int getPosCode () {
        return this.islot == 17 ?
            MapleRoleEquipped.WEAPON_POSITION_CODE : this.islot;
    }

    public short getAttack() {
        return attack;
    }

    public int getReqJob() {
        return reqJob;
    }

    public byte getReqLevel() {
        return reqLevel;
    }

    public short getReqSTR() {
        return reqSTR;
    }

    public short getReqDEX() {
        return reqDEX;
    }

    public short getReqINT() {
        return reqINT;
    }

    public short getReqLUK() {
        return reqLUK;
    }

    public short getIncPAD() {
        return incPAD;
    }

    public byte getTuc() {
        return tuc;
    }

    public int getPrice() {
        return price;
    }

    public byte getAttackSpeed() {
        return attackSpeed;
    }

    public boolean isCash() {
        return cash;
    }
}
