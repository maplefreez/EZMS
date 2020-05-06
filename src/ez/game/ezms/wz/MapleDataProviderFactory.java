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
package ez.game.ezms.wz;

import ez.game.ezms.conf.ServerConfig;
import ez.game.ezms.wz.model.XMLWZFile;

import java.io.File;

/**
 * 经过更改，此类目前只支持加载xml格式的WZ数据。
 */
public class MapleDataProviderFactory {

    /**
     * WZ文件（XML格式）的存储路径。
     */
    private static String wzPath;

    private static MapleDataProvider getWZ (Object in) throws IllegalArgumentException {
        if (in instanceof File) {
            File wzFile = (File) in;
            return new XMLWZFile (wzFile);
        }
        throw new IllegalArgumentException ("Can't create data provider for input " + in);
    }

    public static MapleDataProvider getDataProvider (Object in) {
        return getWZ (in);
    }

    public static MapleDataProvider getDataProvider (String fileName) {
        File file = new File (wzPath, fileName);
        return getDataProvider (file);
    }

    /**
     * 初始化数据工厂及各个数据接口。为缓存
     * WZ数据作准备。
     *
     * @param config  配置信息类实例。
     * @throws Exception
     */
    public static void initialize (ServerConfig config) throws Exception {
        wzPath = config.getString ("server.wzpath");
        MapleWZDataCache.initializeDataInterfaces ();
    }

    public static File fileInWZPath (String filename) {
        return new File(wzPath, filename);
    }
}
