package ez.game.ezms.server.world.handle.func;

import ez.game.ezms.client.MapleClient;
import ez.game.ezms.client.MapleRole;
import ez.game.ezms.client.model.MapleAccount;
import ez.game.ezms.constance.ServerConstants;
import ez.game.ezms.server.packet.OptionFunc;
import ez.game.ezms.server.packet.PacketStreamLEReader;
import ez.game.ezms.server.world.command.CommandEngine;
import org.apache.mina.core.session.IoSession;

/**
 * 当收到 0x1F ( = 31) 功能码报文时执行此处理函数。
 * 角色从对话输入栏输入文字说话。
 */
public class Voice implements OptionFunc {

    @Override
    public void process (byte [] message, IoSession session) {
        PacketStreamLEReader reader = new PacketStreamLEReader (message);
        String voice = reader.readMapleASCIIString ();

        String trimed = voice.trim ();
        MapleClient client = (MapleClient) session.getAttribute (ServerConstants.CLIENT_ENTITY_KEY);
        if (client == null) return;

        MapleAccount account = client.getAccountEntity ();

        /* 处理GM命令。GM是无法输入以"@!"开头的对话的。 */
        if (account.getGMlevel () > 0 && trimed.startsWith ("@!")) {
            CommandEngine engine = CommandEngine.getOrInitialize ();
            String [] args = trimed.split ("\\s+");
            boolean success = engine.tryExecute (args, client);
            if (! success) {
                // TODO 报告错误？
            }
            return;
        }

//        MapleRole role = account.getCurrentLoginRole ();
        // TODO 处理广播相关操作。
    }
}
