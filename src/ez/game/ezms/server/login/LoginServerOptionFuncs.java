package ez.game.ezms.server.login;

import ez.game.ezms.server.login.handle.func.*;
import ez.game.ezms.server.packet.OptionFunc;

/**
 * 目前这个部分只是为LoginServer做的接收功能码实现
 * 数组（从客户端发送到服务端的报文中含有功能码，
 * 此处叫做“接收功能码”）.
 */
public final class LoginServerOptionFuncs {

    public final static OptionFunc[] funcs = {
            // 0x00 占位
            Nop.nop,

            // 0x01 用户登录：通过用户名和密码完成登录操作。
            new Login (),

            // 0x02
            Nop.nop,

            // 0x03 用户点击世界服务器：请求世界服务器状态。
            new WorldServerStatusReq (),

            // 0x04 用户点选某个世界服务器的某个频道：请求此世界服务器中的角色列表。
            new RoleListReq(),

            // 0x05 用户点选某个角色，决定开始游玩此角色：请求角色所在世界的服务器IP和端口等。
            new SelectRole (),

            // 0x06
            Nop.nop,

            // 0x07， 检测用户新建的角色名是否合法。
            new CheckRoleName (),

            // 0x08
            Nop.nop,

            // 0x09
            Nop.nop,

            // 0xA
            Nop.nop,

            // 0x0B 用户点击新建角色：请求在此世界服务器创建新角色。
            new CreateRole ()
    };

    public static OptionFunc mapOptionCode2Function (int optCode) {
        if (optCode >= funcs.length)
            return Nop.nop;

        return funcs [optCode];
    }
}
