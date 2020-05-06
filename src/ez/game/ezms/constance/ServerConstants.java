package ez.game.ezms.constance;

public class ServerConstants {

    public static final boolean PollEnabled = false;
    public static final String Poll_Question = "Are you mudkiz?";
    public static final String[] Poll_Answers = {"test1", "test2", "test3"};
    // End of Poll

    /**
     * 冒险岛版本，
     * 若是0.79，则填入79；
     * 若是0.27，则写入27.
     */
    public static final short MAPLE_VERSION = 27;
    public static final String MAPLE_PATCH = "1";

    /**
     * 此键用于存储client实例，每个Client实例保存在session中，
     * 暂且在此处定义这个键值。
     */
    public final static String CLIENT_ENTITY_KEY = "ClientEntity";

    /** 与服务器所运行的语言区域有关，也与字符编码有关，4为中国，编码是GB18030
     *  此变量过去命名为“MAPLE_TYPE”
     */
    public static byte MAPLE_LOCATECODE = 4;
    /**
     * 服务器运行区域的字符集编码，简体中文是GB18030.
     */
    public static String MAPLE_LOCATE_ENCODING = "GB18030";

    /**
     * 这个项貌似是控制加密相关的一组salt，每个登录的人
     * 应该有自己的salt，目前在调试阶段，我直接使用固定的salt
     * 了，后续最好将false状态实现。控制的位置在LoginHandler中的
     * SessionOpened()事件。
     */
    public static final boolean Use_Fixed_IV = true;

    public static final int MIN_MTS = 110;
    public static final int MTS_BASE = 100; //+1000 to everything in MSEA but cash is costly here
    public static final int MTS_TAX = 10; //+% to everything
    public static final int MTS_MESO = 5000; //mesos needed
    public static final int CHANNEL_COUNT = 200;
    //服务端输出操作
//    public static boolean 封包显示 = Boolean.parseBoolean(ServerProperties.getProperty("MinaMS.封包显示", "false"));
//    public static boolean 调试输出封包 = Boolean.parseBoolean(ServerProperties.getProperty("MinaMS.调试输出封包", "false"));
//    public static boolean 自动注册 = false;
    public static boolean Super_password = false;
    public static String superpw = "";
//    public static final List<Balloon> lBalloon = new ArrayList();
    public static boolean 防卡号 = true;

//    public static boolean getAutoReg() {
//        return 自动注册;
//    }

//    public static String ChangeAutoReg() {
//        自动注册 = !getAutoReg();
//        return 自动注册 ? "开启" : "关闭";
//    }

//    public static List<Balloon> getBalloons() {
//        if (lBalloon.isEmpty()) {
//            lBalloon.add(new Balloon("[MinaMS服务端]致力于打造最完善的079", 236, 122));
//            lBalloon.add(new Balloon("购买请联系QQ：529926174", 0, 276));
//        }
//        return lBalloon;
//    }

    public static final byte Class_Bonus_EXP(final int job) {
        switch (job) {
            case 3000: //whenever these arrive, they'll give bonus
            case 3200:
            case 3210:
            case 3211:
            case 3212:
            case 3300:
            case 3310:
            case 3311:
            case 3312:
            case 3500:
            case 3510:
            case 3511:
            case 3512:
                return 10;
        }
        return 0;
    }

    public static enum PlayerGMRank {

        NORMAL('@', 0),
        INTERN('!', 1),
        GM('!', 2),
        ADMIN('!', 3);
        //SUPERADMIN('!', 3);
        private char commandPrefix;
        private int level;

        private PlayerGMRank(char ch, int level) {
            commandPrefix = ch;
            this.level = level;
        }

        public char getCommandPrefix() {
            return commandPrefix;
        }

        public int getLevel() {
            return level;
        }
    }

    public static enum CommandType {

        NORMAL(0),
        TRADE(1);
        private int level;

        private CommandType(int level) {
            this.level = level;
        }

        public int getType() {
            return level;
        }
    }
}
