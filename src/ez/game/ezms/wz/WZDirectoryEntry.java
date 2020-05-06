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
package ez.game.ezms.wz;

import ez.game.ezms.wz.*;

import java.util.*;

public class WZDirectoryEntry extends WZEntry implements MapleDataDirectoryEntry {

    private final List<MapleDataDirectoryEntry> subDirs = new ArrayList<>();
    private final List<MapleDataFileEntry> files = new ArrayList<>();
    private final Map<String, MapleDataEntry> entries = new HashMap<>();

    public WZDirectoryEntry (String name, int size, int checksum, MapleDataEntity parent) {
        super(name, size, checksum, parent);
    }

    public WZDirectoryEntry() {
        super(null, 0, 0, null);
    }

    public void addDirectory(MapleDataDirectoryEntry dir) {
        subDirs.add(dir);
        entries.put(dir.getName(), dir);
    }

    public void addFile(MapleDataFileEntry fileEntry) {
        files.add(fileEntry);
        entries.put(fileEntry.getName(), fileEntry);
    }

    @Override
    public List<MapleDataDirectoryEntry> getSubdirectories() {
        return Collections.unmodifiableList(subDirs);
    }


    /**
     * 通过子目录名得到目录实体。若找不到返回null
     * @param dirName  目录名称
     * @return
     */
    @Override
    public MapleDataDirectoryEntry getSubDirByName (String dirName) {
        for (MapleDataDirectoryEntry dir : subDirs) {
            if (dir.getName().contains (dirName)) return dir;
        }
        return null;
    }

    @Override
    public List<MapleDataFileEntry> getFiles() {
        return Collections.unmodifiableList(files);
    }

    @Override
    public MapleDataEntry getEntry(String name) {
        return entries.get(name);
    }
}
