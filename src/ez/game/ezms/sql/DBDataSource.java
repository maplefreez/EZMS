package ez.game.ezms.sql;


import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 数据库连接池，并且提供最基本的
 * 数据库语句执行函数。
 */
public final class DBDataSource {

    private DataSource ds;

    /* 单例模式 */
    private static DBDataSource dataSource;

    private DBDataSource () {
        initializeDBDataSource ();
    }

    public static DBDataSource getOrInitializeDBDataSource () {
        if (dataSource == null)
            dataSource = new DBDataSource ();
        return dataSource;
    }

    /**
     * 此函数最有可能在服务器关闭时调用。
     * 鄙人认为此处若出问题，多半是仍有数据库还有
     * 工作没有完成等等。
     */
    public void closeDBDataSource () {
        if (ds == null) return;
        try {
            ((BasicDataSource) ds).close();
        } catch (SQLException ex) {
            ex.printStackTrace ();
        }
    }

    /**
     * 加载数据库配置文件，初始化连接池。
     * @return 初始化好的数据库连接池。
     */
    private DataSource initializeDBDataSource () {
        Properties props = new Properties ();

        /* 加载配置文件 */
        try {
            FileInputStream fileReader = new FileInputStream ("database.properties");
            props.load (fileReader);
            fileReader.close ();
        } catch (IOException ex) {
            System.out.println (ex.getMessage ());
            return null;
        }

        /* 初始化连接池。 使用的是Apache common-DBCP2 */
        try {
            ds = BasicDataSourceFactory.createDataSource(props);
        } catch (Exception ex) {
            ex.printStackTrace ();
            return null;
        }

        return ds;
    }

    /**
     * 获取一个链接。
     * @return
     */
    public Connection getConnection () {
        Connection conn = null;
        try {
            conn = ds.getConnection ();
        } catch (Exception ex) {
            ex.printStackTrace ();
        }
        return conn;
    }

    /**
     * 也许不调用此方法更好。
     * @param conn
     */
    public void releaseConnection (Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 获取SQL语句类。
     * @return
     */
    public Statement getStatement () {
        Connection conn = getConnection ();
        Statement stmt = null;
        if (conn != null) {
            try {
                stmt = conn.createStatement ();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

//    public static void main (String [] args) {
//        int i = 0;
//        javax.sql.DataSource ds = initializeDataSource ();
//        BasicDataSource bds = (BasicDataSource) ds;
//
//        /* use as JDBC */
//        Connection conn = null;
//        Statement stmt = null;
//        ResultSet rset = null;
//
//            try {
//                conn = ds.getConnection();
//                stmt = conn.createStatement();
//                rset = stmt.executeQuery("select * from accounts");
//                while (rset.next()) {
//                    int id = rset.getInt(1);
//                    System.out.print(id);
//                }
//            } catch (SQLException ex) {
//                System.err.println(ex.getMessage());
//            } finally {
//                try {
//                    if (rset != null) rset.close();
//                } catch (Exception ex) {
//                }
//                try {
//                    if (stmt != null) stmt.close();
//                } catch (Exception ex) {
//                }
//                try {
//                    if (conn != null) conn.close();
//                } catch (Exception ex) {
//                }
//            }
//        }

}
