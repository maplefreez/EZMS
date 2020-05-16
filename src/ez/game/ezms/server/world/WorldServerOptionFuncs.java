package ez.game.ezms.server.world;

import ez.game.ezms.server.packet.OptionFunc;
import ez.game.ezms.server.world.handle.func.*;

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
            new RoleLogin (),

            Nop.nop,  // 7
            Nop.nop,  // 8
            Nop.nop,  // 9
            Nop.nop,  // 10
            Nop.nop,  // 11
            Nop.nop,  // 12
            Nop.nop,  // 13
            Nop.nop,  // 14
            Nop.nop,  // 15
            Nop.nop,  // 16
            Nop.nop,  // 17
            Nop.nop,  // 18
            Nop.nop,  // 19
            Nop.nop,  // 20

            /* 0x15  角色进入地图，最常见的场景为“进入传送口”。
             * 另外，回城、出租车、NPC对话等都会触发这个事件。
             */
            new EnterMap (),
            Nop.nop,  // 22
            Nop.nop,  // 23

            /* 0x18  角色、NPC及怪物移动的处理。
             */
            new LifeMove (),

            Nop.nop,  // 25
            Nop.nop,  // 26
            Nop.nop,  // 27
            Nop.nop,  // 28
            Nop.nop,  // 29
            Nop.nop,  // 30

            /* 0x1F ( = 31)  角色说话。通过对话输入栏说话。
             * 此处也处理来自GM角色的命令。
             */
            new Voice ()
    };

    public static OptionFunc mapOptionCode2Function (int optCode) {
        if (optCode >= funcs.length)
            return Nop.nop;
        return funcs [optCode];
    }
}
