package ez.game.ezms.server.packet;

import org.apache.mina.core.session.IoSession;

public interface OptionFunc {
    void process (byte [] message, IoSession session);
}
