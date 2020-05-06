/**
 * Author : ez
 * Date : 2019/10/23
 * Description : ChannelServer实现。
 */

package ez.game.ezms.server.channel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 世界线实体，一个世界服务器中有很多平行
 * 的频道，此作为一个频道的实体。按理说，
 * 每个频道的各种游戏数据都应该是在这个实体
 * 中进行刷新，更新，加载...
 *
 * 所以，这个类应该设计成各种结构的一个容器。
 */
public class WorldChannel {

    /**
     * 在此频道的玩家数。
     */
    private AtomicInteger loginRoleCount;

    public WorldChannel () {
        loginRoleCount = new AtomicInteger (0);
    }

    public int getLoginRoleCount () {
        return loginRoleCount.get ();
    }

}
