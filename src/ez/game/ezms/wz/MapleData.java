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

import java.util.List;


/**
 * 从WZ加载的数据，每个数据项貌似用这个盛放。
 * 由于是树形结构（XML），所以应该属于节点信息。
 */
public interface MapleData extends MapleDataEntity, Iterable<MapleData> {

    @Override
    String getName();

    MapleDataType getType();

    List<MapleData> getChildren();

    MapleData getChildByPath(String path);

    Object getData();
}
