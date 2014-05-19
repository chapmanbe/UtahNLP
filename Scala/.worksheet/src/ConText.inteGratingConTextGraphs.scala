package ConText
import scalax.collection.Graph
import scalax.collection.GraphPredef._, scalax.collection.GraphEdge._

object inteGratingConTextGraphs {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(195); 
  println("Welcome to the Scala worksheet")
  class CI(args: Array[String]){
	  val literal = args(0)
	  val category = args(1).toLowerCase().split(",")
	  val re = if( args.length >= 3 ) (""""""+args(2)).r else s"""$literal""".r
	  val rule = if( args.length >= 4 ) args(3) else ""
	
	  override def toString = "<<"+literal+">> <<"+category(0)+">> <<"+rule+">> <<"+rule+">>"
	
	  def isA( test_category: String): Boolean =
	    category contains test_category.toLowerCase()
	};$skip(478); 
	var b = "Pulmonary Embolism\tPE\tPE\tNone";System.out.println("""b  : String = """ + $show(b ));$skip(43); 
	val c = new CI(b.split("\t").map(_.trim));System.out.println("""c  : ConText.inteGratingConTextGraphs.CI = """ + $show(c ));$skip(43); 
	val d = new CI(b.split("\t").map(_.trim));System.out.println("""d  : ConText.inteGratingConTextGraphs.CI = """ + $show(d ));$skip(32); 
	println(c.toString+d.toString);$skip(28); 
  val cdGraph = Graph(c~>2);System.out.println("""cdGraph  : scalax.collection.Graph[Any,scalax.collection.GraphEdge.DiEdge] = """ + $show(cdGraph ));$skip(23); 
	val nodes = List(c,d);System.out.println("""nodes  : List[ConText.inteGratingConTextGraphs.CI] = """ + $show(nodes ));$skip(29); 
	val edges = List(c~>d,d~>c);System.out.println("""edges  : List[scalax.collection.GraphEdge.DiEdge[ConText.inteGratingConTextGraphs.CI]] = """ + $show(edges ));$skip(55); 
	val neGraph: Graph[Any,Any] = Graph.from(nodes,edges);System.out.println("""neGraph  : scalax.collection.Graph[Any,Any] = """ + $show(neGraph ))}
}
