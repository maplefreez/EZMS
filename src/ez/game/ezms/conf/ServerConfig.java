package ez.game.ezms.conf;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 一个java.util.Properties的包装类。。
 */
public class ServerConfig {

    private Properties properties;

    public ServerConfig (Properties props) {
        properties = props;
    }

    /**
     * 工具方法。
     * @param fileName  配置文件名称。
     * @return
     */
    public static ServerConfig openConfigFile (String fileName) {
        // confFileName指定的配置文件， 加载到此处。
        Properties props = new Properties ();
        FileInputStream fileReader = null;
        ServerConfig conf;

        try {
            fileReader = new FileInputStream (fileName);
            props.load (fileReader);
        } catch (Exception ex) {
            /* 直接退出。 */
            ex.printStackTrace ();
            return null;
        } finally {
            if (fileReader != null)
                try { fileReader.close ();} catch (Exception ex) {}
        }
        conf = new ServerConfig (props);
        return conf;
    }


    public int getInt (String name, int def) {
        String ret = this.properties.getProperty (name);
        if (null == ret) return def;
        try {
            return Integer.parseInt(ret);
        } catch (NumberFormatException ex) {
            // 不做任何记录？ 不然打印一下日志？
        }

        return def;
    }

    public short getShort (String name, short def) {
        String ret = this.properties.getProperty (name);
        if (null == ret) return def;
        try {
            return Short.parseShort (ret);
        } catch (NumberFormatException ex) {
            // 不做任何记录？ 不然打印一下日志？
        }

        return def;
    }

    public long getLong (String name, long def) {
        String ret = this.properties.getProperty (name);
        if (null == ret) return def;
        try {
            return Long.parseLong (ret);
        } catch (NumberFormatException ex) {
            // 不做任何记录？ 不然打印一下日志？
        }

        return def;
    }

    public boolean getBoolean (String name, boolean def) {
        String ret = this.properties.getProperty (name);
        if (null == ret) return def;
        try {
            return Boolean.parseBoolean (ret);
        } catch (NumberFormatException ex) {
            // 不做任何记录？ 不然打印一下日志？
        }

        return def;
    }

    public String getString (String name, String def) {
        return properties.getProperty (name, def);
    }

    /**
     * 查找配置值，若没有则抛出异常。。
     * @param name
     * @return
     * @throws Exception
     */
    public String getString (String name) throws Exception {
        String ret = properties.getProperty (name);
        if (null == ret)
            throw new Exception ("Config key " + name + " has no String value.");
        return ret;
    }

    /**
     * 将a中所有的配置项与当前实例的配置全加载到
     * 一个新的实例，若有键相同则直接覆盖。
     *
     * @param a  欲合并的配置。
     * @return  新的实例。
     */
    public ServerConfig addInto (ServerConfig a) {
        return addInto (a, true);
    }

    /**
     * 将a中所有的配置项全加载到当前实例。
     *
     * @param a  欲合并的配置。
     * @param overwrite  是否覆盖相同的键。若否，则在发现相同键时不会合并。
     * @return  新的实例。
     */
    public ServerConfig addInto (ServerConfig a, boolean overwrite) {
        ServerConfig ret = a.clone ();

        for (Map.Entry <Object, Object> entry : this.properties.entrySet ()) {
            if (ret.hasProperties (entry.getKey ()) && overwrite) {
                ret.setProperties(entry.getKey (), entry.getValue ());
            }
        }
        return ret;
    }

    public ServerConfig addInto (Properties a, boolean overwrite) {
        Properties props = (Properties) a.clone ();
        ServerConfig ret = new ServerConfig (props);

        for (Map.Entry <Object, Object> entry : this.properties.entrySet ()) {
            if (ret.hasProperties (entry.getKey ()) && overwrite) {
                ret.setProperties(entry.getKey (), entry.getValue ());
            }
        }
        return ret;
    }

    public boolean hasProperties (Object k) {
        return this.properties.containsKey (k);
    }

    public void setProperties (Object k, Object v) {
        this.properties.put (k, v);
    }

    @Override
    protected ServerConfig clone () {
        Properties props = (Properties) this.properties.clone ();
        return new ServerConfig (props);
    }

    static String def = "";

}
