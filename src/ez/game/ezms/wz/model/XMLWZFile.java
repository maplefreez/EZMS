/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
		       Matthias Butz <matze@odinms.de>
		       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package ez.game.ezms.wz.model;

//import ez.game.ezms.wz.model.raw.WZDirectoryEntry;
//import ez.game.ezms.wz.model.raw.WZFileEntry;
import ez.game.ezms.wz.WZDirectoryEntry;
import ez.game.ezms.wz.MapleData;
import ez.game.ezms.wz.MapleDataDirectoryEntry;
import ez.game.ezms.wz.MapleDataProvider;
import ez.game.ezms.wz.WZFileEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * XML格式的WZ文件实例。
 */
public class XMLWZFile implements MapleDataProvider {
    private final File file;
    private final WZDirectoryEntry rootForNavigation;

    /**
     * 以文件初始化。
     *
     * @param fileIn
     */
    public XMLWZFile (File fileIn) {
        file = fileIn;
        rootForNavigation = new WZDirectoryEntry (fileIn.getName(), 0, 0, null);
        fillMapleDataEntities(file, rootForNavigation);
    }

    private void fillMapleDataEntities (File fileEntity, WZDirectoryEntry wzdir) {
        for (File file : fileEntity.listFiles()) {
            String fileName = file.getName ();
            /* 若不是目录且文件扩展名为*.img */
            if (file.isDirectory () && !fileName.endsWith (".img")) {
                WZDirectoryEntry newDir = new WZDirectoryEntry (fileName, 0, 0, wzdir);
                wzdir.addDirectory (newDir);
                fillMapleDataEntities(file, newDir);
            } else if (fileName.endsWith(".xml")) {
                wzdir.addFile (new WZFileEntry (fileName.substring(0, fileName.length() - 4), 0, 0, wzdir));
            }
        }
    }

    @Override
    public MapleData getData(String path) {
        File dataFile = new File(file, path + ".xml");
        File imageDataDir = new File(file, path);
        if (!dataFile.exists()) {
            return null;//bitches
        }
        FileInputStream fis;
        try {
            fis = new FileInputStream(dataFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Datafile " + path + " does not exist in " + file.getAbsolutePath());
        }
        final XMLDomMapleData domMapleData;
        try {
            domMapleData = new XMLDomMapleData (fis, imageDataDir.getParentFile());
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return domMapleData;
    }

    @Override
    public MapleDataDirectoryEntry getFile() {
        return rootForNavigation;
    }
}
