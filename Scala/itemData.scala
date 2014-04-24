package ConTextItems

import scala.io.Source
import scala.collection.mutable.ArrayBuffer


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








