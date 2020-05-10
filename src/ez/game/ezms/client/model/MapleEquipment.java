package ez.game.ezms.client.model;

import ez.game.ezms.server.packet.MaplePacket.PacketStreamLEWriter;

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

    // 装备强化的次数。即砸了多少此卷。
	private short upgradeTimes;

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

	// 提升角色的手技。
	private byte juggle;

	public MapleEquipment () { super (); }

	public MapleEquipment (long WZID) {
	    super (WZID);
    }


    public byte [] getPacketEntity () {
	    PacketStreamLEWriter writer = new PacketStreamLEWriter (3);
        writePacketEntityInto (writer);
        return writer.generate ().getByteArray();
    }

    public void writePacketEntityInto (PacketStreamLEWriter buffer) {
	    buffer.writeByte (this.posCode);  // position code
        buffer.writeInt ((int) super.getWZID ()); // WZ ID
    }

    /**
     * 获取
     * @return
     */
    public byte [] getVerbosePacketEntity () {
        PacketStreamLEWriter writer = new PacketStreamLEWriter (256);
        writeVerbosePacketEntityInto (writer);
        return writer.generate ().getByteArray ();
    }

    public void writeVerbosePacketEntityInto (PacketStreamLEWriter writer) {
//        writer.writeByte (this.posCode);
//        writeCommonEquipmentItemInfoHeader (writer, equipment);
        writePacketEntityInto (writer);
        writer.writeByte ((byte) 0);
        writer.writeLong (150842304000000000L); // ElapseTime

        writer.writeByte (remainingenhance);
        writer.writeByte ((byte) upgradeTimes); // 装备已经强化的次数
        writer.writeShort (extrastr);
        writer.writeShort (extradex);
        writer.writeShort (extraint);
        writer.writeShort (extraluk);
        writer.writeShort (extraHP);
        writer.writeShort (extraMP);

        /* 装备的要求信息，比如要求等级，要求敏捷等等，
        都不用从服务器传回，客户端自有WZ数据。 */
        writer.writeShort (extraattack);// 物理攻击
        writer.writeShort (extraMagic);// 魔法攻击
        writer.writeShort (extraphydef);// 物理防御
        writer.writeShort (extramgcdef);// 魔法防御

        writer.writeShort (hitPercentage);   // 命中率
        writer.writeShort (extradodge); // 回避率
        writer.writeShort (juggle);  // 手技
        writer.writeShort (extraspeed);  // 速度
        writer.writeShort (extrajump);   // 跳跃
        writer.writeMapleStoryASCIIString (ownerName);  // 制作者信息。
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

    public byte getJuggle() {
        return juggle;
    }

    public void setJuggle(byte juggle) {
        this.juggle = juggle;
    }

    public short getUpgradeTimes() {
        return upgradeTimes;
    }

    public void setUpgradeTimes(short upgradeTimes) {
        this.upgradeTimes = upgradeTimes;
    }
}
