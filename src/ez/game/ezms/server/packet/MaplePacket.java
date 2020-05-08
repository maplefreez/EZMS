package ez.game.ezms.server.packet;


import ez.game.ezms.constance.ServerConstants;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * MapleStory报文结构。
 *
 * 2020/4/18 ez开发手记
 *     此报文目前只有一个buffer字段，
 * 未来如果没有更多的字段和特性加入，在下相信
 * 此类还不如取消，直接用PacketStreamLEWriter
 * 代替即可，不再使用MaplePacket结构。
 */
public class MaplePacket {

    private byte [] buffer;
    private static final Charset ASCII = Charset.forName (ServerConstants.MAPLE_LOCATE_ENCODING);

    public byte [] getByteArray () {
        return buffer;
    }

    /**
     * 别从此函数构造，而是调用PacketStreamLEWriter实体的
     * 函数构造一个MaplePacket实例。
     *
     * ez: 所以鄙人才说此类没什么用！
     *
     * @param array
     */
    private MaplePacket (byte [] array) {
        buffer = array;
    }

    /** 作为一个构造MaplePacket实例的操作类，
     * 以流处理的方式对报文进行构造。完成后调用
     * generate()返回此实例。
     *
     * 此类进行的写入对齐格式：基础数据类型为“小端字节序”，
     * 字符串格式遵循MapleStory实现：2BYTE LE长度+字符串实体（无终止符）。
     */
    public static class PacketStreamLEWriter {
        private byte [] buffer;

        /**
         * 当前写入的索引位置，idx从零开始，
         * 范围到buffer.length，但这个最好可以
         * 更改，也即是buffer可以自增长。
         */
        private int idx;

        /**
         * 创建一个流式构造器，用于构造报文。
         *
         * @param size  此是写入流缓冲的一个初始大小，
         *              由调用者传入的估计值，别担心，
         *              缓冲大小在运行中会根据写入数据
         *              情况自扩大，直到2^31-1大小。
         */
        public PacketStreamLEWriter(int size) {
            buffer = new byte [size];
            idx = 0x0;
        }

        /**
         * 此函数将写入一个16bit的整数，这个整数被视为
         * 无符号型。且以小端字节序写入。
         *
         * @param x 欲写入的16bit整数。
         */
        public void writeShort (short x) {
            ensureSpaceOrGrowth (idx + 2);
            buffer [idx ++] = (byte) (x & 0xFF);
            buffer [idx ++] = (byte) ((x >>> 8) & 0xFF);
        }

        /**
         * 此函数写入一个64bit的整数。视为无符号型，
         * 以小端字节序写入。
         * @param x
         */
        public void writeLong (long x) {
            ensureSpaceOrGrowth (idx + 8);
            buffer [idx ++] = ((byte) (x & 0xFF));
            buffer [idx ++] = ((byte) (x >>> 8 & 0xFF));
            buffer [idx ++] = ((byte) (x >>> 16 & 0xFF));
            buffer [idx ++] = ((byte) (x >>> 24 & 0xFF));
            buffer [idx ++] = ((byte) (x >>> 32 & 0xFF));
            buffer [idx ++] = ((byte) (x >>> 40 & 0xFF));
            buffer [idx ++] = ((byte) (x >>> 48 & 0xFF));
            buffer [idx ++] = ((byte) (x >>> 56 & 0xFF));
        }

        /**
         * 反转写入64bit整型数。
         * @param x
         */
        public void writeReversedLong (long x) {
            ensureSpaceOrGrowth (idx + 8);
            buffer [idx ++] = ((byte) (x >>> 32 & 0xFF));
            buffer [idx ++] = ((byte) (x >>> 40 & 0xFF));
            buffer [idx ++] = ((byte) (x >>> 48 & 0xFF));
            buffer [idx ++] = ((byte) (x >>> 56 & 0xFF));
            buffer [idx ++] = ((byte) (x & 0xFF));
            buffer [idx ++] = ((byte) (x >>> 8 & 0xFF));
            buffer [idx ++] = ((byte) (x >>> 16 & 0xFF));
            buffer [idx ++] = ((byte) (x >>> 24 & 0xFF));
        }

        /**
         * 此函数将写入一个byte数组的数据。
         *
         * @param array  欲写入的数组数据。
         */
        public void writeByteArray(byte[] array) {
            ensureSpaceOrGrowth (idx + array.length);
            for (byte x : array)
                buffer [idx ++] = x;
        }

        public void writeByte (byte x) {
            ensureSpaceOrGrowth (idx + 1);
            buffer [idx ++] = x;
        }


        /**
         * 此函数将写入一个32bit的整数，传入参数
         * 将被视为无符号型。且以小端字节序写入。
         *
         * @param x  欲写入的32bit整数。
         */
        public void writeInt (int x) {
            ensureSpaceOrGrowth (idx + 4);
            buffer [idx ++] = ((byte) (x & 0xFF));
            buffer [idx ++] = ((byte) (x >>> 8 & 0xFF));
            buffer [idx ++] = ((byte) (x >>> 16 & 0xFF));
            buffer [idx ++] = ((byte) (x >>> 24 & 0xFF));
        }

        /**
         * 本函数保证数组length <= min，若不能，则增加
         * 常数个单位。此处没有使用指数增长（一般以2为底），
         * 因为通常情况下的报文增长都不会太大。每次增大的
         * 常数为函数体中的groupSize.
         *
         * 若min超过2147483639（2^31 - 9，即非常接近2^31了），
         * 直接将数组大小重新申请为2147483647(2^31-1).
         *
         * 注意：在调用中，min应该是( idx的值+欲写入数据的长度 )，因为
         * 函数是用min与buffer当前长度做比较。
         *
         * @param min  增长后的最小长度，这是一个估计值，由调用者提供，
         *             在后续运行中也许仍然会有内存空间不足的情况，所以
         *             慎重选择此值。
         */
        private void ensureSpaceOrGrowth (int min) {
            /* 保证线性增长即可。 */
            final int groupSize = 0x10;
            int oldCapacity = this.buffer.length;
            if (min >= oldCapacity) {
                int newCapacity = oldCapacity + groupSize;
                if (newCapacity - min < 0) {
                    newCapacity = min;
                }

                if (newCapacity - 2147483639 > 0)
                    newCapacity = 2147483647;

                this.buffer = Arrays.copyOf (this.buffer, newCapacity);
            }
        }

        /**
         * 写入MapleStory格式的字符串，字符串数据格式如下：
         * 2Byte LE的字符串长度 + 字符串数据本体（不带终结符）。
         *
         * @param s  欲写入的字符串数据。
         */
        public void writeMapleStoryASCIIString(String s) {
            if (s == null)
                writeShort ((short) 0);
            else {
                ensureSpaceOrGrowth(s.length() + 2);
                byte[] array = s.getBytes (ASCII);
                writeShort ((short) array.length); // 长度，占2Byte
                writeByteArray(array); // 字符数组。
            }
        }

        /**
         * 写入长度固定的字符串，字符串若大于len，则直接截断。
         * 若字符串长度小于len，剩余部分由0填充。
         * 此函数不会写入字符串的长度，只写入字符串数据。
         *
         * @param s   字符串
         * @param len   固定的长度。
         */
        public void writeFixedLengthASCIIString (String s, int len) {
            if (s.length () > len)
                s = s.substring(0, len);
            writeByteArray (s.getBytes (ASCII));
            for (int i = s.length (); i < len; ++ i)
                writeByte ((byte) 0);
        }

        /**
         * 根据此函数调用前一系列写入操作构造出报文实体。
         *
         * @return  构造出的报文实体。
         */
        public MaplePacket generate () {
            byte [] array = null;
            if (idx > 0)
                array = Arrays.copyOf (buffer, idx);
            else
                /* 在下相信这里会有更好的解决方案。 */
                array = new byte [1];

            return new MaplePacket (array);
        }

    }

}
