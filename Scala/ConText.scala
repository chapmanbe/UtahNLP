import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.util.matching.Regex
import scala.math

class ContextItem(args: Array[String]){
  val literal = args(0)
  val category = (args(1).toLowerCase().split(",")
  val re = if( args.length >= 3 ) (""""""+args(2)).r else s"""$literal""".r
  val rule = if( args.length >= 4 ) args(3) else ""

  override def toString = "<<"+literal+">> <<"+category(0)+">> <<"+rule+">> <<"+rule+">>"

  def isA( test_category: String){
    category contains test_category.toLowerCase()
  }
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

  def this(matchedTerm: scala.util.matching.Regex.Match,
           matchingItem: ContextItem) = {
           this(matchedTerm,matchingItem,Span.getSpan(matchedTerm,matchingItem))
  }
  override def toString = s"<id> $tagID </id><phrase> $foundPhrase </phrase><category> $category </category>"
}


class ConTextDocument

class ConTextMarkup

object TestConTextItem{


  def main(args: Array[String]){

    val (h,i) = IO.instantiateItemDataFromCSV(args(0))
    val cid = new ItemData(h,i)
    println(cid)
    for( item <- cid.items ){
      println(item.re.pattern+"-->"+item.literal+"-->"+item.re.findFirstIn(item.literal))
    }
  }
}

object Span{

  def getSpan( matchedTerm: scala.util.matching.Regex.Match, matchingItem: ContextItem) = {
    if( matchingItem.rule == "forward") {
      (matchedTerm.end,matchedTerm.source.length) 
    } else if( matchingItem.rule == "backward"){
      (0,matchedTerm.start)
    } else {
      (0,matchedTerm.source.length)
    }
  }

  def limitScope( target: ConTextTag, modifier: ConTextTag){
    val tRule = target.rule
    if( tRule == "" || 
        tRule == "terminate" ||
        (! target.matchingItem.isA(modifier.category) && modifier.rule != "terminate") ){
          target
    } else{
      var newStart = target.start 
      var newEnd = target.end
      if( ((tRule contains "forward") || (tRule contains "bidirectional") ) && modifier.start > target.end )
        newEnd = math.min(target.end,modifier.start)
      else if( ( (tRule contains "backward") || (tRule contains "bidirectional") ) && modifier.end < target.start)
        newStart = math.max(target.start, modifier.end)
      ConTextTag(target.matchedTerm,target.matchingItem,(newStart,newEnd))
    }
  }
  def dist(tag1: ConTextTag,tag2: ConTextTag)
    math.min( math.abs(tag1.start-tag2.end),math.abs(tag1.end-tag2.start))
  def encompasses(tag1: ConTextTag,tag2: ConTextTag)
    tag1.start <= tag2.start && tag1.end >= tag2.end

  def replaceCategory(tag1: ConTextTag,category: String) 
    tag1
  
}

object IO{
  def instantiateItemDataFromCSV(filename: String) = {
    val rows = ArrayBuffer[ContextItem]()
    //val rows = List()
    val source =Source.fromFile(filename)
    val iter = source.getLines 
    val header = (iter.next).split("\t")
    for (line <- iter ){
      //rows += line.split("\t").map(_.trim)
      //println(line)
      //new ContextItem(line.split("\t")) +: rows
      rows += new ContextItem(line.split("\t").map(_.trim))
    }
    //println("length of rows is "+rows.length)
    (header.toList, rows.toList)
  }
}

object TestSpan{


  def main(args: Array[String]){

    val (h,i) = IO.instantiateItemDataFromCSV(args(0))
    val cid = new ItemData(h,i)
    println(cid)
    for( item <- cid.items ){
      println(item.re.pattern+"-->"+item.literal+"-->"+item.re.findFirstIn(item.literal))
    }
  }
}










