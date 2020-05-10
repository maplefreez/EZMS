package ez.game.ezms.server.login.handle.func;

import ez.game.ezms.constance.ServerConstants;
import ez.game.ezms.server.client.MapleAccount;
import ez.game.ezms.server.packet.OptionFunc;
import ez.game.ezms.server.client.MapleClient;
//import ez.game.ezms.server.login.LoginServer;
import ez.game.ezms.server.packet.LoginServerPacketCreator;
import ez.game.ezms.server.packet.MaplePacket;
import ez.game.ezms.server.packet.PacketStreamLEReader;
import ez.game.ezms.server.world.WorldServer;
import ez.game.ezms.server.world.WorldServerSet;
import ez.game.ezms.sql.Business;
import ez.game.ezms.sql.models.Account;
import ez.game.ezms.tools.LoginCrypt;
import org.apache.mina.core.session.IoSession;

/**
 * 此类实现ChannelServer的
 * 接收功能码  0x01
 * 客户端发起登录请求，Channel获取账户及密码，
 * 并验证用户身份，完成登录所需的初始化及报文
 * 交互。
 */
public class Login implements OptionFunc {

    /**
     * 此处是处理的主体方法，message中目前只有如下信息：
     * 长度 账户字符串 长度 密码字符串 。。。
     * @param message
     */
    @Override
    public void process (byte [] message, IoSession session) {
        // 0C 00 69 63 65 6C 65 6D 6F 6E 31 33 31 34  账号（2B长度(LE)，后接ASCII编码的账号）
        // 05 00 61 64 6D 69 6E                       密码(2B长度(LE)，后接ASCII编码的密码。)
        // 00 00 24 7E DD AA F0 7E D6 03 00 00 00 00 00 53 9C 00 00 00 00
        PacketStreamLEReader packet = new PacketStreamLEReader (message);
        String login = packet.readMapleASCIIString ();
        String pwd = packet.readMapleASCIIString ();
        MapleClient client = (MapleClient) session.getAttribute (ServerConstants.CLIENT_ENTITY_KEY);

        Account acc;

        pwd = LoginCrypt.hexSha1 (pwd);
        acc = Business.searchUserAccountAndPassword (login);

        if (acc != null && pwd.equals (acc.getPassword ())) {
            /* 验证成功 */
            /* 信息填充到client。 */
            MapleAccount account = new MapleAccount (acc);
            client.setAccountEntity (account);
            /* 回送登录成功消息。 */
            pushLoginSuccessMessage (client);
            /* 推送服务器及其下各个频道信息。 */
            pushAliveServerList (client);
        } else {
            // 验证失败。
            pushLoginFailedMessage (acc, client);
        }
    }

    private void pushLoginSuccessMessage (MapleClient client) {
        MaplePacket packet = LoginServerPacketCreator.createLoginSuccess (client);
        client.getSession().write (packet.getByteArray ());
    }

    /**
     * 回传所有存活的服务器（蓝蜗牛、红螃蟹、青鳄鱼等）及负载信息到客户端。
     * @param client  客户端
     */
    private void pushAliveServerList (MapleClient client) {
        IoSession session = client.getSession ();

        /* 循环报告各个世界服务器。注意这里每一个服务器是一个单独的
         报文，单独发送。别所有服务器都挤到一个报文中。 */
        for (WorldServer server : WorldServerSet.serverSet) {
            MaplePacket retPacket = LoginServerPacketCreator.createServerLoadStatusPacket(server);
            session.write(retPacket.getByteArray());
        }
        /* 列表结束标志报文。 */
        MaplePacket endPacket = LoginServerPacketCreator.createServerLoadListPacketEnd();
        session.write (endPacket.getByteArray ());
    }

    /**
     * 当登录不成功时，可能是被封禁或者其他特殊原因，所有判断
     * 逻辑写在此处。
     *
     * @param account  从数据库查询得到的账号信息。
     * @param client   客户端实体。
     */
    private void pushLoginFailedMessage (Account account, MapleClient client) {
        /* 登录不成功，向客户端回发失败原因编码：
         * 0 - 成功登录
         * 1 - 刷新
         * 2 - 封号
         * 3 - 屏蔽了账号登录功能或者已经被删除、终止的账号
         * 4 - 屏蔽了静态密码或密码输入错误
         * 5 - 未登录的账号
         * 6 - 当前连接不稳定。请更换其它频道或世界。为您带来不便，请谅解。
         * 7 - 正在登录中的账号
         * 8 - 当前连接不稳定。请更换其它频道或世界。为您带来不便，请谅解。 (客户端直接关闭)
         * 9 - 当前连接不稳定。请更换其它频道或世界。为您带来不便，请谅解。
         * 10 - 目前因链接邀请过多 服务器未能处理。
         */
        int reason = 4;
        IoSession session = client.getSession ();
        MaplePacket retPacket = LoginServerPacketCreator.createLoginFailed (reason);
        session.write (retPacket.getByteArray ());
    }

}
