package service

import model.Document
import model.ScalaCode
import model.MarkdownText
import hashing.TextBlockHash.CreateBlockID

trait DocumentService {
  
  def find(url: String): Option[Document]

  /**
   * Return List of tuples containing the Block, Option of originalBlockHash, newBlock and Option of newVersionBlockHash
   * You want to update the referencing comment hashes with these new ones.
   * in case of originalBlockHash missing, this is a new Block
   * in case of newVersionBlockHash missing, the block was removed
   */
  def compareDocuments(original: Document, newVersion: Document): List[(String, Option[String], String, Option[String], Int)] = {
    /**
     * create map of blocks with the hash as key
     */
    def docToMap(doc: model.Document): scala.collection.immutable.Map[String, String] = {
      val docInversed = for { (block, hash) <- doc.rawBlocks } yield (hash, block)
      docInversed.toMap
    }

    /**
     * returns a map containing mappings from origMap that are not contained in newVersMap
     */
    def findChanges(origMap: Map[String, String], newVersMap: Map[String, String]): Map[String, String] = {
      origMap.filter(tpl => !newVersMap.contains(tpl._1))
    }

    /**
     * finds changed blocks
     */
    def detectChanges(oldMap: Map[String, String], newMap: Map[String, String], idStrategy: CreateBlockID) = {
      val newValues = newMap.values.toList;
      val oldValues = oldMap.values.toList;

      if(!newValues.isEmpty && !oldValues.isEmpty){
        val res = for {
          oldValue <- oldValues
        } yield {
          val unordered = newValues.map(str => (compareBlocks(oldValue, str), str))
          val orderAndtxt = unordered.sortWith((a, b) => a._1 < b._1).head //more equal blocks first
          val order = orderAndtxt._1
          val newValue = orderAndtxt._2
          val oldHash = (oldMap.find(tpl => tpl._2 == oldValue)).map(t => t._1)
          val newHash = (newMap.find(tpl => tpl._2 == newValue)).map(t => t._1)
          (oldValue, oldHash, newValue, newHash, order)
        }

        // keep only the best matches
        res.filter {
          result =>
            val others = res.find(r => r._1 == result._1 && r._3 != result._3)
            others match {
              case Some(tpl) => result._5 < tpl._5
              case None => true
            }
        }
      }else{
        List()
      }
    }
    
    def addNewBlocks(newMap: Map[String, String], changes: List[(String, Option[String], String, Option[String], Int)], idStrategy: CreateBlockID) = {
      val newValues = newMap.values.toList;

      // add the new blocks (of newValues that are not yet mapped in results newValue)
      val newBlocks = newValues.filter(value => changes.count(tpl => tpl._3 == value) == 0)

      changes ++ newBlocks.map(str => ("", None, str, Some(idStrategy(str)), 0))
    }

    def addRemovedBlocks(removedMap: Map[String, String], changes: List[(String, Option[String], String, Option[String], Int)], idStrategy: CreateBlockID) = {
      val removedValues = removedMap.values.toList;

      // add the new blocks (of newValues that are not yet mapped in results newValue)
      val removedBlocks = removedValues.filter(value => changes.count(tpl => tpl._1 == value) == 0)

      changes ++ removedBlocks.map(str => (str, Some(idStrategy(str)), "", None, 0))
    }

    val origMap = docToMap(original)
    val newVersMap = docToMap(newVersion)

    // remove all unmodified blocks
    val oldBlocks = findChanges(origMap, newVersMap)
    val newBlocks = findChanges(newVersMap, origMap)

    // see, if there are any changed blocks
    val changes = detectChanges(oldBlocks, newBlocks, newVersion.docType.idStrategy)

    val chgAndNew = addNewBlocks(newBlocks, changes, newVersion.docType.idStrategy)

    // Todo add removed blocks
    addRemovedBlocks(oldBlocks, chgAndNew, newVersion.docType.idStrategy)
  }

  /**
   * This first, very simple approach compares all the tokens of both Strings
   * Added or removed words add +1 to the distance.
   */
  def compareBlocks(blockA: String, blockB: String) = {
    val a = blockA.split("""\s+""") // split at (and ignore) whitespace
    val b = blockB.split("""\s+""")
    a.foldLeft(0)((count, token) => if (b.contains(token)) count else count + 1) +
      b.foldLeft(0)((count, token) => if (a.contains(token)) count else count + 1)
  }

}