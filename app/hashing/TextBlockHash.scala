package hashing

// import org.apache.commons.lang.StringUtils

object TextBlockHash {
	/**
	 * First approach to cut a text into some blocks that may be commented.
	 * A Block ends at file end or a sequence of newlines.
	 */
	def blockify(text: String) = {
		def combineLines(lines: Stream[String]): Stream[String] = {
				lines match {
				case x #:: tail => combine(x, tail)
				case strm       => strm
				}
		}
		
		def combine(block: String, stream: Stream[String]): Stream[String] = {
				stream match {
				case line #:: tail => {
					
					if (line.length < 2 /* TODO FIXME StringUtils.isBlank(line) */) {
						if (block.size == 0) {
							combine("", tail)
						} else {
							block #:: combine("", tail)
						}
					} else {
						
						combine(block + line, tail)
					}
					
				}
				case strm => block #:: strm
				}
		}
		
		def hashBlock(block:String) = {
		  // TODO wir brauchen unterschiedliche hashCodes, für blöcke die aus den selben Zeilen bestehen...
			(block, block.hashCode().toString)
		}
	
		combineLines(text.linesWithSeparators.toStream).map(hashBlock(_))
	}
	

	def main(args: Array[String]) {
    val text = """|Zeile 1
  	  |Zeile 2
  	  |Zeile 3
    	|
  	  |Block2
  	  |usw.
      |
      |
      |noch ein Block 
      |(der dritte)""".stripMargin

    val ergebnis = blockify(text)
    ergebnis.foreach { s =>
      println(">>>>>")
      println(s)
      println("<<<<<")
    }

  }
}