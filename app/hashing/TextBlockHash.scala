package hashing

class TextBlockHash {

  /**
   * First approach to cut a text into some blocks that may be commented.
   * A Block ends at file end or a sequence of newlines.
   */
  def blockify(text: String) = {
    combineLines(text.linesWithSeparators.toStream)
  }

  def combineLines(lines: Stream[String]): Stream[String] = {
    lines match {
      case x #:: tail => combine(x, tail)
      case strm => strm
    }
  }

  def combine(block: String, stream: Stream[String]): Stream[String] = {
    stream match {
      case line #:: tail => {
        line match {
          case str => 
            if(str.trim.length==0){
            block #:: combine(block, tail)
//            if (block.size == 0) {
//          	combine(block, tail)
          } else {
            
          	combine(block + line, tail)
          }
        }
      }
      case strm => block #:: strm
    }
  }

}

object TextBlockHash {
  def main(args: Array[String]) {
    val text = """
  	  |Zeile 1
  	  |Zeile 2
  	  |Zeile 3
    	|
  	  |Block2
  	  |usw.
  	  """.stripMargin
      val test = new TextBlockHash
      
    	val ergebnis = test.blockify(text)
    	ergebnis.foreach{s=>
	      println(">>>>>")
	      println(s)
	      println("<<<<<")
	    }
    
  }
}