/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ez.game.ezms.mina;

import ez.game.ezms.constance.ServerConstants;
import ez.game.ezms.server.client.MapleClient;
//import ez.game.ezms.server.login.LoginServer;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaplePacketEncoder implements ProtocolEncoder {

    private static Logger log = LoggerFactory.getLogger(MaplePacketEncoder.class);

    /**
     * 这里本来是做一些加密算法的，但是
     * 0.27版本的客户端并没有什么加密算法，
     * 所以此处没有做任何处理，直接将消息flush()
     * 出去。
     *
     * @param session  mina的session对象，对话。
     * @param message  欲发送给session的消息主体，是一个byte[]
     * @param out
     * @throws Exception
     */
    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        MapleClient client = (MapleClient) session.getAttribute (ServerConstants.CLIENT_ENTITY_KEY);

        /** 当客户端进入到登录页面后，所有的交互报文都需要进行加密处理：
         * 处理格式大概是：4Byte AES签名 + 原始报文
         * 但是当客户端仍然没有进入登录页时，报文不进行加密处理。
         *
         * 总结来说：进入登录页面时，已经完成了握手过程。所以必须使用加密。
         */
        if (client != null && client.getHasShakeHand()) {
            byte[] input = (byte[]) message;
            byte[] unencrypted = new byte [input.length];
            System.arraycopy(input, 0, unencrypted, 0, input.length);
            byte[] ret = new byte [unencrypted.length + 4];

            try {
                byte[] header = client.getSendCrypto ().getPacketHeader (unencrypted.length);
                client.getSendCrypto().crypt(unencrypted);
                System.arraycopy (header, 0, ret, 0, 4);
            } finally {
                // 什么都不作。
            }
            System.arraycopy(unencrypted, 0, ret, 4, unencrypted.length);
            out.write (IoBuffer.wrap (ret));
        } else {
            // 若仍然没有完成握手，则什么加密都不做，直接发送原始报文。
            // 目前只有握手时才会来到这个部分。
            // LoginServer可以直接设置握手标志位，但WorldServer设置不了，
            // 因为握手结束时还拿不到客户端实体。所以WorldServer不设置。
            if (client != null) client.setHasShakeHand (true);
            out.write(IoBuffer.wrap ((byte[]) message));
        }
    }

    @Override
    public void dispose(IoSession session) throws Exception {
    }

//    private String lookupRecv(int val) {
//        for (SendPacketOpcode op : SendPacketOpcode.values()) {
//            if (op.getValue(false) == val) {
//                return op.name();
//            }
//        }
//        return "UNKNOWN";
//    }
//
//    private int readFirstByte(byte[] arr) {
//        return new GenericLittleEndianAccessor(new ByteArrayByteStream(arr)).readByte();
//    }
}
