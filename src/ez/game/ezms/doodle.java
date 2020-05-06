package ez.game.ezms;

import ez.game.ezms.conf.ServerConfig;
import ez.game.ezms.wz.*;

import java.util.Arrays;
import java.util.List;

public class doodle {
    public static int readInt (byte [] buffer) {
        int idx = 0;
        int res = 0;
        res |= (buffer [idx ++] << 24) >>> 24;
        res |= (buffer [idx ++] << 24) >>> 16;
        res |= (buffer [idx ++] << 24) >>> 8;
        res |= (buffer [idx ++] << 24);
        return res;
    }

    public static byte [] getIPString2ByteArray (String IP) {
        String [] eachSeg = IP.split ("\\.");
        byte [] array = new byte [4];
        int i = 0;

        while (i < eachSeg.length) {
            array [i] = (byte) Short.parseShort (eachSeg [i]);
            ++ i;
        }
        return array;
    }

    public static void main (String [] args) {
//        { // readInt ()
//            byte[] buffer = new byte[]{
//                    (byte) 0x82, (byte) 0xde, (byte) 0x0f, (byte) 0x00
//            };
//            int res = readInt(buffer);
//            System.out.println(res);
//        }


//        {
//            byte [] array = getIPString2ByteArray ("254.0.0.1");
//            System.out.println (array [0]);
//        }

//        {
//            ServerConfig conf = ServerConfig.openConfigFile ("srvconfig.conf");
//            MapleDataProvider provider;
//            try {
//                MapleDataProviderFactory.initialize(conf);
//                provider = MapleDataProviderFactory.getDataProvider ("Character.wz/Weapon/");
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                return;
//            }
//
//            // ./WZData/Item.wz/...
//            MapleDataDirectoryEntry entry = provider.getFile ();
//            // Consume, Etc, Install, Pet, Special...
//            List <MapleDataFileEntry> files = entry.getFiles ();
//                for (MapleDataFileEntry file : files) {
//                    MapleData xmlDom = provider.getData (file.getName ());
//                    MapleData weaponInfo = xmlDom.getChildByPath("info/reqLevel");
//                    if (weaponInfo == null) {
//                        System.out.println ("Cannot open " + file.getName ());
//                        continue;
//                    }
//                    System.out.println (file.getName () + ": RequestLevel = " + MapleDataTool.getInt(weaponInfo));
//                }
//            }

//            {
//                List<MapleDataDirectoryEntry> subDirectories = entry.getSubdirectories();
//                for (MapleDataDirectoryEntry subDir : subDirectories) {
//                    // 每个在/WZData/Item.wz/*/下的文件.
//                    List<MapleDataFileEntry> group = subDir.getFiles();
//                    for (MapleDataFileEntry file : group) {
//                        // 每个文件做成一个data.
//                        final MapleData iz = provider.getData(subDir.getName() + "/" + file.getName());
//                        List<MapleData> items = iz.getChildren();
//                        for (MapleData item : items) {
//                            System.out.println(item.getName());
//                            System.out.println(item.getType());
//                        }
//                    }
//                }
//            }

//        {
//            short x = (short) 0xFFFF;
//            System.out.println (x + 1);
//            System.out.println (x);
//        }

        {
            point [] points = new point [12];
            point [] req = Arrays.copyOf (points, points.length);
//            List <point> plist = List.of (points);
            System.out.println (req.length);
        }

    }

    class point {
        int x; int y;
    }

}
