package hashing

import java.security.MessageDigest

/**
 * A collection of functions to transform a monolithic text in multiple blocks that are identifiable by
 * block hashes or IDs.
 * There are multiple strategies for finding the block borders and for creating a block hash or extracting a block-ID
 */
object TextBlockHash {

  type FindBlockBorder = String => Boolean
  type CreateBlockID = String => String

  // Strategies for block border detection
  def newlineBlockStrategy: FindBlockBorder = { line: String => line.trim().length() == 0 }
  private val explicitBlockPattern = """.*\[id:([A-Za-z0-9]+)\]\s*"""
  def explicitBlockStrategy: FindBlockBorder = { line: String => line.matches(explicitBlockPattern) } // finds lines containing: [id:{blockid}] where {blockid} is alphanumeric 

  // strategies for hash creation
  /**
   * Hash is built from the Block-contents
   */
  def buildSimpleHashStrategy: CreateBlockID = { block: String => block.hashCode().toString }
  
  /**
   * I guess SHA1 is also used by GIT
   */
  def buildSHA1HashStrategy: CreateBlockID  = { block: String =>
    val digest = MessageDigest.getInstance("SHA1").digest(block.getBytes())
    val hex=digest.map(Integer.toHexString(_))
    hex.mkString("")
    }
  /**
   * Id is extracted from block marker eg: [id:123]
   */
  def extractExplicitIdStrategy: CreateBlockID = {
    block: String =>
      val line = block.lines.find(explicitBlockStrategy).getOrElse("")
      val pattern = explicitBlockPattern.r
      pattern.findFirstMatchIn(line).map(grp => grp.group(1)).getOrElse("")
  }

  /**
   * convenience function to create (block,hash) tuples
   */
  def breakIntoIdentifiableBlocks(text: String, blockStrategy:FindBlockBorder, createHash: CreateBlockID) = {
    blockify(text, blockStrategy).map(identifyBlock(_, createHash))
  }

  /**
   * First approach to cut a text into some blocks that may be commented.
   * A Block ends at file end or depending on the FindBlockBorder function.
   * The default FindBlockBorder function is the newlineBlockStrategy.
   */
  def blockify(text: String, findBlockBorder: FindBlockBorder = newlineBlockStrategy) = {
    def combine(block: String, stream: Stream[String]): Stream[String] = {
      stream match {
        case line #:: tail => {

          if (findBlockBorder(line)) {
            if(block.trim().length() == 0 && line.trim().length==0){
            	combine("", tail)
            }else{
            	(block + line) #:: combine("", tail)
            }
          } else {
            combine(block + line, tail)
          }

        }
        case strm => {
          if (block.trim().length() == 0) {
            strm
          } else {
            block #:: strm
          }
        }
      }
    }

    def combineLines(lines: Stream[String]): Stream[String] = {
      lines match {
        case x #:: tail => combine(x, tail)
        case strm       => strm
      }
    }
    combineLines(text.linesWithSeparators.toStream)
  }

  /**
   * After the text was blockified we create a identifier (eg. hash) of each block
   * The default CreateBlockID-method is the buildSimpleHashStrategy
   */
  def identifyBlock(block: String, createHash: CreateBlockID = buildSHA1HashStrategy) = {
    (block, createHash(block))
  }

}
