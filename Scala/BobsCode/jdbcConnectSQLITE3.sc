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
// To get the sqlite3 connector sqlite-jdbc-3.7.2.jar file to work, you need to put it the ~/jre/lib/ext directory.
//
// Step 1: Install sqlite3 and .jar files.
// Step 2: Using sqlite3 at the command-line, create your database - because this is a FILE, naming is important, even with the extension. 
//	You may name your file ANYTHING, just as long as you use this name EXACTLY as it is for the JDBC sqlite statement.
// Step 3: Create a table/insert data into tbl1
//
// sqlite> create table tbl1(one varchar(10), two smallint);
// sqlite> insert into tbl1 values('hello!',10);
// sqlite> insert into tbl1 values('goodbye', 20);
// sqlite> select * from tbl1;
//  hello!|10
//  goodbye|20
//
// Step 5: run code
// Step 6: Your results should look like this.
// Results: hello!, 10
// Results: goodbye, 20
//
////////////////

package jdbc

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLDataException;
import java.sql.SQLData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;

object jdbcConnectSQLITE3 {
  def main(args: Array[String]) {
    // connect to the database named "UtahNLP" on localhost
    val driver = "org.sqlite.JDBC";
    // note: YOU MUST CHANGE THE PATH FOR YOUR DB FILE!!
    val db = "/opt/Development/utahnlp"; 
    val sjdbc = "jdbc:sqlite";
    val url = sjdbc + ":" + db;

    // there's probably a better way to do this
    // This is good to have to ensure you are correctly identifying your file - Debugged this code for well over a day!
    println("URL is: " + url)
    
    var connection:Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url)

      // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM TBL1")
      while ( resultSet.next() ) {
        val one = resultSet.getString("one")
        val two = resultSet.getString("two") 
      	println("Results: " + one + ", " + two )
      }
    } catch {
      case e: Throwable => e.printStackTrace
    }
    connection.close()
  }
}
