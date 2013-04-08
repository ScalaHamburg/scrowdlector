package hashing

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import hashing.TextBlockHash._
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.hamcrest.core.IsInstanceOf

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class TextBlockHashSpec extends SpecificationWithJUnit {

  val text =
    """|Zeile 1
			|Zeile 2
			|Zeile 3
			|
			|Block2
			|usw.
			|
			|
			|noch ein Block 
			|(der dritte)""".stripMargin

  "TextBlockHash" should {

    "recognize three blocks" in {
      blockify(text) must have size 3
    }

    "create the same hash for equal blocks" in {
      (hashBlock("123Test")._2) === (hashBlock("123Test")._2)
    }

    "create a different hash for unequal blocks" in {
      (hashBlock("123TestA")._2) !== (hashBlock("123TestB")._2)
    }

    "should return a sequence of Pairs (text + hash!)" in {
      // TODO I couldnt get the instaneOf test working...
      hashBlock(blockify(text).head)._2 === "623187366"
    }

    "compareBlocks should return 0 when called with equal blocks" in {
      val blockA = "a simple 	example      block"
      val blockB = "a\rsimple\nexample block"

      compareBlocks(blockA, blockB) === 0
    }

    "compareBlocks should return a larger value for more differences" in {
      val blockA = "a simple example block"
   		val blockB = "a different example block"
   		val blockC = "this is a different example block"

      compareBlocks(blockA, blockB) must be lessThan compareBlocks(blockA, blockC)
    }
  }
}