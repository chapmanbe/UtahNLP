package ConText
import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.util.matching.Regex
import scala.math
import scalax.collection.Graph
import scalax.collection.GraphPredef._, scalax.collection.GraphEdge._


object Span{;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(578); 

  def getSpan( matchedTerm: scala.util.matching.Regex.Match, matchingItem: ConText.ContextItem) = {
    if( matchingItem.rule == "forward") {
      (matchedTerm.end,matchedTerm.source.length)
    } else if( matchingItem.rule == "backward"){
      (0,matchedTerm.start)
    } else {
      (0,matchedTerm.source.length)
    }
  };System.out.println("""getSpan: (matchedTerm: util.matching.Regex.Match, matchingItem: ConText.ConText.ContextItem)(Int, Int)""");$skip(799); 

  def limitScope( target: ConText.ConTextTag, modifier: ConText.ConTextTag): ConText.ConTextTag ={
    val tRule = target.rule
    if( tRule == "" ||
        tRule == "terminate" ||
        (! target.matchingItem.isA(modifier.category(0)) && modifier.rule != "terminate") ){
          target
    } else{
      var newStart = target.start
      var newEnd = target.end
      if( ((tRule contains "forward") || (tRule contains "bidirectional") ) && modifier.start > target.end )
        newEnd = math.min(target.end,modifier.start)
      else if( ( (tRule contains "backward") || (tRule contains "bidirectional") ) && modifier.end < target.start)
        newStart = math.max(target.start, modifier.end)
      new ConText.ConTextTag(target.matchedTerm,target.matchingItem,(newStart,newEnd))
    }
  };System.out.println("""limitScope: (target: ConText.ConText.ConTextTag, modifier: ConText.ConText.ConTextTag)ConText.ConText.ConTextTag""");$skip(148); 

  def dist(tag1: ConText.ConTextTag,tag2: ConText.ConTextTag): Double =
    math.min( math.abs(tag1.start-tag2.end),math.abs(tag1.end-tag2.start));System.out.println("""dist: (tag1: ConText.ConText.ConTextTag, tag2: ConText.ConText.ConTextTag)Double""");$skip(133); 
  def encompasses(tag1: ConText.ConTextTag,tag2: ConText.ConTextTag): Boolean =
    tag1.start <= tag2.start && tag1.end >= tag2.end;System.out.println("""encompasses: (tag1: ConText.ConText.ConTextTag, tag2: ConText.ConText.ConTextTag)Boolean""");$skip(97); 

  def replaceCategory(tag1: ConText.ConTextTag,category: String): ConText.ConTextTag =
    tag1;System.out.println("""replaceCategory: (tag1: ConText.ConText.ConTextTag, category: String)ConText.ConText.ConTextTag""")}
  
}

object IO{
  def instantiateItemDataFromCSV(filename: String) = {
    val rows = ArrayBuffer[ConText.ContextItem]()
    //val rows = List()
    val source =Source.fromFile(filename)
    val iter = source.getLines
    val header = (iter.next).split("\t")
    for (line <- iter ){
      rows += new ConText.ContextItem(line.split("\t").map(_.trim))
    }
    //println("length of rows is "+rows.length)
    (header.toList, rows.toList)
  }
}
object Text{
	val cleanTextRegEx1 = """\W""".r
	val cleanTextRegEx2 = """\s+""".r
	val cleanTextRegEx3 = """\d""".r
	
	//def cleanText(str1: String, stripNonAlphaNumeric: Boolean, stripNumbers: Boolean) = {
	def cleanText(str1: String) = {
		cleanTextRegEx3 replaceAllIn(str1," ")
	}


}
object ConText {
  println("Welcome to the Scala worksheet")
  class ContextItem(args: Array[String]){
	  val literal = args(0)
	  val category = args(1).toLowerCase().split(",")
	  val re = if( args.length >= 3 ) (""""""+args(2)).r else s"""$literal""".r
	  val rule = if( args.length >= 4 ) args(3) else ""
	
	  override def toString = "<<"+literal+">> <<"+category(0)+">> <<"+rule+">> <<"+rule+">>"
	
	  def isA( test_category: String): Boolean =
	    category contains test_category.toLowerCase()
	}
	class ItemData(val header: List[String], val items: List[ContextItem]){
	  override def toString = items.length + " ContextItem objects"
	}

	class ConTextTag(val matchedTerm: scala.util.matching.Regex.Match,
	                val matchingItem: ContextItem,
	                val scope: (Int, Int)){
	  val tagID = ""
	  def start = matchedTerm.start
	  def end = matchedTerm.end
	  def foundPhrase = matchedTerm.matched
	  def category = matchingItem.category
	  def rule = matchingItem.rule

	  /*def this(matchedTerm: scala.util.matching.Regex.Match, matchingItem: ContextItem) =
	    this(matchedTerm,matchingItem,Span.getSpan(matchedTerm,matchingItem))*/

	  override def toString = s"<id> $tagID </id><phrase> $foundPhrase </phrase><category> $category </category>"
	}
	class ConTextMarkup( val text: String){
		def markItems( items: ItemData ) = {
			// make a list of ConTextTag items for each item in items matching in text
		}
	}
	
}

object TestConTextItem{


  def main(args: Array[String]){

    val (h,i) = IO.instantiateItemDataFromCSV(args(0))
    val cid = new ConText.ItemData(h,i)
    println(cid)
    for( item <- cid.items ){
      println(item.re.pattern+"-->"+item.literal+"-->"+item.re.findFirstIn(item.literal))
    }
  }
  var b = "Pulmonary Embolism\tPE\tPE\tNone"
  b
  var c = b.split("\t").map(_.trim)
  var d = new ConText.ContextItem(b.split("\t").map(_.trim))
  var g1 = Graph[Int,DiEdge](1,2,3,1~>2,1~>3)
  println(g1)
}
