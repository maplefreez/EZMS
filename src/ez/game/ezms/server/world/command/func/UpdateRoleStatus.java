package ez.game.ezms.server.world.command.func;

import ez.game.ezms.client.MapleClient;
import ez.game.ezms.server.packet.MaplePacket;
import ez.game.ezms.server.packet.WorldServerPacketCreator;
import ez.game.ezms.server.world.command.CommandFunc;
import org.apache.mina.core.session.IoSession;

import java.util.HashMap;

/**
 * 更改当前角色状态。
 * 目前支持状态有：
 *    能力值四项，
 *    金币，
 *    经验，
 *    等级，
 *    人气，
 *    当前HP，
 *    当前MP，
 */
public class UpdateRoleStatus implements CommandFunc {

    private final static String [] statusType = {
        "meso", "luk", "str", "dex", "int",
        "exp", "hp", "mp", "level", "fame"
    };

    public static HashMap <String, SubFunction> subFuncs;

    private static UpdateRoleStatus singleton;

    public static UpdateRoleStatus getOrInitialize () {
        if (singleton == null)
            singleton = new UpdateRoleStatus ();
        return singleton;
    }

    @Override
    public String getCommandName() {
        return "@!stat";
    }

    @Override
    public String getDescription() {
        StringBuilder builder = new StringBuilder (64);
        builder.append (getCommandName ()).append (" <");

        for (String status : statusType)
            builder.append (status).append ("|");
        builder.deleteCharAt (builder.length () - 1);
        builder.append ("> <value>");
        return builder.toString ();
    }

    @Override
    public boolean execute(String[] args, MapleClient client) {
        /* 参数必须是大于等于两个， 严格的是两个，一个是
        * 子函数名称，一个是改变值。  */
        if (args.length >= 2) {
            String subName = args [0];
            String value = args [1];

            /* 会话。 */
            IoSession session = client.getSession ();
            if (session == null) return false;

            SubFunction sub = subFuncs.get (subName);
            if (sub != null) {
                long valLong = 0;
                try {
                    valLong = Long.parseLong (value);
                } catch (Exception ex) {
                    MaplePacket packet = WorldServerPacketCreator.sendMessage (client,
                            "Value format errors!", 5);
                    session.write (packet.getByteArray ());
                    return false;
                }
                return sub.exec (valLong, client); // 执行.
            }
        }
        return false;
    }

    private UpdateRoleStatus () {
        subFuncs = new HashMap <> ();
        subFuncs.put ("meso", new mesoFunc ());

        // TODO...
    }

    /**
     * 冒险币。
     */
    class mesoFunc implements SubFunction {
        @Override
        public boolean exec (long value, MapleClient client) {
            /* 会话。 */
            IoSession session = client.getSession ();
            MaplePacket packet = WorldServerPacketCreator.updateRoleMeso ((int) value);
            session.write (packet.getByteArray ());

            /* 消息告知。 */
            String message = (value > 0 ? "Meso +" : "") + value;
            packet = WorldServerPacketCreator.sendMessage (client, message, 0);
            session.write (packet.getByteArray ());
            return true;
        }
    }

    private interface SubFunction {
        boolean exec (long value, MapleClient client);
    }

}
