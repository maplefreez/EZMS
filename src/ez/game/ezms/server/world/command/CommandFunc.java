package ez.game.ezms.server.world.command;

import ez.game.ezms.client.MapleClient;

public interface CommandFunc {

    /**
     * 返回此命令的名称字符串。
     * 根据此字符串区分不同的命令。
     * 角色发送命令也以此识别。
     *
     * 注意：
     *  请使用小写英文给命令命名！
     *  目前命令名称格式： @! + <name>， 感叹号是英文感叹号。
     *
     * @return
     */
    String getCommandName ();

    /**
     * 执行此命令时发生的动作。
     *
     * @param args  命令行参数。若不需要参数可以直接传入null。
     * @param client
     * @return
     */
    boolean execute (String [] args, MapleClient client);
}
