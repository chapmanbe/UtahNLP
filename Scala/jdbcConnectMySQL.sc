////////////////
//
// Author: Bob Angell for 
// Dr. Wendy Chapman, 
// Department Chair, 
// Biomedical Informatics, 
// University of Utah School of Medicine, 
// University of Utah
//         
// Date: May, 2014
// 
// How to use:
//
// To get the MySQL connector to work, you need to put the mysql-connector-java-X.X.XX in the ~/jre/lib/ext for this to work.
//
// Step 1: Install MySQL Server/Client (download the connector separately)
// Step 2: As root, create database utahnlp
// Step 3: Create a table called testing [mysql>create table testing ( id INT, data VARCHAR(100) );]
// Step 4: mysql> insert into testing values (1,"This is the only value");
// Step 5: run code
// Step 6: Results: 1, This is the only value
//
////////////////

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLDataException;
import java.sql.SQLData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

object jdbcConnectMySQL {
  def main(args: Array[String]) {
    // connect to the database named "UtahNLP" on localhost
    val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://localhost/utahnlp"
    val username = "root"
    val password = ""

    // there's probably a better way to do this
    var connection:Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("Select * from testing")
      
      //val resultSet = statement.executeQuery("SELECT host, user FROM user")
      while ( resultSet.next() ) {
        val id = resultSet.getInt("id")
        val data = resultSet.getString("data")
      	println("Results: " + id + ", " + data)
      }
      //  val host = resultSet.getString("host")
      //  val user = resultSet.getString("user")
      //  println("host, user = " + host + ", " + user)
      //}
    } catch {
      case e: Throwable => e.printStackTrace
    }
    connection.close()
  }
}