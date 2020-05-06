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

//import client.MapleClient;
//import constants.GameConstants;
//import constants.ServerConstants;
//import handling.RecvPacketOpcode;
import java.nio.ByteBuffer;

import ez.game.ezms.tools.MapleAESOFB;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
//import ez.game.ezms.tools.MapleAESOFB;
//import ez.game.ezms.tools.tools.MapleCustomEncryption;

import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import ez.game.ezms.tools.tools.tools.FileoutputUtil;
//import ez.game.ezms.tools.tools.tools.HexTool;
//import ez.game.ezms.tools.tools.data.input.ByteArrayByteStream;
//import ez.game.ezms.tools.tools.data.input.GenericLittleEndianAccessor;

public class MaplePacketDecoder extends CumulativeProtocolDecoder {

    public static final String DECODER_STATE_KEY = MaplePacketDecoder.class.getName() + ".STATE";
    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        int packetHeader = in.getInt(); // 另外一种方式，getShort() xor getShort()
        int packetLen = MapleAESOFB.getPacketLength (packetHeader);
        if (packetLen <= 0) return true;

        // 报文已经去掉了头部，getInt()调用将去掉头部。
        byte [] buffer = new byte [packetLen];

        in.get(buffer, 0, packetLen);
//        decoderState.packetlength = -1;
//        client.getReceiveCrypto().crypt(decryptedPacket);
        out.write(buffer);
        return true;
    }

    private String lookupSend(int val) {
//        for (RecvPacketOpcode op : RecvPacketOpcode.values()) {
//            if (op.getValue() == val) {
//                return op.name();
//            }
//        }
        return "UNKNOWN";
    }

    private int readFirstByte(byte[] arr) {
//        return new GenericLittleEndianAccessor(new ByteArrayByteStream(arr)).readByte();
        return 0;
    }

    public static class DecoderState {

        public int packetlength = -1;
    }
}
