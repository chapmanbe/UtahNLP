////////////////
//
// Author: Bob Angell for Dr. Wendy Chapman
// Date: May, 2014
// 
// How to use:
//
// To get the sqlite3 connector sqlite-jdbc-3.7.2.jar file to work, you need to put it the ~/jre/lib/ext directory.
//
// Step 1: Install sqlite3 and .jar files.
// Step 2: Using sqlite3 at the command-line, create your database - because this is a FILE, naming is important, even with the extension. 
//	You may name your file ANYTHING, just as long as you use this name EXACTLY as it is for the JDBC sqlite statement.
// Step 3: Use table reports from the cpta.db
// Step 4: run code
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
import scala.util.matching.Regex

object jdbcConnectCTPASQLITE3 {
  def main(args: Array[String]) {
    // connect to the database named "ctpa.db" on localhost
    val driver = "org.sqlite.JDBC";
    // note: YOU MUST CHANGE THE PATH FOR YOUR DB FILE!!
    val db = "/opt/Development/ctpa.db"; 
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
      val resultSet = statement.executeQuery("SELECT * FROM reports")
      //
      // The code below selects all records from the database
      // and places them into an object called resultSet
      // The while statement will access each record row by row
      //
      // Trying the regex code from the regexDebug.sc worksheet.
      //
      // val r1 = """PE""".r                          //> r1  : scala.util.matching.Regex = PE
      // val r2 = """\b(?i)PE\b""".r                  //> r2  : scala.util.matching.Regex = \b(?i)PE\b
      // val cleanTextRegEx2 = """\s+""".r            //> cleanTextRegEx2  : scala.util.matching.Regex = \s+
      //
      // Added regex from Domain file for a further in-depth test
      //
      val aneurysm1 = """aneurysm[a-z]*""".r
      val aneurysm2 = """aneurysm""".r
      val aneurysm3 = """(aneurysmal )?dilatation""".r 
      val dvt = """((non.?)?occlusive)?\s?(thromb(us|i|osis|osed) |DVT|clot )""".r
      //
      val embolism = """ (emboli|embolism|embolus) """.r
      val pembolism = """pulmonary\s(artery )?(embol[a-z]+)""".r
      val pe = """PE""".r                             //> r1  : scala.util.matching.Regex = PE
      //
      //
      //
      //
      while ( resultSet.next() ) {
        val id = resultSet.getString("id")
        val impression = resultSet.getString("impression")
        //
        // println( (r1 findAllIn impression).mkString(","))
        // println( (r2 findAllIn impression).mkString(","))
        // println( (aneurysm1 findAllIn impression).mkString(","))
        // println( (aneurysm2 findAllIn impression).mkString(","))
        // println( (aneurysm3 findAllIn impression).mkString(","))
        //
        println("These are the results of all PEs as follows: \n")
        println("Record ID: " + id + "\n")
        println( (embolism findAllIn impression).mkString(","))
        println("\n")
        println( (pembolism findAllIn impression).mkString(","))
        println("\n")
        println( (pe findAllIn impression).mkString(","))
        println("\n")
      	// println("Results: " + id + ", " + impression + "\n")
      }
    } catch {
      case e: Throwable => e.printStackTrace
    }
    connection.close()
  }
}
