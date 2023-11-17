import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class Connect
 */
@WebServlet("/QuoteResponseDAO")
public class QuoteResponseDAO {
    private static final long serialVersionUID = 1L;
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public QuoteResponseDAO() {
    }

    /**
     * @see HttpServlet#HttpServlet()
     */
    protected void connect_func() throws SQLException {
        //uses default connection to the database
        if (connect == null || connect.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            connect = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/pro?allowPublicKeyRetrieval=true&useSSL=false&user=john&password=Pass1550");
            System.out.println(connect);
        }
    }

    public boolean database_login(String email, String password) throws SQLException {
        try {
            connect_func("root", "pass1234");
            String sql = "select * from QuoteResponse where ResponseID = ?";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("failed login");
            return false;
        }
    }

    //connect to the database
    public void connect_func(String username, String password) throws SQLException {
        if (connect == null || connect.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            connect = (Connection) DriverManager
                    .getConnection("jdbc:mysql://127.0.0.1:3306/pro?"
                            + "useSSL=false&user=" + username + "&password=" + password);
            System.out.println(connect);
        }
    }

    public List<QuoteResponse> listAllResponses() throws SQLException {
        List<QuoteResponse> listResponses = new ArrayList<QuoteResponse>();
        String sql = "SELECT * FROM QuoteResponse";
        connect_func();
        statement = (Statement) connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int responseID = resultSet.getInt("ResponseID");
            int requestID = resultSet.getInt("RequestID");
            int clientID = resultSet.getInt("ClientID");
            String responseDate = resultSet.getString("ResponseDate");
            double price = resultSet.getDouble("Price");
            String workPeriodFrom = resultSet.getString("WorkPeriodFrom");
            String workPeriodTo = resultSet.getString("WorkPeriodTo");
            String note = resultSet.getString("Note");


            QuoteResponse response = new QuoteResponse (responseID, requestID, clientID, responseDate, price, workPeriodFrom, workPeriodTo, note);
            listResponses.add(response);
        }
        resultSet.close();
        disconnect();
        return listResponses;
    }

    protected void disconnect() throws SQLException {
        if (connect != null && !connect.isClosed()) {
            connect.close();
        }
    }

    public void insert(QuoteResponse response) throws SQLException {
        connect_func("root", "pass1234");
        String sql = "INSERT INTO QuoteResponse (RequestID, ClientID, ResponseDate, Price, WorkPeriodFrom, WorkPeriodTo, Note) VALUES (?, ?, ?, ?, ?, ?, ?)";
        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);

        preparedStatement.setInt(1, response.getRequestID());
        preparedStatement.setInt(2, response.getClientID());
        preparedStatement.setString(3, response.getResponseDate());
        preparedStatement.setDouble(4, response.getPrice());
        preparedStatement.setString(5, response.getWorkPeriodFrom());
        preparedStatement.setString(6, response.getWorkPeriodTo());
        preparedStatement.setString(7, response.getNote());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        disconnect();
    }

    public boolean delete(int ResponseID) throws SQLException {
        String sql = "DELETE FROM QuoteResponse WHERE ResponseID = ?";
        connect_func();

        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
        preparedStatement.setInt(1, ResponseID);

        boolean rowDeleted = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
        return rowDeleted;
    }

    public boolean updateQuoteResponse(QuoteResponse quoteResponse) throws SQLException {
        String sql = "UPDATE QuoteResponse set RequestID =?, ClientID =?, ResponseDate=?,Price = ?,WorkPeriodFrom= ?, WorkPeriodTo= ?, Note = ?";
        connect_func();

        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
       
        preparedStatement.setInt(1, quoteResponse.getRequestID());
        preparedStatement.setInt(2, quoteResponse.getClientID());
        preparedStatement.setString(3, quoteResponse.getResponseDate());
        preparedStatement.setDouble(4, quoteResponse.getPrice());
        preparedStatement.setString(5, quoteResponse.getWorkPeriodFrom());
        preparedStatement.setString(6, quoteResponse.getWorkPeriodTo());
        preparedStatement.setString(7, quoteResponse.getNote());

        boolean rowUpdated = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
        return rowUpdated;
    }

    public QuoteResponse getQuoteResponse(int responseID) throws SQLException {
        QuoteResponse quoteResponse = null;
        String sql = "SELECT * FROM QuoteResponse WHERE ResponseID = ?";

        connect_func();

        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
        preparedStatement.setInt(1, responseID);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int requestID = resultSet.getInt("RequestID");
            int clientID = resultSet.getInt("ClientID");
            String responseDate = resultSet.getString("ResponseDate");
            Double price = resultSet.getDouble("Price");
            String workPeriodFrom = resultSet.getString("WorkPeriodFrom");
            String workPeriodTo = resultSet.getString("WorkPeriodTo");
            String note = resultSet.getString("Note");

            quoteResponse = new QuoteResponse(responseID, requestID, clientID, responseDate, price, workPeriodFrom, workPeriodTo, note);

            resultSet.close();
            preparedStatement.close();

            return quoteResponse;
        }
        return null; // Return null if no matching record is found
    }



    public boolean checkResponseID(int ResponseID) throws SQLException {
        boolean checks = false;
        String sql = "SELECT * FROM QuoteResponse WHERE ResponseID = ?";
        connect_func();
        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
        preparedStatement.setInt(1, ResponseID);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            checks = true;
        }

        resultSet.close();
        preparedStatement.close();
        return checks;
    }

    
    public boolean isValid(int ResponseID, int RequestID) throws SQLException {
        String sql = "SELECT * FROM QuoteResponse WHERE ResponseID = ? AND RequestID = ?";
        connect_func();
        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
        preparedStatement.setInt(1, ResponseID);
        preparedStatement.setInt(2, RequestID);

        ResultSet resultSet = preparedStatement.executeQuery();

        boolean isValid = resultSet.next(); // Check if a matching record is found

        resultSet.close();
        preparedStatement.close();
        return isValid;
    }



    public void init() throws SQLException, FileNotFoundException, IOException {
        connect_func();
        statement = (Statement) connect.createStatement();

        String[] INITIAL = {
            "drop database if exists pro; ",
            "create database pro; ",
            "use pro; ",
            "drop table if exists QuoteResponse; ",
            
                "CREATE TABLE if not exists QuoteResponse ( " +
                "ResponseID INT AUTO_INCREMENT PRIMARY KEY, " +
                "RequestID INT, " +
                "ClientID INT, " +
                "ResponseDate DATE," +
                "Price DOUBLE, " +
                "WorkPeriodFrom DATE," +
                "WorkPeriodTo DATE," +
                "Note VARCHAR(50)," +
                "FOREIGN KEY (RequestID) REFERENCES QuoteRequest (RequestID),"+
                "FOREIGN KEY (ClientID) REFERENCES QuoteRequest (ClientID)"+
                "); "
            
        };

        String[] TUPLES = {
            "insert into QuoteResponse (RequestID, ClientID, ResponseDate, Price, WorkPeriodFrom, WorkPeriodTo, Note) " +
            "values(1, 1, '2010-01-01', 1000, '2010-01-02', '2010-01-12', 'Note1')," +
            "(2, 2, '2011-02-02', 1100, '2011-02-03', '2011-02-13', 'Note2')," +
            "(3, 3, '2012-03-03', 1200, '2012-03-04', '2023-01-14', 'Note3')," +
            "(4, 4, '2013-04-04', 1300, '2013-04-05', '2013-04-15', 'Note4')," +
            "(5, 5, '2014-05-05', 1400, '2014-05-06', '2014-05-16', 'Note5')," +
            "(6, 6, '2015-06-06', 1500, '2015-06-07', '2015-06-17', 'Note6')," +
            "(7, 7, '2016-07-07', 1600, '2016-07-08', '2016-07-18', 'Note7')," +
            "(8, 8, '2017-08-08', 1700, '2017-08-09', '2017-08-19', 'Note8')," +
            "(9, 9, '2018-09-09', 1800, '2018-09-10', '2023-04-20', 'Note9')," +
            "(10, 10, '2019-10-10', 1900, '2019-10-11', '2019-10-21', 'Note10');"
        };

        // For loop to execute these SQL statements
        for (int i = 0; i < INITIAL.length; i++) {
            statement.execute(INITIAL[i]);
        }

        for (int i = 0; i < TUPLES.length; i++) {
            statement.execute(TUPLES[i]);
        }

        disconnect();
    }
}
