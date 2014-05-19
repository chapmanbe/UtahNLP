package ConText
import scalax.collection.Graph
import scalax.collection.GraphPredef._, scalax.collection.GraphEdge._

object inteGratingConTextGraphs {
  println("Welcome to the Scala worksheet")
  class CI(args: Array[String]){
	  val literal = args(0)
	  val category = args(1).toLowerCase().split(",")
	  val re = if( args.length >= 3 ) (""""""+args(2)).r else s"""$literal""".r
	  val rule = if( args.length >= 4 ) args(3) else ""
	
	  override def toString = "<<"+literal+">> <<"+category(0)+">> <<"+rule+">> <<"+rule+">>"
	
	  def isA( test_category: String): Boolean =
	    category contains test_category.toLowerCase()
	}
	var b = "Pulmonary Embolism\tPE\tPE\tNone"
	val c = new CI(b.split("\t").map(_.trim))
	val d = new CI(b.split("\t").map(_.trim))
	println(c.toString+d.toString)
  val cdGraph = Graph(c~>2)
	val nodes = List(c,d)
	val edges = List(c~>d,d~>c)
	val neGraph: Graph[Any,Any] = Graph.from(nodes,edges)
}