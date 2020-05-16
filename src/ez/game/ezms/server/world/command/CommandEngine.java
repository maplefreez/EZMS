package ez.game.ezms.server.world.command;

import ez.game.ezms.client.MapleClient;
import ez.game.ezms.server.world.command.func.EnterSpecifiedMap;
import ez.game.ezms.server.world.command.func.ListCommands;

import java.util.*;

/**
 * 命令引擎，GM命令都来自于此。
 */
public final class CommandEngine {
    private HashMap<String, CommandFunc> funcs;

    /**
     * 引擎单例。
     */
    private static CommandEngine engineSingleton;

    /**
     * 只能通过此函数得到CommandEngine实例。
     *
     * @return 命令引擎实例。
     */
    public static CommandEngine getOrInitialize () {
        if (engineSingleton != null) return engineSingleton;
        return initialize ();
    }


    /**
     * 尝试执行命令，若命令不存在，则返回false，且什么
     * 都不做。
     *
     * @param args 命令参数（包括命令name和命令携带的参数。）
     *
     * @return
     */
    public boolean tryExecute (String [] args, MapleClient client) {
        if (args.length >= 1) {
            String cmdName = args [0].trim ().toLowerCase ();
            CommandFunc func = this.funcs.get (cmdName);
            if (func != null) {
                /* 输入参数部分处理。 */
                String [] parameters = null;
                if (args.length > 1) {
                    parameters = new String [args.length - 1];
                    System.arraycopy (args, 1, parameters, 0, parameters.length);
                }
                /* 执行 */
                return func.execute (parameters, client);
            }
        }

        return false;
    }

    public Iterator <CommandFunc> getCommandList () {
        return this.funcs.values().iterator ();
    }

    private static CommandEngine initialize () {
        engineSingleton = new CommandEngine ();

        /* 注册命令。 */
        engineSingleton.registerCommand (new EnterSpecifiedMap ());
        engineSingleton.registerCommand (new ListCommands ());

        return engineSingleton;
    }

    private CommandEngine () {
        this.funcs = new HashMap <> (0x20);
    }

    private void registerCommand (CommandFunc func) {
        this.funcs.put (func.getCommandName(), func);
    }
}
