package service

import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import hashing.TextBlockHash._
import play.api.test.Helpers._
import model.Document
import model.Default

@RunWith(classOf[JUnitRunner])
class DocumentServiceSpec extends SpecificationWithJUnit {
  val service = new DocumentService() {}

  val text =
    """|Block 1 (stays the same))
	   |
	   |This one (2) will be removed
	   |
	   |
	   |Block 3 (is changed)""".stripMargin

  val removedText = """|Block 1 (stays the same))
	   |
	   |
	   |
	   |Block 3 (is changed)""".stripMargin

  val addedText = """|Block 1 (stays the same))
	   |
	   |This one (2) will be removed
       |
	   |this is added
	   |
	   |Block 3 (is changed)""".stripMargin

  val changedText =
    """|Block 1 (stays the same))
	   |
	   |This one (2) will not be removed
	   |
	   |
	   |Block 3 (is now changed)""".stripMargin

  val ambiguousText =
    """|Block 1 (stays the same))
	   |
	   |The fourth's new!
	   |
	   |Block 3 (was changed)""".stripMargin

  "DocumentService" should {

    "compareDocuments should detect removal." in {
      val docA = new Document(text, Default)
      docA.rawBlocks.foreach(println)
      val docR = new Document(removedText, Default)
      val diff = service.compareDocuments(docA, docR)

      println("\ndifferences:")
      diff.foreach(println)
      diff should have size 1
      diff.head._1 startsWith ("This one (2) will be removed")
      diff.head._3 === ""
      diff.head._4 === None
    }

    "compareDocuments should detect addition." in {
      val docA = new Document(text, Default)
      docA.rawBlocks.foreach(println)
      val docB = new Document(addedText, Default)
      val diff = service.compareDocuments(docA, docB)

      println("\ndifferences:")
      diff.foreach(println)
      diff should have size 1
      diff.head._1 === ""
      diff.head._2 === None
      diff.head._3 startsWith ("this is added")
    }

    "compareDocuments should detect changes." in {
      val docA = new Document(text, Default)
      docA.rawBlocks.foreach(println)
      val docB = new Document(changedText, Default)
      val diff = service.compareDocuments(docA, docB)

      diff should have size 2
      diff.head._1 startsWith "This one (2) will be removed"
      diff.head._3 startsWith ("This one (2) will not be removed")
      diff.head._2 !== diff.head._4
    }

    "compareDocuments: removing while adding a new one will result in an ambiguous 'change'" in {
      val docA = new Document(text, Default)
      docA.rawBlocks.foreach(println)
      val docB = new Document(ambiguousText, Default)
      docB.rawBlocks.foreach(println)
      val diff = service.compareDocuments(docA, docB)

      println("\ndifferences:")
      diff.foreach(println)
      diff should have size 2
    }

    "compareBlocks should return 0 when called with equal blocks" in {
      val blockA = "a simple 	example      block"
      val blockB = "a\rsimple\nexample block"

      service.compareBlocks(blockA, blockB) === 0
    }

    "compareBlocks should return a larger value for more differences" in {
      val blockA = "a simple example block"
      val blockB = "a different example block"
      val blockC = "this is a different example block"

      service.compareBlocks(blockA, blockB) must be lessThan service.compareBlocks(blockA, blockC)
    }

  }
}