////////////////
//
// Author: Bob Angell for Dr. Wendy Chapman
// Date: June, 2014
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
// Step 6: Results: 1, Results: 1, test record, 25, 123 Elm Street                                    , 25500.4609
// Results: hello!, 10
// Results: goodbye, 20
//
// This code applies regex from the domain tsv file and applies it to the "impression" column in the reports table within the ctpa.db database.
//
// Added June 18: The code now loads and stores the domain file into a two-dimensional array. It writes the results to regexResults.txt file. The file
// contains the values of the DB ID from the SQLITE db file, the Sentence ID from the file where the regex worked, etc. I have also printed out the 
// regex being used and the results. Next step is the parse the sentece further to determine whether it is negated or not.
//
// Updated July 2014: Added lexical file/matching on the sentences.
//
//
////////////////

package jdbc

import java.sql.DriverManager
import java.sql.Connection
import java.sql.SQLDataException
import java.sql.SQLData
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.sql.DatabaseMetaData
import scala.util.matching.Regex
import scala.util.matching.Regex.MatchIterator
import scala.io.Source
import java.io.File
import scala.util.regexp.Base
import java.io.PrintWriter
import java.io.FileWriter
import Array._


object jdbcConnectFromFile2SQLITE3v2 {
  def main(args: Array[String]) {
    // connect to the database named "ctpa.db" on localhost
    val driver = "org.sqlite.JDBC";
    // note: YOU MUST CHANGE THE PATH FOR YOUR DB FILE!!
    val db = "/opt/Development/ctpa.db"; 
    val sjdbc = "jdbc:sqlite";
    val url = sjdbc + ":" + db;

    // there's probably a better way to do this
    // This is good to have to ensure you are correctly identifying your file - Debugged this code for well over a day!
    // println("URL is: " + url)
    
    val delim = "\t"
    // val sentences = "\\.[^.]+"
    // val sentences = "(?i)(?<=[.?!])\\S+(?=[a-z])"
    val sentences = "(?<=[.?!])\\s+(?=[a-zA-Z])"

      
    //
    // z array is for the domain material
    // w array is for the lexical material
    //
    var z = new Array[String](4)
    var w = new Array[String](4)

    //
    var connection:Connection = null
    //
    // Read in the domain tsv file - you will need to change the path to use this.
    // if you want this to be interactive, you will need to provide input/output 
    // checks for this to work.
    //
    var domainFile = new File("/opt/Development/domain_kb.tsv")
    var lexicalFile = new File("/opt/Development/lexical_kb_04232013.tsv")
    //
    // Change this to write out the file with your results
    // If you want this file to change, you will either need to do this manually
    // here or do it dynamically in some other fashion.
    //
    //val fw = new FileWriter("/opt/Development/regexFullOutput.txt");
    //val fw1 = new FileWriter("/opt/Development/wheat.txt");
    //val fw2 = new FileWriter("/opt/Development/chaf.txt");
    //val fw3 = new FileWriter("/opt/Development/devl.txt");
    val fw4 = new FileWriter("/opt/Development/regexResults.txt");
    //
    // fw5 is currently not being used.
    //
    val fw5 = new FileWriter("/opt/Development/regexNegExResults.txt");

    //
    // Read from the domainfile variable above into the array zMatrix - this will keep this file in memory.
    // zArraySize calculates the total number of rows in the file. wArray calculates the total size of the lexical file.
    //
    //
    val zArraySize = Source.fromFile(domainFile).getLines.size
    val wArraySize = Source.fromFile(lexicalFile).getLines.size
    
    var cntw = 0
    var cntz = 0
    
    var zMatrix = Array.ofDim[String](zArraySize,4)
    var wMatrix = Array.ofDim[String](wArraySize,5)
    
    Source
        .fromFile(lexicalFile)
        .getLines
        .foreach { line => 
          //
          // splitting the lines into a tab delimited array (w) - must use -1 flag to ensure it creates FIVE columns
          //
     	  w = line.split(delim,-1).toArray
     	  //
     	  // make sure all values are placed into the wMatrix
     	  //
     	  wMatrix(cntw)(0) = w(0)     	       	  
     	  wMatrix(cntw)(1) = w(1)
     	  wMatrix(cntw)(2) = w(2)
     	  wMatrix(cntw)(3) = w(3)
     	  wMatrix(cntw)(4) = w(4)
     	  cntw += 1
    }

    Source
        .fromFile(domainFile)
        .getLines
        .foreach { line => 
          //
          // splitting the lines into a tab delimited array (z) - must use -1 flag to ensure it creates FOUR columns
          //
     	  z = line.split(delim,-1).toArray
     	  //
     	  // make sure all values are placed into the zMatrix
     	  //
     	  zMatrix(cntz)(0) = z(0)     	       	  
     	  zMatrix(cntz)(1) = z(1)
     	  zMatrix(cntz)(2) = z(2)
     	  zMatrix(cntz)(3) = z(3)
     	  cntz += 1
    }
    // fw3.close()

    // Use the below for a Header Row when outputting data if you would like. Have commented out this section currently
    //
    
    //fw4.write("DB ID: ")
    // fw4.write(id)
    //fw4.write("SID: ")
    // fw4.write(sCount.toString)
    // fw4.write("\n")
    //fw4.write("Sentence:           ") // can comment out when ready
    // fw4.write("\n")
    // fw4.write(l) // actual sentence
	//fw4.write("Results:") // actual sentence
	//fw4.write("\n")            	   		
    
    try {
      //
      // make the database connection to the sqlite3 database.
      //
      Class.forName(driver)
      connection = DriverManager.getConnection(url)
      //
      // create the statement, and run the select query
      //
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM reports")
      //
      // The code below selects all records from the database
      // and places them into an object called resultSet
      // The while statement will access each record row by row
      //
      // These were examples to test out the regex
      //
      //val r1 = """PE""".r                             //> r1  : scala.util.matching.Regex = PE
      //val r2 = """\b(?i)PE\b""".r                     //> r2  : scala.util.matching.Regex = \b(?i)PE\b
      //val cleanTextRegEx2 = """\s+""".r               //> cleanTextRegEx2  : scala.util.matching.Regex = \s+
      //
      // Added regex from Domain file for a further in-depth test
      //
      //val aneurysm1 = """aneurysm[a-z]*""".r
      //val aneurysm2 = """aneurysm""".r
      // val eRegex = """pulmonary\s(artery )?(embol[a-z]+)"""
      //val aneurysm3 = """(aneurysmal )?dilatation""".r 
      //val dvt = """((non.?)?occlusive)?\s?(thromb(us|i|osis|osed) |DVT|clot )""".r
      //
      //val embolism = """ + eRegex + """.r
      //val pembolism = """pulmonary\s(artery )?(embol[a-z]+)""".r
      //val pe = """PE""".r                             //> r1  : scala.util.matching.Regex = PE
      // 
      // Loop through the database - each record gets processed, all of the domain file is processed for each record.
      // 
      while ( resultSet.next() ) {
        //
        // id is from the column "id" in the reports table in the ctpa.db database
        //
        val id = resultSet.getString("id")
        //
        // impression is from the column "impression" in the reports table in the ctpa.db database
        //
        val impression = resultSet.getString("impression")
        //
        //
        // Splits impression into sentences and keep track of which sentences are which for regex computations.
        // Will eventually drop this code down below to apply regex at the sentence level.
        //
        // sArray is the sentence array and the way Scala implements a counter is while it iterates over a stream (go figure)
        // and even more strange, cannot cast the output to an integer, but it is a string ... am sure there is a more elegant
        // way to make this happen, however, just trying to get the basics down.
        //
        val sArray = impression.split(sentences,-1)
        val numberStream = Stream.iterate(0)(_ + 1).iterator
        val sCount: Int = numberStream.next
        //
        // alleRegex = Results of finding all instantiations of the regex inside the impression field.
        //
        var alleRegex = None: Option[CharSequence]
        
    	var countr1 = 0 
    	
    	//
    	// Several for loops iterating over the different arrays: domain/lexical material. The first of these is the sentence array.
    	// The logic goes like this:
    	// 		For each record in the database, each sentence is loaded into a sentence array (sArray).
    	//		Regex is applied to each sentence from the domain file. If there is a "hit", then the lexical file regex is 
    	//		executed to capture all of the possible negation involved.
    	//
    	//		Please note: THE PROGRAM OUTPUTS ALL POSSIBLE matches from the lexical file. One would need to "prune" out the irrelevant
    	//		parts. Have not figured this out for this program.
    	//
    	
        for ( l <- sArray) {
            val sCount: Int = numberStream.next // count of the sentence in the sArray
            for (i <- 0 to (zArraySize-1)) {
              //
              // Variable f just tells the program whether there is a regex value in the 3rd column, if not, program defaults to using the 1st column.
              //
              	var f = zMatrix(i)(2)
              	if (f.isEmpty()) {
              	  //
              	  // eeRegex pulls the value from the Domain file and is used in the eeRegex.r variable below.
              	  //
              		var eeRegex :String = (zMatrix(i)(0))
              		//
              		// alleRegex provides the results to finding regex in the sentence.
              		//
              		val alleRegex = eeRegex.r.findAllIn(l).mkString(",")
              	   	if (alleRegex.nonEmpty) {
              	   	// This is where we want to further parse each sentence using Regex values from the lexical file.
              	   	// The values below represent those who have matched on the domain Regex.

              	   	// All values below need to be in another for loop to loop through lexical array for each sentence.
              	   	// Would have placed this into a function, however, wanted to keep it simple and exposed to make it easier
              	   	// to read through the program logic when sewing this together.
              	   	                	   	  
              	   	  for (j <- 0 to (wArraySize-1)) {
              	   	    //
              	   	    // Need to test to see if column 3 in the lexical file is empty or not. If empty, use value in the first column
              	   	    //
              	   	    var g = wMatrix(j)(2)
                   	    if (g.isEmpty()) {
                   	      var wRegex :String = (wMatrix(j)(0))
              	   	      var edgeType :String = (wMatrix(j)(1))
              	   	      var directionType :String = (wMatrix(j)(3))            	   	    
              		      val negRegex = wRegex.r.findAllIn(l).mkString(",")
              		      //
              		      // If there is a "hit" for the regex, then it prints it out below using regex from column 1
              		      //
              		      if (negRegex.nonEmpty) {
              		        fw4.write("DB ID: ")
              	   	    	fw4.write(id)
              	   	    	// fw4.write(",")
              	   	    	fw4.write(" Sentence ID: ")
              	   	    	fw4.write(sCount.toString)
              	   	    	// fw4.write(",")
              	   	    	fw4.write("\n")
              	   	    	fw4.write("Actual Sentence: ") // can comment out when ready
              	   	    	// fw4.write("\n")
              	   	    	fw4.write(l) // actual sentence
              	   	    	// fw4.write(",")
              	   	    	fw4.write("\n")
              	   	    	// fw4.write("Regex: ") // actual sentence
              	   	    	// fw4.write("\n")
              	   	    	// fw4.write(eeRegex) // actual sentence
              	   	    	// fw4.write("\n")
              	   	    	// fw4.write("Results: ") // actual sentence
              	   	    	// fw4.write("\n")
              	   	    	fw4.write("Lexical Match: ")
              	   	    	fw4.write(negRegex)
              	   	    	fw4.write("\n")
              	   	    	// fw4.write(",")
              	   	    	fw4.write("Node Type: ")
              	   	    	fw4.write(edgeType)
              	   	    	fw4.write("\n")
              	   	    	// fw4.write(",")
              	   	    	fw4.write("Direction: ")
              	   	    	fw4.write(directionType)
              	   	    	fw4.write("\n")              	   		
              	   	    	// fw4.write(alleRegex) // actual sentence
              	   	    	fw4.write("\n")
              	   		    // fw4.write("\n")
              	   	    	} else {
              	   	    	  //
              	   	    	  // If the value in column 2 is there for the regex, then use column 2
              	   	    	  //
              	   	    	  var wRegex :String = (wMatrix(j)(2))
              	   	    	  var edgeType :String = (wMatrix(j)(1))
              	   	    	  var directionType :String = (wMatrix(j)(3))            	   	    
              	   	    	  val negRegex = wRegex.r.findAllIn(l).mkString(",")
              	   	    	  if (negRegex.nonEmpty) {
              	   	    		fw4.write("DB ID: ")
              	   	    		fw4.write(id)
              	   	    		// fw4.write(",")
              	   	    		fw4.write(" Sentence ID: ")
              	   	    		fw4.write(sCount.toString)
              	   	    		// fw4.write(",")
              	   	    		fw4.write("\n")
              	   	    		fw4.write("Actual Sentence: ") // can comment out when ready
              	   	    		// fw4.write("\n")
              	   	    		fw4.write(l) // actual sentence
              	   	    		// fw4.write(",")
              	   	    		fw4.write("\n")
              	   	    		// fw4.write("Regex: ") // actual sentence
              	   	    		// fw4.write("\n")
              	   	    		// fw4.write(eeRegex) // actual sentence
              	   	    		// fw4.write("\n")
              	   	    		// fw4.write("Results: ") // actual sentence
              	   	    		// fw4.write("\n")
              	   	    		fw4.write("Lexical Match: ")
              	   	    		fw4.write(negRegex)
              	   	    		fw4.write("\n")
              	   	    		// fw4.write(",")
              	   	    		fw4.write("Node Type: ")
              	   	    		fw4.write(edgeType)
              	   	    		fw4.write("\n")
              	   	    		// fw4.write(",")
              	   	    		fw4.write("Direction: ")
              	   	    		fw4.write(directionType)
              	   	    		fw4.write("\n")              	   		
              	   	    		// fw4.write(alleRegex) // actual sentence
              	   	    		fw4.write("\n")
              	   	    		// fw4.write("\n")
              	   	    	}
              	   	    }
                   	   }
              	   	}
              	   	} 
              	} else {
              	  //
              	  // This is used when there is regex in the 3rd column of the domain file.
              	  //
              		var eeRegex :String = (zMatrix(i)(2))
              		val alleRegex = eeRegex.r.findAllIn(l).mkString(",")
              	   	if (alleRegex.nonEmpty) {
              	   	  for (j <- 0 to (wArraySize-1)) {
              	   	    var g = wMatrix(j)(2)
              	   	    //
              	   	    // Test to see if lexical column 2 is empty
              	   	    //
              	   	    if (g.isEmpty()) {              	   	    
              	   	      var wRegex :String = (wMatrix(j)(0))
              	   	      var edgeType :String = (wMatrix(j)(1))
              	   	      var directionType :String = (wMatrix(j)(3))            	   	    
              		      val negRegex = wRegex.r.findAllIn(l).mkString(",")
              		      if (negRegex.nonEmpty) {
              	   	    	fw4.write("DB ID: ")
              	   	    	fw4.write(id)
              	   	    	// fw4.write(",")
              	   	    	fw4.write(" Sentence ID: ")
              	   	    	fw4.write(sCount.toString)
              	   	    	// fw4.write(",")
              	   	    	fw4.write("\n")
              	   	    	fw4.write("Actual Sentence: ") // can comment out when ready
              	   	    	// fw4.write("\n")
              	   	    	fw4.write(l) // actual sentence
              	   	    	// fw4.write(",")
              	   	    	fw4.write("\n")
              	   	    	// fw4.write("Regex: ") // actual sentence
              	   	    	// fw4.write("\n")
              	   	    	// fw4.write(eeRegex) // actual sentence
              	   	    	// fw4.write("\n")
              	   	    	// fw4.write("Results: ") // actual sentence
              	   	    	// fw4.write("\n")
              	   	    	fw4.write("Lexical Match: ")
              	   	    	fw4.write(negRegex)
              	   	    	fw4.write("\n")
              	   	    	// fw4.write(",")
              	   	    	fw4.write("Node Type: ")
              	   	    	fw4.write(edgeType)
              	   	    	fw4.write("\n")
              	   	    	// fw4.write(",")
              	   	    	fw4.write("Direction: ")
              	   	    	fw4.write(directionType)
              	   	    	fw4.write("\n")              	   		
              	   	    	// fw4.write(alleRegex) // actual sentence
              	   	    	fw4.write("\n")
              	   		    // fw4.write("\n")
              	   	    	}
              	   	    } else {
              	   	      var wRegex :String = (wMatrix(j)(2))
              	   	      var edgeType :String = (wMatrix(j)(1))
              	   	      var directionType :String = (wMatrix(j)(3))
              	   	      val negRegex = wRegex.r.findAllIn(l).mkString(",")
              	   	      
              	   	      if (negRegex.nonEmpty) {
              	   	        fw4.write("DB ID: ")
              	   	    	fw4.write(id)
              	   	    	// fw4.write(",")
              	   	    	fw4.write(" Sentence ID: ")
              	   	    	fw4.write(sCount.toString)
              	   	    	// fw4.write(",")
              	   	    	fw4.write("\n")
              	   	    	fw4.write("Actual Sentence: ") // can comment out when ready
              	   	    	// fw4.write("\n")
              	   	    	fw4.write(l) // actual sentence
              	   	    	// fw4.write(",")
              	   	    	fw4.write("\n")
              	   	    	// fw4.write("Regex: ") // actual sentence
              	   	    	// fw4.write("\n")
              	   	    	// fw4.write(eeRegex) // actual sentence
              	   	    	// fw4.write("\n")
              	   	    	// fw4.write("Results: ") // actual sentence
              	   	    	// fw4.write("\n")
              	   	    	fw4.write("Lexical Match: ")
              	   	    	fw4.write(negRegex)
              	   	    	fw4.write("\n")
              	   	    	// fw4.write(",")
              	   	    	fw4.write("Node Type: ")
              	   	    	fw4.write(edgeType)
              	   	    	fw4.write("\n")
              	   	    	// fw4.write(",")
              	   	    	fw4.write("Direction: ")
              	   	    	fw4.write(directionType)
              	   	    	fw4.write("\n")              	   		
              	   	    	// fw4.write(alleRegex) // actual sentence
              	   	    	fw4.write("\n")
              	   	    	// fw4.write("\n")
              	   	      }
              	   	      }           	   	    
              	   	  }  
              	   	}         	  
              	}
            }
        }
      }
    } catch {
      case e: Throwable => e.printStackTrace
    }
    println("Finished processing")
    connection.close()
    //fw.close
    //fw1.close
    //fw2.close
    //fw3.close
    fw4.close
    fw5.close
  }
}
