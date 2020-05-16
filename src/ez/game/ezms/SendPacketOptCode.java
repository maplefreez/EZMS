package ez.game.ezms;


/**
 * 定义服务器发送给客户端的报文功能码。
 * 功能码真正的值是code，而不一定是枚举值。
 */
public enum SendPacketOptCode {
    /* ---------------- LoginServer用 -----------------*/
    LOGIN_RESULT (1),

    /* 回显世界服务器状态（蓝蜗牛、红螃蟹等）. */
    SERVER_STATUS (4),

    /* 列举世界服务器信息。 */
    LIST_SERVER (5),

    /* 列举某个世界服务器下的当前账号
    * 创建的角色信息。 */
    LIST_CHARACTER (6),

    /* 回发某个世界服务器的网络地址信息到客户端。
    * 用户点选某个角色时（接收功能码0x05）以此回应。 */
    WORLDSERVER_ADDR (7),

    /* 将角色名是否可用的查询结果返回给
     * 用户。 在创建角色时，角色会使用
     * 角色名检查。此功能码用于响应报文。 */
    CHECK_NAME_RESULT (8),

    /* 创建角色成功后发送给用户的报文。
     * 用户设置好新角色，点击“创建角色”时
     * 将发送创建报文（0xB），此功能码
     * 用于创建完成的响应报文。 */
    ROLE_CREATED (12),

    /* ---------------- WorldServer用 -----------------*/
    LOGIN_WORLDSERVER (43),

    /* 发送消息给客户端。
     */
    MESSAGE_SEND (0x28),

    WARP_MAP (43),
    ;

    /**
     * 功能码值。
     */
    int code;

    SendPacketOptCode (int code) {
        this.code = code;
    }

    public int getCode () {
        return this.code;
    }
}
