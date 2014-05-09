package ConText
import scala.util.matching.Regex

object regexDebug {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(113); 
  println("Welcome to the Scala worksheet");$skip(22); 
  val r1 = """PE""".r;System.out.println("""r1  : scala.util.matching.Regex = """ + $show(r1 ));$skip(30); 
  val r2 = """\b(?i)PE\b""".r;System.out.println("""r2  : scala.util.matching.Regex = """ + $show(r2 ));$skip(36); 
  val cleanTextRegEx2 = """\s+""".r;System.out.println("""cleanTextRegEx2  : scala.util.matching.Regex = """ + $show(cleanTextRegEx2 ));$skip(74); 
  println( (r1 findAllIn "We had a PE and a pe performed").mkString(","));$skip(74); 
  println( (r2 findAllIn "We had a PE and a pe performed").mkString(","));$skip(78); 
  println( cleanTextRegEx2 replaceAllIn("We \n\nhad to get\ta   number"," "))}
}
