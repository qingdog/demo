package com.wm.file.util.easypoi;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCDruidUtils {

    /**
     * 关闭conn,和 statement独对象资源
     *
     * @param connection
     * @param statement
     * @MethodName: close
     * @return: void
     */
    public static void close(Connection connection, Statement statement) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭 conn , statement 和resultset三个对象资源
     *
     * @param connection
     * @param statement
     * @param resultSet
     * @MethodName: close
     * @return: void
     */
    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        close(connection, statement);
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
