package ez.game.ezms.server.world.handle.func;


import ez.game.ezms.constance.ServerConstants;
import ez.game.ezms.client.MapleClient;
import ez.game.ezms.client.MapleRole;
import ez.game.ezms.server.packet.WorldServerPacketCreator;
import ez.game.ezms.server.packet.MaplePacket;
import ez.game.ezms.server.packet.OptionFunc;
import ez.game.ezms.server.packet.PacketStreamLEReader;
import ez.game.ezms.server.world.WorldServerSet;
import ez.game.ezms.server.world.handle.WorldServerHandler;
import ez.game.ezms.tools.MapleAESOFB;
import org.apache.mina.core.session.IoSession;

/**
 * 玩家点击某个角色，角色登录世界服务器，最初的角色信息推送操作流程
 * 在此。流程如下，每一个项目是一个报文。因为是单独的报文，所以无顺
 * 序之分，但个人建议第一条一定先发出去。
 *
 * 1、角色基本信息. （必须）
 *      基本信息、穿戴在身上的装备、背包装备类物品、
 *      背包消耗类物品、背包设置类物品、背包其他类物品、
 *      背包现金类物品. 技能信息、任务信息（所有任务？）、
 *      指环信息。
 * 2、快件到达通知。
 * 2、好友信息.
 * 3、技能链宏指令信息。
 * 4、消息（可能是在离线情况下好友发送的）
 * 5、在线家族信息
 * 6、组队成员HP信息
 * 7、角色键盘设置信息。
 * 8、已开始任务信息，击杀怪物更新。
 * 9、好友申请信息通知。
 * 10、召唤宠物。
 * 11、发送欢迎信息。
 */
public class RoleLogin implements OptionFunc {

    @Override
    public void process (byte [] message, IoSession session) {
        PacketStreamLEReader reader = new PacketStreamLEReader (message);
        int roleID = reader.readInt ();

        MapleClient client = WorldServerSet.removeCacheClientByID (roleID);
        if (client == null) {
            // 处理异常情况。
            return;
        }

        MapleRole role = client.getAccountEntity ().getCurrentLoginRole ();
        if (role == null) {
            // 处理异常情况。
            return;
        }

        setTags (client, session);

        /* 从数据库加载所有的用户数据。 */
        role.loadAllItemsFromDB ();
        role.loadSkillDataFromDB ();
        role.loadQuestDataFromDB ();

        /* TODO... 发送基本角色信息。 */
        MaplePacket packet = WorldServerPacketCreator.enterWorldServer (client, role);
        session.write (packet.getByteArray ());
    }


    /**
     * 设置相关标志位。
     *
     * @param client
     * @param session
     */
    private void setTags (MapleClient client, IoSession session) {
        MapleAESOFB recv = (MapleAESOFB) session.getAttribute (WorldServerHandler.RECVIV_KEY);
        MapleAESOFB send = (MapleAESOFB) session.getAttribute (WorldServerHandler.SENDIV_KEY);

        /* 将原来的LoginServer会话换成WorldServer的会话。 */
        client.changeSession (session);

        client.setHasShakeHand (true);
        client.setRcvCypher (recv);
        client.setSndCypher (send);

        session.setAttribute (ServerConstants.CLIENT_ENTITY_KEY, client);
    }

}
