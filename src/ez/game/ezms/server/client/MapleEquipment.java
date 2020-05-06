package ez.game.ezms.server.client;

/**
 * 人物装备实体，先这么设计，后续会有很多改动。
 */
public class MapleEquipment extends MapleItem {
    /**
     * 物品所属角色的ID
     */
	private int roleID;

    /**
     * 物品所属账号的ID
     */
	private int accountID;

    /**
     * 物品剩余的可强化次数。
     * 或者用于存储物品已经做的可强化次数。
     */
	private byte remainingenhance;

	// 当前装备放置状态，0背包、1穿戴在身上、2仓库。
	private byte status;


    /**
     * 制作者字符串。
     */
	private String ownerName;

    /**
	 *装备的位置编码，也是所谓的
     *装备类别编码，指定装备是手套，还是
     *上衣，还是鞋子等：
     *
     * 1 帽子
     * 2 脸饰
     * 3 眼饰
     * 4 耳环
     * 5 上衣
     * 6 裤子
     * 7 鞋子
     * 8 手套
     * 9 披风
     * 10 盾牌
     * 11 武器
     * 12 戒指1
     * 13 戒指2
     * 14 宠物装备
     * 15 戒指3
     * 16 戒指4 */
	private byte posCode;

    /**
     * 是否是现金装备。
     */
	private boolean isCash;

	// '额外提升的力量，来自卷轴强化
	private short extrastr;

	//'额外增加的敏捷，来自卷轴强化
	private short extradex;

	// 额外增加的智力，来自卷轴强化
	private short extraint;

	//额外增加的运气，来自卷轴强化
	private short extraluk;

    // 穿戴需要的等级。
	private short level;

	//额外提升的HP'
	private short extraHP;

	private short extraMP;

    // 命中率
	private short hitPercentage;

	//额外增加的物理防御，来自卷轴强化
	private short extraphydef;

	//额外增加的魔法防御，来自卷轴强化
	private short extramgcdef;

	//额外增加的攻击力，来自卷轴强化
	private short extraattack;

    // 额外的魔法力（即魔法攻击的攻击力。）
	private short extraMagic;

	// 额外增加的回避率，来自卷轴强化
	private short extradodge;

	// 额外增加的速度，来自卷轴强化
	private short extraspeed;

	// 额外增加的跳跃，来自卷轴强化
	private short extrajump;

	// 额外增加的暴击，来自卷轴强化
	private byte extravicious;

	public MapleEquipment () { super (); }

	public MapleEquipment (long WZID) {
	    super (WZID);
    }

    public boolean getIsCash() {
        return isCash;
    }

    public void setIsCash(boolean isCash) {
        this.isCash = isCash;
    }


    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public byte getRemainingenhance() {
        return remainingenhance;
    }

    public void setRemainingenhance(byte remainingenhance) {
        this.remainingenhance = remainingenhance;
    }

    public byte getStatus() {
        return status;
    }

    public byte getPosCode() {
        return posCode;
    }

    public void setPosCode(byte posCode) {
        this.posCode = posCode;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public short getExtrastr() {
        return extrastr;
    }

    public void setExtrastr(short extrastr) {
        this.extrastr = extrastr;
    }

    public short getExtradex() {
        return extradex;
    }

    public void setExtradex(short extradex) {
        this.extradex = extradex;
    }

    public short getExtraint() {
        return extraint;
    }

    public void setExtraint(short extraint) {
        this.extraint = extraint;
    }

    public short getExtraluk() {
        return extraluk;
    }

    public void setExtraluk(short extraluk) {
        this.extraluk = extraluk;
    }

    public short getExtraHP() {
        return extraHP;
    }

    public void setExtraHP(short extraHP) {
        this.extraHP = extraHP;
    }

    public short getExtraMP() {
        return extraMP;
    }

    public void setExtraMP(short extraMP) {
        this.extraMP = extraMP;
    }

    public short getExtraphydef() {
        return extraphydef;
    }

    public void setExtraphydef(short extraphydef) {
        this.extraphydef = extraphydef;
    }

    public short getExtramgcdef() {
        return extramgcdef;
    }

    public void setExtramgcdef(short extramgcdef) {
        this.extramgcdef = extramgcdef;
    }

    public short getExtraattack() {
        return extraattack;
    }

    public void setExtraattack(short extraattack) {
        this.extraattack = extraattack;
    }

    public short getExtradodge() {
        return extradodge;
    }

    public void setExtradodge(short extradodge) {
        this.extradodge = extradodge;
    }

    public short getExtraspeed() {
        return extraspeed;
    }

    public void setExtraspeed(short extraspeed) {
        this.extraspeed = extraspeed;
    }

    public short getExtrajump() {
        return extrajump;
    }

    public void setExtrajump(short extrajump) {
        this.extrajump = extrajump;
    }

    public byte getExtravicious() {
        return extravicious;
    }

    public void setExtravicious(byte extravicious) {
        this.extravicious = extravicious;
    }

    public short getExtraMagic() {
        return extraMagic;
    }

    public void setExtraMagic(short extraMagic) {
        this.extraMagic = extraMagic;
    }

    public short getHitPercentage() {
        return hitPercentage;
    }

    public void setHitPercentage(short hitPercentage) {
        this.hitPercentage = hitPercentage;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }
}
