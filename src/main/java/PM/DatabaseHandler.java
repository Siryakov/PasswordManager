package PM;
import java.sql.*;

public class DatabaseHandler extends Configs{
    Connection dbConnection;

    public Connection getDbConnection()
        throws ClassNotFoundException, SQLException{
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }
    public void signUser(User user) {
        String insert = "INSERT INTO " + Const.USER_TABLE + " (" +
                Const.USER_LOGIN + ", " + Const.USER_PASSWORD + ") " +
                "VALUES (?, ?)";
        try {
            Connection connection = getDbConnection();
            if (connection != null) {
                PreparedStatement prSt = connection.prepareStatement(insert);
                prSt.setString(1, user.getEmail());
                prSt.setString(2, user.getPassword());
                prSt.executeUpdate();
            } else {
                System.out.println("Failed to establish database connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

//    public ResultSet getUser(User user){ // Ищем пользовотеля , который записан в нашу бд
//        ResultSet resultSet =null;
//
//        String select = " SELECT * FROM " + Const.USER_TABLE + " WHERE " + Const.USER_LOGIN + " =? AND " + Const.USER_PASSWORD + " +=? " ;
//
//        try {
//            Connection connection = getDbConnection();
//            if (connection != null) {
//                PreparedStatement prSt = connection.prepareStatement(select);
//                prSt.setString(1, user.getEmail());
//                prSt.setString(2, user.getPassword());
//                resultSet = prSt.executeQuery();
//            } else {
//                System.out.println("Failed to establish database connection.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return resultSet;
//    }
public ResultSet getUser(User user) {
    ResultSet resultSet = null;

    String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " + Const.USER_LOGIN + " = ? AND " + Const.USER_PASSWORD + " = ?";

    try {
        Connection connection = getDbConnection();
        if (connection != null) {
            PreparedStatement prSt = connection.prepareStatement(select);
            prSt.setString(1, user.getEmail());
            prSt.setString(2, user.getPassword());
            resultSet = prSt.executeQuery();
        } else {
            System.out.println("Failed to establish database connection.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }

    return resultSet;
}


}

