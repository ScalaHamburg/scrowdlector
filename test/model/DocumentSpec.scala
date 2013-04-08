package model

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class DocumentSpec extends SpecificationWithJUnit {

  val document = new Document(
    """
  	|## Scrowdlector ##
  	|
  	|Eine Play-App zum kommentiewren von Text-Dateien.
  	|Diese Datei dient aber auch gleich als markdown test.
  	|
  	|## Was ist Uberhaupt das Problem?
  	|------------------------------
  	|
  	|Ein Wiki ist geeignet zur Darstellung des aktuellen Konsenses. Wenn es Kontroversen gibt, muss oft gegen den "jeder darf editieren" verstossen werden. Inhalt zeichnet niemand verantwortlich. Ein Wiki beruht auf Symmetrie zwischen den Benutzern.
  	|Gelegentlich ist diese Symmetrie nicht gegeben oder nicht gewuenscht. Vielleicht soll ein Text entstehen, der einem Autor / einer Gruppe zuordenbar ist, da diese(r) am Ende dafuer verantwortlich gemacht wird. Solche Texte schreibt man gegenwaertig meist allein. Das sollte inzwischen besser gehen.
  	|
  	|Ein typisches Beispiel ist die Erstellung einer Spezifikation, bei der die Meinung der Autoren normativ ist; Kommentatoren aber essentiell wichtig sind, um den Text verst�ndlich und lesbar zu machen.
  	|
  	|## Roles, Concepts, Use cases, Processes
  	|
  	|### Roles
  	|#### Author
  	|There may be several of them. Autors have the right to edit the main text.
  	|#### Commenter
  	|There may be many of them. Commenters, well, comment.
     *italic*   **bold**\n_italic_   __bold__
  	""".stripMargin, MarkdownText)

  val scalaDocument = new Document(
    """
  	|package model
  	|
  	|import hashing.TextBlockHash._ //[id:head]
  	|
  	|sealed trait DocumentType { 
    |	def name: String
    |	def blockStrategy : FindBlockBorder = newlineBlockStrategy
    |	def idStrategy : CreateBlockID = buildSimpleHashStrategy
    |} // [id:mainClass]
  	|  
    |case object MarkdownText extends DocumentType { val name = "Markdown" } // [id:case1] 
  	|  
    |case object ScalaCode extends DocumentType { 
    |	val name = "Scala"
    |	override def blockStrategy = explicitBlockStrategy
    |	override def idStrategy = extractExplicitIdStrategy
    |}//[id:case2]
      
  	|""".stripMargin, ScalaCode)

  "Document blocks" should {
    "have size 7" in {
      document.blocks.size === 7
    }
    "have size 4" in {
      scalaDocument.blocks.foreach(println)
      scalaDocument.blocks.size === 4
    }
  }

}