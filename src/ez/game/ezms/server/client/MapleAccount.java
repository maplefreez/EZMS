package ez.game.ezms.server.client;

import ez.game.ezms.sql.models.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户端的账号实例。
 */
public class MapleAccount extends Account {

    /**
     * 027版本的冒险岛最多能创建
     * 3个角色，所以这里直接用一个
     * 数组存储即可。
     *
     * 当选中角色之后，角色将被置于0索引处。
     * 剩余的位置将被置为空。
     */
    private MapleRole [] roles;
    private final static int CURRENT_ROLE_INDEX = 0;

    /**
     * 创建一个实体，必须输入实体的数据库ID。
     * @param accountID
     */
    public MapleAccount (int accountID) {
        super ();
        super.setId (accountID);
        // 027版本最多只能创建3个。
        roles = new MapleRole [0x03];
    }

    /**
     * 通过Account构造一个实例。
     * @param account
     */
    public MapleAccount (Account account) {
        super ();
        super.setAccount (account.getAccount ());
        super.setBannedReason (account.getBannedReason());
        super.setBirthday(account.getBirthday());
        super.setCreateddate(account.getCreateddate());
        super.setEmail(account.getEmail());
        super.setGender(account.getGender());
        super.setGMlevel(account.getGMlevel());
        super.setId(account.getId());
        super.setIsbanned(account.isIsbanned ());
        super.setIslogined(account.isIslogined());
        super.setLastLogintime(account.getLastLogintime());
        super.setPassword(account.getPassword());
        super.setPoints(account.getPoints());
        super.setVpoints(account.getVpoints());

        roles = new MapleRole [0x03];
    }

    /**
     * 将角色实体设置到此账号的角色列表中，注意，设置的
     * 索引由外界提供，idx若大于2将不执行任何操作。另外
     * 此函数只应该在RoleListReq情况下调用。
     *
     * @param role  实体
     * @param idx   索引，保证idx 范围 [0, 2]，调用者务必谨慎。
     * @return  返回下一个可以插入的位置。若插入满了返回-1.
     */
    public int setRole (MapleRole role, int idx) {
        int slot1 = getRoleSlotToBeSet ();
        if (idx > 2 || idx < 0) return slot1;

        if (slot1 >= 0) this.roles [idx ++] = role;
        return idx > 2 ? -1 : idx;
    }

    /**
     * 获取能被设置的角色数组中最小下标。
     *
     * @return 最小数组下标。
     */
    private int getRoleSlotToBeSet () {
        int idx = 0;
        if (roles [idx] == null) return idx;
        else if (roles [++ idx] == null) return idx;
        else if (roles [++ idx] == null) return idx;
        else return -1;
    }

    /**
     * 通过角色ID得到角色实体。
     * @return
     */
    private MapleRole getRoleByID (int roleID) {
        for (MapleRole r : roles) {
            if (r != null && r.getID() == roleID)
                return r;
        }
        return null; /* 若运行到此，系统应该有问题！ */
    }

    /**
     * 下线当前角色，只是将角色的实体从容器中删除。
     */
    public void offlineCurrentRole () {
        this.roles [CURRENT_ROLE_INDEX] = null;
    }

    /**
     * 返回角色列表。
     *
     * @return
     */
    public List<MapleRole> getRoles () {
        List <MapleRole> retList = new ArrayList <> (3);
        for (MapleRole r : this.roles) {
            if (r != null) retList.add(r);
        }
        return retList;
    }

    public void loginRole (MapleRole role) {
        this.roles [CURRENT_ROLE_INDEX] = role;
        /* 清空剩余两个空位置。 */
        this.roles [1] = null;
        this.roles [2] = null;
    }

    public void loginRole (int roleID) {
        // 若得到的是null，绝对不正常！ 正常情况下此处不可能出现null。
        MapleRole role = this.getRoleByID (roleID);
        loginRole (role);
    }

    /**
     * 返回当前此账号中已经登录世界服务器的角色。
     * 此函数应该在角色成功登录世界服务器后才能
     * 调用，正常情况下不可能出现返回空。
     *
     * @return 返回登录的角色实体，roles的第0号索引的实体。
     */
    public MapleRole getCurrentLoginRole () {
        return roles [CURRENT_ROLE_INDEX];
    }

}
