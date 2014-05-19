import ConTextItem.ContextItem
import ConTextItem.IemData
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

//
// Testing the code sync option from Eclipse
//
//
//
// Another coomment
//