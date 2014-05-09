package ConText
import scalax.collection.Graph
import scalax.collection.GraphPredef._, scalax.collection.GraphEdge._

object graphDebug {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(181); 
  println("Welcome to the Scala worksheet");$skip(46); 
  var g1 = Graph[Int,DiEdge](1,2,3,1~>2,1~>3);System.out.println("""g1  : scalax.collection.Graph[Int,scalax.collection.GraphEdge.DiEdge] = """ + $show(g1 ));$skip(23); 
  var g2 = Graph(3~>2);System.out.println("""g2  : scalax.collection.Graph[Int,scalax.collection.GraphEdge.DiEdge] = """ + $show(g2 ));$skip(22); 
  val nodes = List(5);System.out.println("""nodes  : List[Int] = """ + $show(nodes ));$skip(24); 
  val edges = List(3~1);System.out.println("""edges  : List[scalax.collection.GraphEdge.UnDiEdge[Int]] = """ + $show(edges ));$skip(35); 
  val g3 = Graph.from(nodes,edges);System.out.println("""g3  : scalax.collection.Graph[Int,scalax.collection.GraphEdge.UnDiEdge] = """ + $show(g3 ));$skip(16); 
  var n, m = 0;System.out.println("""n  : Int = """ + $show(n ));System.out.println("""m  : Int = """ + $show(m ));$skip(71); ;
  //val f = Graph.fill(100)({n=m; m+=1; n~>m})
	val e1 = g2.edges.head;System.out.println("""e1  : scalax.collection.Graph[Int,scalax.collection.GraphEdge.DiEdge]#EdgeT = """ + $show(e1 ));$skip(25); 
  val n1 = g2.nodes.head;System.out.println("""n1  : scalax.collection.Graph[Int,scalax.collection.GraphEdge.DiEdge]#NodeT = """ + $show(n1 ));$skip(22); 
  val g4 = Graph(1~2);System.out.println("""g4  : scalax.collection.Graph[Int,scalax.collection.GraphEdge.UnDiEdge] = """ + $show(g4 ));$skip(25); 
  val n4 = g4.nodes.head;System.out.println("""n4  : ConText.graphDebug.g4.NodeT = """ + $show(n4 ));$skip(9); val res$0 = 
  n4 % 3;System.out.println("""res0: Int = """ + $show(res$0));$skip(18); val res$1 = 
  n4.diSuccessors;System.out.println("""res1: scala.collection.Set[ConText.graphDebug.g4.NodeT] = """ + $show(res$1));$skip(38); 
  val g5 = Graph(2~3,3~1,5~2,4~5,4~6);System.out.println("""g5  : scalax.collection.Graph[Int,scalax.collection.GraphEdge.UnDiEdge] = """ + $show(g5 ));$skip(19); val res$2 = 
  g5 mkString "::";System.out.println("""res2: String = """ + $show(res$2));$skip(25); val res$3 = 
  g5.nodes mkString "::";System.out.println("""res3: String = """ + $show(res$3));$skip(12); val res$4 = 
  g5 find 2;System.out.println("""res4: Option[ConText.graphDebug.g5.NodeT] = """ + $show(res$4));$skip(12); val res$5 = 
  g5 find 7;System.out.println("""res5: Option[ConText.graphDebug.g5.NodeT] = """ + $show(res$5))}
}
