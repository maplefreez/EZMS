package ez.game.ezms.client.panel;

import ez.game.ezms.client.MapleRole;
import ez.game.ezms.client.MapleRoleState;
import ez.game.ezms.server.packet.MaplePacket;
import ez.game.ezms.sql.Business;

/**
 * 角色基础信息，包括能力值四项、人气、
 * 当前地图位置，当前站立位置等。
 * 主要是能力界面显示的各项数值。
 */
public class MapleRoleBasicInfo {
    /**
     * 剩余Ability Point，
     * 剩余能力点。
     */
    private short remainingAP;

    /**
     * 能力值：力量
     */
    private short strength;

    /**
     * 能力值：运气
     */
    private short luck;

    /**
     * 能力值：智力
     */
    private short intelligence;

    /**
     * 能力值：敏捷
     */
    private short dexterity;

    /**
     * 基础HP，当前的剩余量。
     */
    private short HP;

    /**
     * 基础最大HP
     */
    private short MaxHP;

    /**
     * 基础MP，当前的剩余量。
     */
    private short MP;

    /**
     * 基础最大MP。
     */
    private short MaxMP;

    /**
     * 职业
     */
    private short job;

    /**
     * 当前冒险币持有量，不包括仓库。
     * 虽然在下不知道这个变量是什么的
     * 缩写。
     */
    private long mesos;

    /**
     * 剩余技能点（skill point）。
     * 注意，在数据库中这个字段存储的是五个字段，
     * 分别对应五个职业阶段，但在低版本冒险岛中
     * 技能点并不区分是来自哪一个阶段的职业。
     */
    private short remainingSP;

    /**
     * 经验值。
     */
    private int experience;

    /**
     * 人气，声望。
     */
    private short fame;

    /**
     * 角色等级
     */
    private byte level;

    /**
     * 肤色
     */
    private byte skin;

    /**
     * 脸型
     */
    private int face;

    /**
     * 头型（还是发色？）
     */
    private int hair;

    /**
     * 当前所在地图的ID；
     */
    private int mapID;

    /**
     * 进入游戏世界后，角色应该站在什么位置。
     * 这个位置在WZ中每个地图都有定义。
     */
    private byte initSpawnPoint;

    /**
     * 修改掩码，若修改了某个状态值，此字段指示的位将会被设置。
     * 某些字段无法囊括将使用isOtherFieldWrote指示，但不再具体
     * 指示哪个字段被修改。
     */
    private volatile int writeMask;

    /**
     * 指示mapID，initSpawnPoint，两个字段是否被修改。
     */
    private volatile boolean isOtherFieldWrote;


    /**
     * 创建一个默认的能力值实例。
     *
     * @return
     */
    public static MapleRoleBasicInfo createDefaultAbilityEntity () {
        MapleRoleBasicInfo ability = new MapleRoleBasicInfo();
        ability.setStrength ((short) 5);
        ability.setDexterity ((short) 5);
        ability.setIntelligence ((short) 5);
        ability.setLuck ((short) 5);
        ability.setHP ((short) 50);
        ability.setMaxHP((short) 50);
        ability.setMP ((short) 50);
        ability.setMaxMP ((short) 50);
        return ability;
    }

    /**
     * 将此实例按报文语法序列化。
     *
     * @param writer  写入此缓冲区中。
     */
    public void getPacketEntity (MaplePacket.PacketStreamLEWriter writer) {
        writer.writeByte (level);
        writer.writeByte (this.skin);
        writer.writeInt (face);
        writer.writeInt (hair);
        writer.writeLong (0L);  // Pet SN
        writer.writeShort (job);

        writer.writeShort (strength);
        writer.writeShort (dexterity);
        writer.writeShort (intelligence);
        writer.writeShort (luck);
        writer.writeShort (HP);
        writer.writeShort (MaxHP);
        writer.writeShort (MP);
        writer.writeShort (MaxMP);
        writer.writeShort (remainingAP);
        writer.writeShort (remainingSP);
        writer.writeInt (experience);
        writer.writeShort (fame);
        writer.writeInt (mapID);
        writer.writeByte (initSpawnPoint);

        /* 鄙人并不清楚下面三个字段的含义。 */
        writer.writeReversedLong (System.currentTimeMillis());
        writer.writeInt(0);
        writer.writeInt(0);
    }

    public byte [] getPacketEntity () {
        MaplePacket.PacketStreamLEWriter writer = new MaplePacket.PacketStreamLEWriter (32);
        getPacketEntity (writer);
        return writer.generate ().getByteArray ();
    }

    /**
     * 将此实例更新回MySQL，此函数并非强制更新，仅仅按已经写入了数据进行更新。
     * TODO...
     * @param role   角色ID。
     */
    public void tryWriteBack2DB (MapleRole role) {
        /* 由MapleRoleState定义的字段被修改了。 */
        if (this.writeMask != 0x00) {
            // 目前将字段全部写回，不区分是哪个字段被修改。
            Business.updateRoleBasicInfo2DB (role, this, true);
        }

        if (this.isOtherFieldWrote) {
            // 目前将字段全部写回，不区分是哪个字段被修改。
        }
    }

    public long getMesos() {
        return mesos;
    }

    public void setMesos(long mesos) {
        this.mesos = mesos;
    }

    public short getRemainingSP() {
        return remainingSP;
    }

    public void setRemainingSP(short remainingSP) {
        this.remainingSP = remainingSP;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public short getFame() {
        return fame;
    }

    public void setFame(short fame) {
        this.fame = fame;
    }

    public int getMapID() {
        return mapID;
    }

    public void setMapID(int mapID) {
        this.mapID = mapID;
    }

    public byte getInitSpawnPoint() {
        return initSpawnPoint;
    }

    public void setInitSpawnPoint(byte initSpawnPoint) {
        this.initSpawnPoint = initSpawnPoint;
    }

    public short getRemainingAP() {
        return remainingAP;
    }

    public void setRemainingAP(short remainingAP) {
        this.remainingAP = remainingAP;
    }

    public short getStrength() {
        return strength;
    }

    public void setStrength(short strength) {
        this.strength = strength;
    }

    public short getLuck() {
        return luck;
    }

    public void setLuck(short luck) {
        this.luck = luck;
    }

    public short getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(short intelligence) {
        this.intelligence = intelligence;
    }

    public short getDexterity() {
        return dexterity;
    }

    public void setDexterity(short dexterity) {
        this.dexterity = dexterity;
    }

    public short getHP() {
        return HP;
    }

    public void setHP(short HP) {
        this.HP = HP;
    }

    public short getMaxHP() {
        return MaxHP;
    }

    public void setMaxHP(short maxHP) {
        MaxHP = maxHP;
    }

    public short getMP() {
        return MP;
    }

    public void setMP(short MP) {
        this.MP = MP;
    }

    public short getMaxMP() {
        return MaxMP;
    }

    public void setMaxMP(short maxMP) {
        MaxMP = maxMP;
    }

    public short getJob() {
        return job;
    }

    public void setJob(short job) {
        this.job = job;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public byte getSkin() {
        return skin;
    }

    public void setSkin(byte skin) {
        this.skin = skin;
    }

    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public int getHair() {
        return hair;
    }

    public void setHair(int hair) {
        this.hair = hair;
    }

    public int getWriteMask() {
        return writeMask;
    }

    public void setWriteMask(int writeMask) {
        this.writeMask = writeMask;
    }

    public boolean isOtherFieldWrote() {
        return isOtherFieldWrote;
    }

    public void setOtherFieldWrote(boolean otherFieldWrote) {
        isOtherFieldWrote = otherFieldWrote;
    }
}
