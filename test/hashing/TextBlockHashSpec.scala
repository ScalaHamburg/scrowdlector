package hashing

import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner

import hashing.TextBlockHash._
import play.api.test.Helpers._

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
			|usw. // [id:Block123456789]
			|
			|
			|noch ein Block 
			|(der dritte)// [id:Block2]
  		|""".stripMargin

  "TextBlockHash" should {

    "recognize three blocks" in {
      blockify(text) must have size 3
    }

    "recognize two blocks" in {
    	blockify(text, explicitBlockStrategy) must have size 2
    }
    "extract id with extractExplicitIdStrategy" in {
      identifyBlock("noch ein Block\n(der dritte)// [id:Block2]", extractExplicitIdStrategy)._2 === "Block2"
    } 

    "create the same hash for equal blocks" in {
      (identifyBlock("123Test", buildSimpleHashStrategy)._2) === (identifyBlock("123Test", buildSimpleHashStrategy)._2)
    }

    "create a different hash for unequal blocks" in {
      (identifyBlock("123TestA", buildSimpleHashStrategy)._2) !== (identifyBlock("123TestB", buildSimpleHashStrategy)._2)
    }

    "should return a sequence of Pairs (text + hash!)" in {
      // TODO I couldnt get the instaneOf test working...
      identifyBlock(blockify(text).head, buildSimpleHashStrategy)._2 === "1882604995"
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