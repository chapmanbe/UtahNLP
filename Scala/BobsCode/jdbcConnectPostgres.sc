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
// To get the Postgres connector postgresql-9.3-1100.jdbc4.jar file to work, you need to put it the ~/jre/lib/ext for this to work.
//
// Step 1: Install Postgres Server/Client (make sure you download the connector with the install)
// Step 2: Make sure you install the pgAdmin III to create database utahnlp
// Step 3: Create a table called company [create table testing ( id INT, data VARCHAR(100) );]
//
//CREATE TABLE company
//(
//  id integer NOT NULL,
//  name text NOT NULL,
//  age integer NOT NULL,
//  address character(50),
//  salary real,
// CONSTRAINT company_pkey PRIMARY KEY (id)
//)
//WITH (
//  OIDS=FALSE
//);
//ALTER TABLE company
//  OWNER TO postgres;
//
// Step 4: Insert data
//
// INSERT INTO company VALUES
//    (1, 'test record', 25, '123 Elm Street', 25500.46);
//
// Step 5: run code
// Step 6: Results: 1, Results: 1, test record, 25, 123 Elm Street                                    , 25500.4609
//
////////////////

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLDataException;
import java.sql.SQLData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

object jdbcConnectPostgres {
  def main(args: Array[String]) {
    // connect to the database named "UtahNLP" on localhost
    val driver = "org.postgresql.Driver"
    val url = "jdbc:postgresql://localhost/utahnlp?user=postgres&password=XXXXXXX";

    // there's probably a better way to do this
    var connection:Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url)

      // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("Select * from company")

      while ( resultSet.next() ) {
        val id = resultSet.getInt("id")
        val name = resultSet.getString("name")
        val age = resultSet.getInt("age")
        val address = resultSet.getString("address")
        val salary = resultSet.getDouble("salary")
        
      	println("Results: " + id + ", " + name + ", " + age + ", " + address + ", " + salary )
      }
    } catch {
      case e: Throwable => e.printStackTrace
    }
    connection.close()
  }
}