package ez.game.ezms.server.client;

/**
 * 角色能力面板的所有属性。
 */
public class MapleRoleAbility {
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
     * 创建一个默认的能力值实例。
     *
     * @return
     */
    public static MapleRoleAbility createDefaultAbilityEntity () {
        MapleRoleAbility ability = new MapleRoleAbility ();
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
}
