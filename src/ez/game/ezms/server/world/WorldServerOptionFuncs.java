package ez.game.ezms.server.world;

import ez.game.ezms.server.packet.OptionFunc;
import ez.game.ezms.server.world.handle.func.Nop;
import ez.game.ezms.server.world.handle.func.RoleLogin;

public class WorldServerOptionFuncs {

    final static OptionFunc[] funcs = {
            Nop.nop, // 0
            Nop.nop, // 1
            Nop.nop, // 2
            Nop.nop, // 3
            Nop.nop, // 4
            Nop.nop, // 5

            /* 0x06 角色登录世界服务器，在用户点选某个
             * 角色并成功与世界服务器握手后，会发送0x06功能码
             * 的报文，请求进入世界服务器，客户端尝试进入游戏地图。 */
            new RoleLogin ()
    };

    public static OptionFunc mapOptionCode2Function (int optCode) {
        if (optCode >= funcs.length)
            return Nop.nop;
        return funcs [optCode];
    }
}
