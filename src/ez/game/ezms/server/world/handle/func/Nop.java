package ez.game.ezms.server.world.handle.func;

import ez.game.ezms.server.packet.OptionFunc;
import org.apache.mina.core.session.IoSession;

/**
 * 作为哑元， 此操作码实现仅仅用作
 * 占位使用。
 */
public class Nop implements OptionFunc {
    public static final Nop nop = new Nop();

    /* 不准在外部随便使用，只准使用哑元。 */
    private Nop() {}

    @Override
    public void process (byte [] message, IoSession session) {
        /* 什么都不做。 */
    }
}
