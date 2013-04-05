package hashing

object TextBlockHash {
  /**
   * First approach to cut a text into some blocks that may be commented.
   * A Block ends at file end or a sequence of newlines.
   */
  def blockify(text: String) = {
    def combine(block: String, stream: Stream[String]): Stream[String] = {
      stream match {
        case line #:: tail => {

          if (line.trim().length() == 0) {
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

    def combineLines(lines: Stream[String]): Stream[String] = {
      lines match {
        case x #:: tail => combine(x, tail)
        case strm       => strm
      }
    }
    combineLines(text.linesWithSeparators.toStream).map(hashBlock(_))
  }

  /**
   * creates a Hash of the
   */
  def hashBlock(block: String) = {
	// TODO wir brauchen unterschiedliche hashCodes, für blöcke die aus den selben Zeilen bestehen... oder?
    (block, block.hashCode().toString)
  }
}
