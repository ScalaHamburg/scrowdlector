package service

import dataaccess.DataAccessLayer
import hashing.TextBlockHash.CreateBlockID
import model.Document

trait DocumentComponent {
  this: DataAccessLayer =>

  class DefaultDocumentService extends DocumentService {
    def find(url: String): Option[Document] = {
      documentRepository.find(url)
    }

    // Two blocks & hashes and their difference weight
    case class BlkDiff(val oldValue: String, val oldHash: Option[String], val newValue: String, val newHash: Option[String], val weight: Int)

    //  case class DocumentDiff

    /**
     * Return List of tuples containing the Block, Option of originalBlockHash, newBlock and Option of newVersionBlockHash
     * You want to update the referencing comment hashes with these new ones.
     * in case of originalBlockHash missing, this is a new Block
     * in case of newVersionBlockHash missing, the block was removed
     *
     * Attention: This function may not produce the correct result in all cases! (because it's hard!)
     */
    def compareDocuments(original: Document, newVersion: Document): List[BlkDiff] = {
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

        if (!newValues.isEmpty && !oldValues.isEmpty) {
          val res = for {
            oldValue <- oldValues
          } yield {
            val unordered = newValues.map(str => (compareBlocks(oldValue, str), str))
            val weightAndtxt = unordered.sortWith((a, b) => a._1 < b._1).head //more equal blocks first
            val weight = weightAndtxt._1
            val newValue = weightAndtxt._2
            val oldHash = (oldMap.find(tpl => tpl._2 == oldValue)).map(t => t._1)
            val newHash = (newMap.find(tpl => tpl._2 == newValue)).map(t => t._1)
            BlkDiff(oldValue, oldHash, newValue, newHash, weight)
          }

          // keep only the best matches
          val bestMatches = res.filter {
            result =>
              val others = res.find(r => r.oldValue == result.oldValue && r.newValue != result.newValue)
              others match {
                case Some(tpl) => result.weight < tpl.weight
                case None => true
              }
          }
          // avoid ambiguity: delete & new <--> change
          bestMatches
            .filter {
              diff =>
                // TODO we need to fiddle with this ratio:
                diff.weight < countTokens(diff.oldValue) // /2 /3
              //
            }
        } else {
          List()
        }
      }

      def addNewBlocks(newMap: Map[String, String], changes: List[BlkDiff], idStrategy: CreateBlockID) = {
        val newValues = newMap.values.toList;

        // add the new blocks (of newValues that are not yet mapped in results newValue)
        val newBlocks = newValues.filter(value => changes.count(diff => diff.newValue == value) == 0)

        changes ++ newBlocks.map(str => BlkDiff("", None, str, Some(idStrategy(str)), 0))
      }

      def addRemovedBlocks(removedMap: Map[String, String], changes: List[BlkDiff], idStrategy: CreateBlockID) = {
        val removedValues = removedMap.values.toList;

        // add the new blocks (of newValues that are not yet mapped in results newValue)
        val removedBlocks = removedValues.filter(value => changes.count(tpl => tpl.oldValue == value) == 0)

        changes ++ removedBlocks.map(str => BlkDiff(str, Some(idStrategy(str)), "", None, 0))
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

    def countTokens(blockA: String): Int = {
      blockA.split("""\s+""").length
    }
  }
}