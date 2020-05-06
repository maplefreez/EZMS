package ez.game.ezms.server.packet;

import ez.game.ezms.constance.ServerConstants;

import java.io.UnsupportedEncodingException;

public class PacketStreamLEReader {
    private byte [] buffer;
    private int idx;

    public PacketStreamLEReader (byte [] array) {
        this.buffer = array;
        idx = 0;
    }

    public PacketStreamLEReader (MaplePacket packet) {
        this.buffer = packet.getByteArray ();
        idx = 0;
    }

    public void reset () {
        idx ^= idx;
    }

    public byte readByte () {
        return buffer [idx ++];
    }

    public int readInt () {
        /* 在下也不想这么做的，奈何Java中没有无符号型，
        * 鄙人技术水平有限，只能先这么处理，将符号位扩展问题
        * 草草解决。 */
        int res = (buffer [idx ++] << 24) >>> 24;
        res |= (buffer [idx ++] << 24) >>> 16;
        res |= (buffer [idx ++] << 24) >>> 8;
        res |= (buffer [idx ++] << 24);
        return res;
    }

    public short readShort () {
        short res = (short) ((buffer [idx ++] << 8) >>> 8);
        res |= (short) (buffer [idx ++] << 8);
        return res;
    }

    public byte [] readByteArray (int len) {
        int i = 0;
        byte [] array = new byte [len];
        while (i < len) array [i ++] = buffer [idx ++];
        return array;
    }

    public String readMapleASCIIString () {
        int length = readShort ();
        byte [] ret = readByteArray (length);

        try {
            return new String(ret, ServerConstants.MAPLE_LOCATE_ENCODING);
        } catch (UnsupportedEncodingException e) {
            /* Nothing to do... */
        }
        return "";
    }
}
