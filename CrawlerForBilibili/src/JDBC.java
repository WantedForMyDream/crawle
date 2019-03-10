import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class JDBC {

    //通过jdbc连接数据库
    private static Connection getConn() {

        String driver = "com.mysql.cj.jdbc.Driver";
        //下面的变量设置根据自己的情况
        String url = "jdbc:mysql://localhost:3306/user?useUnicode=true&characterEncoding=UTF8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8"; //数据库是webone
        String username = "root";
        String password = "190516";

        Connection conn = null;

        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }


    static void insert( String stringname,List<String> list1 ,List<String> list2,String str1,String str2) throws SQLException {

        Connection conn = getConn();
        conn.setAutoCommit(false);
        int i = 0;

        //往数据库插入信息
        String sql = "insert into "+stringname+ "("+str1+","+str2+") values(?,?)";
        PreparedStatement pstmt=null;

        try {

            //表示预编译的sql对象
            pstmt = (PreparedStatement) conn.prepareStatement(sql);

            for (int j = 0; j < list1.size(); j++) {
                pstmt.setString(1, list1.get(j));
                pstmt.setString(2, list2.get(j));
                i = pstmt.executeUpdate();
            }
            pstmt.close();
            conn.commit();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void insert( String stringname,String s1,String s2,String str1,String str2) throws SQLException {

        Connection conn = getConn();
        conn.setAutoCommit(false);
        int i = 0;

        //往数据库插入信息
        String sql = "insert into "+ stringname +"("+s1+","+s2+") values(?,?)";
        PreparedStatement pstmt=null;

        try {

            //表示预编译的sql对象
            pstmt = (PreparedStatement) conn.prepareStatement(sql);

            pstmt.setString(1, str1);
            pstmt.setString(2, str2);
            i = pstmt.executeUpdate();
            pstmt.close();
            conn.commit();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
