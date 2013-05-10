package service

import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import hashing.TextBlockHash._
import play.api.test.Helpers._
import model.Document
import model.Default
import scala.io.Source
import dataaccess.DataAccessLayer
import org.specs2.mock.Mockito
import dataaccess.DocumentRepository
import dataaccess.InMemoryDocumentRepository
import dataaccess.InMemoryCommentRepository

@RunWith(classOf[JUnitRunner])
class DocumentServiceSpec extends SpecificationWithJUnit with DocumentComponent with DataAccessLayer {
  val service = new DefaultDocumentService()
  val documentRepository = new InMemoryDocumentRepository // TODO replace with mock
  val commentRepository = new InMemoryCommentRepository // TODO replace with mock

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
      val docR = new Document(removedText, Default)
      val diff = service.compareDocuments(docA, docR)

      diff should have size 1
      diff.head.oldValue startsWith ("This one (2) will be removed")
      diff.head.newValue === ""
      diff.head.newHash === None
    }

    "compareDocuments should detect addition." in {
      val docA = new Document(text, Default)
      val docB = new Document(addedText, Default)
      val diff = service.compareDocuments(docA, docB)

      diff should have size 1
      diff.head.oldValue === ""
      diff.head.oldHash === None
      diff.head.newValue startsWith ("this is added")
    }

    "compareDocuments should detect changes." in {
      val docA = new Document(text, Default)
      val docB = new Document(changedText, Default)
      val diff = service.compareDocuments(docA, docB)

      diff should have size 2
      diff.head.oldValue startsWith "This one (2) will be removed"
      diff.head.newValue startsWith ("This one (2) will not be removed")
      diff.head.oldHash !== diff.head.newHash
    }

    // fixed this one...
//    "compareDocuments: removing while adding a new one will result in an ambiguous 'change'" in {
//      val docA = new Document(text, Default)
//      docA.rawBlocks.foreach(println)
//      val docB = new Document(ambiguousText, Default)
//      docB.rawBlocks.foreach(println)
//      val diff = service.compareDocuments(docA, docB)
//
//      println("\ndifferences:")
//      diff.foreach(println)
//      diff should have size 2
//    }
    
    "compareDocuments of the two testfiles should show 3 differences" in {
      val file1 = Source.fromInputStream(Thread.currentThread ().getContextClassLoader ().getResourceAsStream("service/TestFile_1.md"))
      val file2 = Source.fromInputStream(Thread.currentThread ().getContextClassLoader ().getResourceAsStream("service/TestFile_2.md"))
      val doc1 = new Document(file1.getLines.reduceLeft( (a, b) => a+"\n"+b), Default)
      val doc2 = new Document(file2.getLines.reduceLeft( (a, b) => a+"\n"+b), Default)
      val diff = service.compareDocuments(doc1, doc2)
      diff.foreach(println)
       diff should have size 3
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