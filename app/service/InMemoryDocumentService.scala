package service

import model.Comment
import model.Document
import model.MarkdownText
import model.ScalaCode
import hashing.TextBlockHash.CreateBlockID

class InMemoryDocumentService extends DocumentService {
  val markdownText =
    """
  	|# Scrowdlector #
  	|
  	|Eine Play-App zum kommentiewren von Text-Dateien.
  	|Diese Datei dient aber auch gleich als markdown test.
  	|
  	|## Was ist überhaupt das Problem? ##
  	|------------------------------
  	|
  	|Ein Wiki ist geeignet zur Darstellung des aktuellen Konsenses. Wenn es Kontroversen gibt, muss oft gegen den "jeder darf editieren" verstoßen werden. nhalt zeichnet niemand verantwortlich. Ein Wiki beruht auf Symmetrie zwischen den Benutzern.
  	|Gelegentlich ist diese Symmetrie nicht gegeben oder nicht gewünscht. Vielleicht soll ein Text entstehen, der einem Autor / einer Gruppe zuordenbar ist, da diese(r) am Ende dafür verantwortlich gemacht wird. Solche Texte schreibt man gegenwärtig meist allein. Das sollte inzwischen besser gehen.
  	|
  	|Ein typisches Beispiel ist die Erstellung einer Spezifikation, bei der die Meinung der Autoren normativ ist; Kommentatoren aber essentiell wichtig sind, um den Text verständlich und lesbar zu machen.
  	|
  	|## Roles, Concepts, Use cases, Processes ##
  	|
  	|### Roles ###
  	|#### Author ####
  	|There may be several of them. Autors have the right to edit the main text.
  	|#### Commenter ####
  	|There may be many of them. Commenters, well, comment.
  	|        
  	|        Markdown check:
  	|*italic*   **bold**
  	|_italic_   __bold__
  	""".stripMargin

  val scalaCode = """
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
  	|""".stripMargin

  // dummy Implementation until GIT repo is implemented
  // real world implementation may find DocumentType by file extension.
  def find(url: String): Option[Document] = {
    url match {
      case "example.md" => Some(new Document(markdownText, MarkdownText))
      case "example.scala" => Some(new Document(scalaCode, ScalaCode))
      case _ => None
    }
  }
}