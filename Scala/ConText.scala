package ConTextItems

import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.util.matching.Regex

class ContextItem(args: Array[String]){
  val literal = args(0)
  val category = ((args(1)).toLowerCase()).split(",")
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

class tagObject(val matchedTerm: scala.util.matching.Regex.Match, val matchingItem: ContextItem){
  val tagID = ""
  val start = matchedTerm.start
  val end = matchedTerm.end
  val foundPhrase = matchedTerm.matchedTerm
  val category = matchingItem.category

  override def toString = s"<id> $tagid </id><phrase> $foundPhrase </phrase><category> $category </category>"
}


class ConTextDocument

class ConTextMarkup

object TestConTextItem{

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

  def main(args: Array[String]){

    val (h,i) = instantiateItemDataFromCSV(args(0))
    val cid = new ItemData(h,i)
    println(cid)
    for( item <- cid.items ){
      println(item.re.pattern+"-->"+item.literal+"-->"+item.re.findFirstIn(item.literal))
    }
  }
}








