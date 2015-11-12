package main;
import java.lang.*;
import javax.naming.spi.ObjectFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.helpers.DefaultValidationEventHandler;

import org.xml.sax.SAXException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import model.Server;

public class MainApp
{
  private static String localpath;

  public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
	boolean running = true;
	Connection connection = null;
    if (args.length < 1){
      System.out.println("Specify location of data xml file as first parameter" );
      running = false;
    } else
      localpath = args[0];

    if (running) {
      connection = getConnection();
      createSchema(connection);
    }


	while(running)
	{
	  System.out.println(" Please select a command (help for command list); ");
      DataInputStream in =  new DataInputStream(System.in);
      String option = in.readLine();

	    if(option.equals("help")){
		  showHelp();
    	}
	    else if(option.equals("quit")) {
		   running = false;
	    }
	    else if(option.equals("countServers"))
	    {
			 String sqlquery = new String("select count(*) from server");
			 runCommand(connection, sqlquery, false);
	    }
	    else if(option.equalsIgnoreCase("addServer"))
	    {
			 Server server = readXML();
			 addServer(connection,server);
	    }
	    else if(option.equalsIgnoreCase("deleteServer"))
	    {
			 String server_id = (String)in.readLine();
			 String sqlquery = new String("delete from server where id=%s");
			 String formattedQuery = String.format(sqlquery,server_id);
			 runCommand(connection, formattedQuery,false);
	    }
	    else if(option.equalsIgnoreCase("editServer"))
	    {
		    String server_id = (String)in.readLine();
		    String server_name = (String)in.readLine();
			String sqlquery = new String("update server set name = %s where id = %s");
			String formattedQuery = String.format(sqlquery, server_name, server_id);
			runCommand(connection, formattedQuery,false);
	    }
	    else if(option.equalsIgnoreCase("listServers"))
	    {
			String sqlquery = new String("select name from server");
			runCommand(connection, sqlquery,true);
	    }
	  }
    }

    private static void showHelp() {
		System.out.println("0) help  this list");
		System.out.println("1) countServers to display the current number of servers present");
		System.out.println("2) addServer to add a new server (reads id and server name from external file server_1.xml) ");
		System.out.println("3) editServer to change the name of a server identified by id (takes 2 additional args - the id and the new name)");
		System.out.println("4) deleteServer to delete a server (takes one more arg - the id of the server to delete)");
		System.out.println("5) listServers to display details of all servers servers");
    }


    private static Connection getConnection() {

      Connection conn = null;
	  try {
	   conn = DriverManager.getConnection("jdbc:mysql://localhost/test?user=root?password=password");
	  } catch (SQLException ex){
	    System.out.println("Error establishing connection with database using root/password as credentials.");
	  }

       return conn;
    }

    private static void createSchema(Connection conn) {
      String sql = null;

      try {
	    File f = new File("dbSchema.sql");
        byte[] buffer = new byte[(int)f.length()];
        FileInputStream is = new FileInputStream("dbSchema.sql");
		is.read(buffer);
		sql = new String(buffer, "UTF-8");
		System.out.println("Results of sql access :" + sql);

		runCommand(conn,sql, false);

      } catch (IOException ex){
      	System.out.println("Error reading SQL from file");
      }

    }

   /**
    * Run command : (Connection, Sql)
    */
   private static String runCommand(Connection conn, String sql, boolean list) {

	 Statement stmt = null;
	 ResultSet rs = null;
	 String value = new String("0");

	 try {
	   stmt = conn.createStatement();

	   if (stmt.execute(sql))
		 rs = stmt.getResultSet();


	   if (rs != null)
	     if(rs.next() != false) {
	       if(!list) {
		     System.out.println("\n Result " + rs.getString(1) +"\n");
	       } else {
	         System.out.println("\n Result " + rs.getString("name")  +"\n");
		     while (rs.next())
		       System.out.println("\n Result " +  rs.getString("name")  +"\n");
	       }
	     } else
		   System.out.println("Empty/No result set");

	 } catch (SQLException ex){
		System.out.println("Error reading from database : " + ex.getMessage());
	 } finally {
	   try {
	    stmt.close();
	    if (rs !=null) rs.close();
	   } catch (SQLException ex){}
     }

     return value;
 }

   private static Server readXML() {

     Server po = null;
	 try {
	   JAXBContext jc = JAXBContext.newInstance(Server.class);
	   Unmarshaller u = jc.createUnmarshaller();
	   u.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
	   po = (Server)u.unmarshal(new FileInputStream(localpath +"\\data.xml"));
	 } catch (JAXBException ex){
      	System.out.println("Error parsing XML from file " + ex.getMessage());
	 } catch (IOException ex){
      	System.out.println("IO  error  " + ex.getMessage());
     }
     return po;
  }

  private static void addServer (Connection connection, Server server) {
	 String serverCountQuery = new String("select count(*) from server");
	 String serverCount = runCommand(connection, serverCountQuery, false);
	 int currentServerTotal = Integer.parseInt(serverCount);
	 String newServerTotal = Integer.toString(++currentServerTotal);
	 String server_name = newServerTotal;
	 String sqlquery = new String("insert into server values (%s, %s)");
	   String formattedQuery = String.format(sqlquery,server.getId(), server.getName());
	 System.out.println("formattedQuery is " + formattedQuery);
	 runCommand(connection, formattedQuery, false);
   }
}
