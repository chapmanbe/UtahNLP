package ConText
import scala.util.matching.Regex

object regexDebug {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  val r1 = """PE""".r                             //> r1  : scala.util.matching.Regex = PE
  val r2 = """\b(?i)PE\b""".r                     //> r2  : scala.util.matching.Regex = \b(?i)PE\b
  val cleanTextRegEx2 = """\s+""".r               //> cleanTextRegEx2  : scala.util.matching.Regex = \s+
  println( (r1 findAllIn "We had a PE and a pe performed").mkString(","))
                                                  //> PE
  println( (r2 findAllIn "We had a PE and a pe performed").mkString(","))
                                                  //> PE,pe
  println( cleanTextRegEx2 replaceAllIn("We \n\nhad to get\ta   number"," "))
                                                  //> We had to get a number
}