package controller.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetLoginId {
    public static int getIdOfLogin(String login){
        Connection c = TableCreator.getConnection();
        String getLoginId = "select id from users where login = ?";
        PreparedStatement logstmt;
        try {
            logstmt = c.prepareStatement(getLoginId);
            logstmt.setString(1, login);
            ResultSet res = logstmt.executeQuery();
            res.next();
            return res.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

//    public static String getLoginOfId(String login){
//        Connection c = TableCreator.getConnection();
//        String getLoginId = "select id from users where login = ?";
//        PreparedStatement logstmt;
//        try {
//            logstmt = c.prepareStatement(getLoginId);
//            logstmt.setString(1, login);
//            ResultSet res = logstmt.executeQuery();
//            return res.getInt(1);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
}
