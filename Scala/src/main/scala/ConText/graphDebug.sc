package ConText
import scalax.collection.Graph
import scalax.collection.GraphPredef._, scalax.collection.GraphEdge._

object graphDebug {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  var g1 = Graph[Int,DiEdge](1,2,3,1~>2,1~>3)     //> g1  : scalax.collection.Graph[Int,scalax.collection.GraphEdge.DiEdge] = Grap
                                                  //| h(1, 2, 3, 1~>2, 1~>3)
  var g2 = Graph(3~>2)                            //> g2  : scalax.collection.Graph[Int,scalax.collection.GraphEdge.DiEdge] = Grap
                                                  //| h(2, 3, 3~>2)
  val nodes = List(5)                             //> nodes  : List[Int] = List(5)
  val edges = List(3~1)                           //> edges  : List[scalax.collection.GraphEdge.UnDiEdge[Int]] = List(3~1)
  val g3 = Graph.from(nodes,edges)                //> g3  : scalax.collection.Graph[Int,scalax.collection.GraphEdge.UnDiEdge] = Gr
                                                  //| aph(1, 3, 5, 3~1)
  var n, m = 0;                                   //> n  : Int = 0
                                                  //| m  : Int = 0
  //val f = Graph.fill(100)({n=m; m+=1; n~>m})
	val e1 = g2.edges.head                    //> e1  : scalax.collection.Graph[Int,scalax.collection.GraphEdge.DiEdge]#EdgeT 
                                                  //| = 3~>2
  val n1 = g2.nodes.head                          //> n1  : scalax.collection.Graph[Int,scalax.collection.GraphEdge.DiEdge]#NodeT 
                                                  //| = 2
  val g4 = Graph(1~2)                             //> g4  : scalax.collection.Graph[Int,scalax.collection.GraphEdge.UnDiEdge] = Gr
                                                  //| aph(1, 2, 1~2)
  val n4 = g4.nodes.head                          //> n4  : ConText.graphDebug.g4.NodeT = 1
  n4 % 3                                          //> res0: Int = 1
  n4.diSuccessors                                 //> res1: scala.collection.Set[ConText.graphDebug.g4.NodeT] = Set(2)
  val g5 = Graph(2~3,3~1,5~2,4~5,4~6)             //> g5  : scalax.collection.Graph[Int,scalax.collection.GraphEdge.UnDiEdge] = Gr
                                                  //| aph(1, 2, 3, 4, 5, 6, 2~3, 3~1, 4~5, 4~6, 5~2)
  g5 mkString "::"                                //> res2: String = 1::5::2::6::3::4::5~2::2~3::3~1::4~6::4~5
  g5.nodes mkString "::"                          //> res3: String = 1::5::2::6::3::4
  g5 find 2                                       //> res4: Option[ConText.graphDebug.g5.NodeT] = Some(2)
  g5 find 7                                       //> res5: Option[ConText.graphDebug.g5.NodeT] = None
}