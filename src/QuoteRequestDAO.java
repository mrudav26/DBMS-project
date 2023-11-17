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
@WebServlet("/QuoteRequestDAO")
public class QuoteRequestDAO 
{
	private static final long serialVersionUID = 1L;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	public QuoteRequestDAO(){}
	
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
		        String sql = "SELECT * FROM QuoteRequest WHERE RequestID = ?";
		        preparedStatement = connect.prepareStatement(sql);
		        preparedStatement.setString(1, email);
		        ResultSet rs = preparedStatement.executeQuery();
		        return rs.next();
		    } catch (SQLException e) {
		        e.printStackTrace(); // Log the exception for debugging purposes
		        return false;
		    } finally {
		        disconnect();
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
    
    public List<QuoteRequest> listAllRequests() throws SQLException {
        List<QuoteRequest> listRequests = new ArrayList<QuoteRequest>();
        String sql = "SELECT * FROM QuoteRequest"; // Corrected the table name to QuoteRequest
        connect_func();
        statement = (Statement) connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int requestID = resultSet.getInt("RequestID");
            int clientID = resultSet.getInt("ClientID");          
            String requestDate = resultSet.getString("RequestDate");
            String status = resultSet.getString("Status");
            String note = resultSet.getString("Note");
            String size = resultSet.getString("Size");
            double height = resultSet.getDouble("Height");
            String location = resultSet.getString("Location");
            double proximityToHouse = resultSet.getDouble("ProximityToHouse");

            QuoteRequest request = new QuoteRequest(requestID, clientID, requestDate, status, note, size, height, location, proximityToHouse);
            listRequests.add(request);
        }
        resultSet.close();
        disconnect();
        return listRequests;
    }

    protected void disconnect() throws SQLException {
        if (connect != null && !connect.isClosed()) {
        	connect.close();
        }
    }
    
    public void insert(QuoteRequest request) throws SQLException {
        connect_func("root", "pass1234");
        String sql = "INSERT INTO QuoteRequest (ClientID, RequestDate, Status, Note, Size, Height, Location, ProximityToHouse) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
        preparedStatement.setInt(1, request.getClientID());
        preparedStatement.setString(2, request.getRequestDate());
        preparedStatement.setString(3, request.getStatus());
        preparedStatement.setString(4, request.getNote());
        preparedStatement.setString(5, request.getSize());
        preparedStatement.setDouble(6, request.getHeight());
        preparedStatement.setString(7, request.getLocation());
        preparedStatement.setDouble(8, request.getProximityToHouse());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public boolean delete(int RequestID) throws SQLException {
        String sql = "DELETE FROM QuoteRequest WHERE RequestID = ?";
        connect_func();

        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
        preparedStatement.setInt(1, RequestID);

        boolean rowDeleted = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
        return rowDeleted;
    }

     
    public boolean updateQuoteRequest(QuoteRequest quoteRequest) throws SQLException {
        String sql = "UPDATE QuoteRequest SET ClientID=?, RequestDate=?, Status=?, Note=?, Size=?, Height=?, Location=?, ProximityToHouse=? WHERE RequestID=?";
        connect_func();

        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
        preparedStatement.setInt(1, quoteRequest.getClientID());       
        preparedStatement.setString(2, quoteRequest.getRequestDate());
        preparedStatement.setString(3, quoteRequest.getStatus());
        preparedStatement.setString(4, quoteRequest.getNote());
        preparedStatement.setString(5, quoteRequest.getSize());
        preparedStatement.setDouble(6, quoteRequest.getHeight());
        preparedStatement.setString(7, quoteRequest.getLocation());
        preparedStatement.setDouble(8, quoteRequest.getProximityToHouse());
        preparedStatement.setInt(9, quoteRequest.getRequestID());

        boolean rowUpdated = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
        return rowUpdated;
    }

    
    public QuoteRequest getQuoteRequest(int requestID) throws SQLException {
        QuoteRequest quoteRequest = null;
        String sql = "SELECT * FROM QuoteRequest WHERE RequestID = ?";

        connect_func();

        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
        preparedStatement.setInt(1, requestID);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int clientID = resultSet.getInt("ClientID");            
            String requestDate = resultSet.getString("RequestDate");
            String status = resultSet.getString("Status");
            String note = resultSet.getString("Note");
            String size = resultSet.getString("Size");
            double height = resultSet.getDouble("Height");
            String location = resultSet.getString("Location");
            double proximityToHouse = resultSet.getDouble("ProximityToHouse");

            quoteRequest = new QuoteRequest(requestID, clientID, requestDate, status, note, size, height, location, proximityToHouse);
        }

        resultSet.close();
        preparedStatement.close();
        disconnect(); // Close the connection after use

        return quoteRequest;
    }


    
    public boolean checkRequestID(int RequestID) throws SQLException {
        boolean checks = false;
        String sql = "SELECT * FROM QuoteRequest WHERE RequestID = ?";
        connect_func();
        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
        preparedStatement.setInt(1, RequestID);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            checks = true;
        }

        resultSet.close();
        preparedStatement.close();
        return checks;
    }

    
    
    
    
    public boolean isValid(int RequestID, int ClientID) throws SQLException {
        String sql = "SELECT * FROM QuoteRequest WHERE RequestID = ? AND ClientID = ?";
        connect_func();

        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
        preparedStatement.setInt(1, RequestID);
        preparedStatement.setInt(2, ClientID);

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
            "drop database if exists pro;",
            "create database pro;",
            "use pro;",
            "drop table if exists QuoteRequest;",
            "CREATE TABLE if not exists QuoteRequest ( " +
            "RequestID INT AUTO_INCREMENT PRIMARY KEY, " +
            "ClientID INT, " +
            "RequestDate DATE," +
            "Status VARCHAR(100)," +
            "Note VARCHAR(200)," +
            "Size VARCHAR(10)," +
            "Height DECIMAL(3, 1)," +
            "Location VARCHAR(100)," +
            "ProximityToHouse FLOAT(20)" +
            "FOREIGN KEY (ClientID) REFERENCES Client (ClientID)," +"); "
        };
        
    

        String[] TUPLES = {("insert into QuoteResponse (RequestID, ClientID, ResponseDate, Price, WorkPeriodFrom, WorkPeriodTo, Note)" +
            "values (1, 1, '2010-01-01', 1000, '2010-01-02', '2010-01-12', 'Note1')," +
            "( 2, 2, '2011-02-02', 1100, '2011-02-03', '2011-02-13', 'Note2'),"+
            "(3, 3, '2012-03-03', 1200, '2012-03-04', '2023-01-14', 'Note3'),"+
            "(4, 4, '2013-04-04', 1300, '2013-04-05', '2013-04-15', 'Note4'),"+
            "( 5, 5, '2014-05-05', 1400, '2014-05-06', '2014-05-16', 'Note5'),"+
            "( 6, 6, '2015-06-06', 1500, '2015-06-07', '2015-06-17', 'Note6'),"+
            "(7, 7, '2016-07-07', 1600, '2016-07-08', '2016-07-18', 'Note7'),"+
            "( 8, 8, '2017-08-08', 1700, '2017-08-09', '2017-08-19', 'Note8'),"+           
            "( 9, 9, '2018-09-09', 1800, '2018-09-10', '2023-04-20', 'Note9'),"+
            "(10, 10, '2019-10-10', 1900, '2019-10-11', '2019-10-21', 'Note10');")
        };

        // Loop to execute the SQL statements
        for (int i = 0; i < INITIAL.length; i++)
            statement.execute(INITIAL[i]);
        for (int i = 0; i < TUPLES.length; i++)
            statement.execute("INSERT INTO QuoteRequest (ClientID, RequestDate, Status, Note, Size, Height, Location, ProximityToHouse) VALUES " + TUPLES[i]);
        disconnect();
    }

   
    
    
    
    
    
	
	

}
