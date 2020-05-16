package ez.game.ezms.server.world.command.func;

import ez.game.ezms.client.MapleClient;
import ez.game.ezms.server.packet.MaplePacket;
import ez.game.ezms.server.packet.WorldServerPacketCreator;
import ez.game.ezms.server.world.command.CommandEngine;
import ez.game.ezms.server.world.command.CommandFunc;
import org.apache.mina.core.session.IoSession;

import java.util.Iterator;

/**
 * 根据权限打印所有可执行的命令。
 */
public class ListCommands implements CommandFunc {

    @Override
    public String getDescription () {
        return getCommandName ();
    }

    @Override
    public String getCommandName () {
        return "@!lscmd";
    }

    @Override
    public boolean execute (String [] args, MapleClient client) {
        StringBuilder builder = new StringBuilder ();
        Iterator <CommandFunc> allFuncs = CommandEngine.getOrInitialize ().getCommandList ();
        while (allFuncs.hasNext ()) {
            CommandFunc func = allFuncs.next ();
            builder.append (func.getDescription ());
            builder.append (';');
        }

        IoSession session = client.getSession ();
        if (session == null) return false;

        MaplePacket packet = WorldServerPacketCreator.sendMessage (client, builder.toString (), 5);
        session.write (packet.getByteArray ());

        return true;
    }

}
