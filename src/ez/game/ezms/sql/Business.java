package ez.game.ezms.sql;

import ez.game.ezms.client.model.MapleEquipment;
import ez.game.ezms.client.model.MapleItem;
import ez.game.ezms.client.MapleItemType;
import ez.game.ezms.client.MapleRole;
import ez.game.ezms.sql.models.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Business {

    /**
     * 根据账号查询数据库的中的账号信息，若
     * 存在此信息，则返回填充信息的实体，否则返回null。
     * 填充的字段如下：
     * ID、account, password(已加密), gender, GMlevel,
     * islogined, isbanned, bannedreason;
     *
     * @param account  账号字符串
     *
     * @return  账号实体（成功匹配） 或者 null（未匹配成功）
     *
     * 注：此函数只是将数据取出，若做判断等逻辑，请调用者自行完成。
     */
    public static Account searchUserAccountAndPassword (String account) {
        DBDataSource ds = DBDataSource.getOrInitializeDBDataSource ();
        Connection conn = ds.getConnection ();
        ResultSet returnSet = null;
        try {
            PreparedStatement pStmt = conn.prepareStatement("select ID, password, gender, " +
                    "GMlevel, islogined, isbanned, bannedreason from EZMS_ACCOUNTS where account = ?");
            pStmt.setString (1, account);
            returnSet = pStmt.executeQuery ();
            if (! returnSet.first ())
                return null;
        } catch (SQLException ex) {
            ex.printStackTrace ();
            return null;
        }

        /* 填充数据实体。 */
        Account retEntity = new Account ();
        try {
            retEntity.setId (returnSet.getInt (1));
            retEntity.setAccount (account);
            retEntity.setPassword (returnSet.getString (2));
            retEntity.setGender (returnSet.getShort (3) > 0);
            retEntity.setGMlevel (returnSet.getShort (4));
            retEntity.setIslogined (returnSet.getShort (5) > 0);
            retEntity.setIsbanned (returnSet.getShort (6) > 0);
            retEntity.setBannedReason (returnSet.getShort (7));
        } catch (SQLException ex) {
            ex.printStackTrace ();
            return null;
        }

        return retEntity;
    }

    /**
     * 将用户身上的装备存入数据库。特指其背包中所有的装备类。
     * role实例至少需要提供accountid和roleid。
     *
     * @param role
     */
    public static void insertRoleEquipments2DB (MapleRole role) {
        List<MapleEquipment> equipments = role.getKnapsack ().getEquipments ();
        for (MapleEquipment equipment : equipments)
            insertEquipment2DB (role, equipment);
    }

    /**
     * 将一个装备存入数据库，其中role实例至少需要提供accountid和roleid。
     *
     * @param role
     * @param equipment
     * @return  成功返回true.
     */
    public static boolean insertEquipment2DB (MapleRole role, MapleEquipment equipment) {
        DBDataSource ds = DBDataSource.getOrInitializeDBDataSource ();
        Connection conn = ds.getConnection ();

        try {
            PreparedStatement pStmt = conn.prepareStatement("insert into EZMS_EQUIPMENT (roleID," +
                    "accountID,equipmentID,remainingenhance,`status`,extrastr,extradex,extraint,extraluk," +
                    "extraHP,extraMP,extraphydef,extramgcdef,extraattack,extradodge,extraspeed,extrajump," +
                    "extravicious,poscode)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
            pStmt.setInt (1, role.getID ());
            pStmt.setInt (2, role.getAccountID ());
            pStmt.setLong (3, equipment.getWZID ());
            pStmt.setByte (4, equipment.getRemainingenhance ());
            pStmt.setByte (5, equipment.getStatus ());
            pStmt.setShort (6, equipment.getExtrastr ());
            pStmt.setShort (7, equipment.getExtradex ());
            pStmt.setShort (8, equipment.getExtraint ());
            pStmt.setShort (9, equipment.getExtraluk ());
            pStmt.setShort (10, equipment.getExtraHP ());
            pStmt.setShort (11, equipment.getExtraMP ());
            pStmt.setShort (12, equipment.getExtraphydef ());
            pStmt.setShort (13, equipment.getExtramgcdef ());
            pStmt.setShort (14, equipment.getExtraattack ());
            pStmt.setShort (15, equipment.getExtradodge ());
            pStmt.setShort (16, equipment.getExtraspeed());
            pStmt.setShort (17, equipment.getExtrajump ());
            pStmt.setByte (18, equipment.getExtravicious ());
            pStmt.setByte (19, equipment.getPosCode ());

            return pStmt.execute ();
        } catch (SQLException ex) {
            ex.printStackTrace ();
            return false;
        }
    }

    /**
     * 插入新角色数据到MySQL。
     *
     * @return  成功返回true.
     */
    public static boolean insertRole2DB (MapleRole role) {
        DBDataSource ds = DBDataSource.getOrInitializeDBDataSource ();
        Connection conn = ds.getConnection ();

        try {
            PreparedStatement pStmt = conn.prepareStatement("insert into EZMS_ROLES (" +
                    "accountID,worldserverID,rolename,strength,dexterity,luck,intelligence," +
                    "HP,MP,maxHP,maxMP,MESOS,jobID,skin,gender,hair,face,locatemapID," +
                    "maxfriend)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            pStmt.setInt (1, role.getAccountID ());
            pStmt.setInt (2, role.getWorldID ());
            pStmt.setString (3, role.getRoleName ());
            pStmt.setShort (4, role.getStrength());
            pStmt.setShort (5, role.getDexterity());
            pStmt.setShort (6, role.getLuck());
            pStmt.setShort (7, role.getIntelligence());
            pStmt.setShort (8, role.getHP());
            pStmt.setShort (9, role.getMP());
            pStmt.setShort (10, role.getMaxHP());
            pStmt.setShort (11, role.getMaxMP());
            pStmt.setLong (12, role.getMesos());
            pStmt.setShort (13, role.getJob ());
            pStmt.setByte (14, role.getSkin());
            pStmt.setByte (15, role.getGender() ? (byte)1 : (byte)0);
            pStmt.setInt (16, role.getHair());
            pStmt.setInt (17, role.getFace());
            pStmt.setInt (18, role.getMapID());
            pStmt.setInt (19, 25);

            pStmt.executeUpdate ();
            /* 设置生成的主键。 */
            ResultSet rs = pStmt.getGeneratedKeys ();
            if (rs.next ()) {
                role.setID (rs.getInt (1));
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace ();
        }
        return false;
    }

    /**
     * 根据角色的数据库ID得到角色的信息实体。
     *
     * @return
     */
    public static MapleRole searchRoleByRoleID (int roleID) {
        DBDataSource ds = DBDataSource.getOrInitializeDBDataSource ();
        Connection conn = ds.getConnection ();
        ResultSet returnSet;
        try {
            PreparedStatement pStmt = conn.prepareStatement ("select * from EZMS_ROLES where ID=?;");
            pStmt.setInt (1, roleID);
            returnSet = pStmt.executeQuery ();
            if (returnSet.next ())
                return fillRoleDataFromOneResult (returnSet);
        } catch (SQLException ex) {
            ex.printStackTrace ();
        }
        return null;
    }

    /**
     * 从数据库查找账号ID在指定直接服务器下创建的
     * 角色基本信息。(EZMS_ROLES).
     *
     * @param accountID
     * @param serverID
     * @return
     */
    public static List <MapleRole> searchRolesFromAccount (int accountID, int serverID) {
        DBDataSource ds = DBDataSource.getOrInitializeDBDataSource ();
        Connection conn = ds.getConnection ();
        ResultSet returnSet;
        try {
            PreparedStatement pStmt = conn.prepareStatement("select * from EZMS_ROLES where accountID=? and worldserverID=?;");
            pStmt.setInt (1, accountID);
            pStmt.setInt (2, serverID);
            returnSet = pStmt.executeQuery ();
            return fillRolesDataFromSearchResult(returnSet);
        } catch (SQLException ex) {
            ex.printStackTrace ();
            return null;
        }
    }

    private static List <MapleRole> fillRolesDataFromSearchResult(ResultSet resultSet) throws SQLException {
        List <MapleRole> roles = new ArrayList <> (3);
        while (resultSet.next ()) {
            MapleRole role = fillRoleDataFromOneResult (resultSet);
            roles.add (role);
        }
        return roles;
    }

    private static MapleRole fillRoleDataFromOneResult (ResultSet resultSet) throws SQLException {
        MapleRole role = new MapleRole ();
        role.setRoleName (resultSet.getString ("rolename"));
        role.setAccountID (resultSet.getInt (2));
        role.setID (resultSet.getInt (1));
        role.setGender (resultSet.getByte ("gender") <= 0); // 数据库中存的0表示男。
        role.setSkin (resultSet.getByte ("skin"));
        role.setFace (resultSet.getInt ("face"));
        role.setHair (resultSet.getInt ("hair"));
        role.setLevel (resultSet.getByte (5));
        role.setJob (resultSet.getShort ("jobID"));

        role.setStrength (resultSet.getShort ("strength"));
        role.setDexterity (resultSet.getShort ("dexterity"));
        role.setIntelligence (resultSet.getShort ("intelligence"));
        role.setLuck (resultSet.getShort ("luck"));

        role.setHP (resultSet.getShort ("HP"));
        role.setMaxHP (resultSet.getShort ("maxMP"));
        role.setMP (resultSet.getShort ("MP"));
        role.setMaxMP (resultSet.getShort ("maxMP"));

        role.setRemainingAP (resultSet.getShort ("AP"));
//            role.setRemainingSP (resultSet.getShort ("SP"));

        role.setExperience (resultSet.getInt ("experience"));

        role.setFame (resultSet.getShort ("fame"));
        role.setMapID (resultSet.getInt ("locatemapID"));
        role.setInitSpawnPoint (resultSet.getByte ("spawnpoint"));
        return role;
    }

    /**
     * 根据角色的ID查找用户穿戴在身上的装备。
     *
     * @return  装备列表
     */
    public static List <MapleEquipment> searchArmedEquipmentsFromRoleID (int roleID) {
        /* status 字段为1代表穿带在身上。 */
        try {
            return searchSpecifiedEquipmentsByRoleID(roleID, 1);
        } catch (SQLException ex) {
            ex.printStackTrace ();
            return null;
        }
    }

    /**
     * 根据角色的ID查找用户的某一类装备信息。
     *
     * @param roleID  用户ID
     * @param type    类别字段:
     *                0：
     *                1: 穿戴在身上的。
     *                2: 现金装备。
     * @return
     */
    private static List <MapleEquipment> searchSpecifiedEquipmentsByRoleID(int roleID, int type)
            throws SQLException {
        DBDataSource ds = DBDataSource.getOrInitializeDBDataSource ();
        Connection conn = ds.getConnection ();
        ResultSet returnSet;
        PreparedStatement pStmt = conn.prepareStatement("select * from EZMS_EQUIPMENT where roleID=? and `status`=1;");
        pStmt.setInt (1, roleID);
        returnSet = pStmt.executeQuery ();
        return fillEquipmentDataFromSearchResult(returnSet);
    }

    private static List <MapleEquipment> fillEquipmentDataFromSearchResult(ResultSet resultSet) throws SQLException {
        List <MapleEquipment> equipments = new ArrayList <> (4);
        while (resultSet.next ()) {
            MapleEquipment equipment = new MapleEquipment ();
            equipment.setDBID (resultSet.getLong (1));
            equipment.setRoleID (resultSet.getInt (2));
            equipment.setAccountID (resultSet.getInt (3));
            equipment.setWZID (resultSet.getLong (4));
            equipment.setPosCode (resultSet.getByte ("poscode"));
            equipment.setRemainingenhance (resultSet.getByte ("remainingenhance"));
            equipment.setStatus (resultSet.getByte ("status"));
            equipment.setExtraattack (resultSet.getShort ("extraattack"));
            equipment.setExtradex (resultSet.getShort ("extradex"));
            equipment.setExtrastr (resultSet.getShort ("extrastr"));
            equipment.setExtraint (resultSet.getShort ("extraint"));
            equipment.setExtraluk (resultSet.getShort ("extraluk"));
            equipment.setExtraHP (resultSet.getShort ("extraHP"));
            equipment.setExtraMP (resultSet.getShort ("extraMP"));
            equipment.setExtraphydef (resultSet.getShort ("extraphydef"));
            equipment.setExtramgcdef (resultSet.getShort ("extramgcdef"));
            equipment.setExtraspeed (resultSet.getShort ("extraspeed"));
            equipment.setExtrajump (resultSet.getShort ("extrajump"));
            equipment.setExtravicious (resultSet.getByte ("extravicious"));
            equipment.setExtradodge (resultSet.getByte ("extradodge"));

            equipments.add (equipment);
        }
        return equipments;
    }

    /**
     * 根据角色的ID查找角色拥有的所有物品。
     * @param roleID  角色ID
     * @return
     */
    public static List <MapleItem> searchAllItemsByRoleID (int roleID) {
        return searchSpecifiedItemsByRoleID (roleID, MapleItemType.ALL);
    }

    /**
     * 根据角色的ID查找角色某一类物品信息。
     *
     * @param roleID  角色ID。
     * @param type    类别代码枚举
     * @return
     */
    private static List <MapleItem> searchSpecifiedItemsByRoleID(int roleID, MapleItemType type) {
        // TODO... 目前返回空表，因为数据库没有做。
        return new ArrayList <> (0);
    }


}
