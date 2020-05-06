package ez.game.ezms.server.login.handle.func;

import ez.game.ezms.server.packet.LoginServerPacketCreator;
import ez.game.ezms.server.packet.MaplePacket;
import ez.game.ezms.server.packet.OptionFunc;
import ez.game.ezms.server.packet.PacketStreamLEReader;
import org.apache.mina.core.session.IoSession;

/**
 * 用于处理客户端的角色名合法检测请求（接收功能码0x07）。
 */
public class CheckRoleName implements OptionFunc {

    @Override
    public void process (byte [] body, IoSession session) {
        PacketStreamLEReader reader = new PacketStreamLEReader (body);
        String roleName = reader.readMapleASCIIString ();

        boolean used = checkRoleName (roleName);
        MaplePacket packet = LoginServerPacketCreator.createCheckRoleNameResult (roleName, used);
        session.write (packet.getByteArray ());
    }

    /**
     * 根据roleName字符串检测此名是否合法。
     * 按道理，除了保证数据库中的唯一性，还应该保证
     * 一些敏感词汇不被使用。
     *
     * 2020/4/20  此处仅仅保证数据库唯一性。
     *
     * @param roleName  欲检测的角色名
     * @return 是否合法
     */
    private boolean checkRoleName (String roleName) {
        // TODO...
        return false;
    }


}
