package model

import hashing.TextBlockHash._
import play.api.templates.Html
import model.Document._

/**
 * Support for different types of documents 
 */
sealed trait DocumentType {
  def name: String
  def blockStrategy: FindBlockBorder = newlineBlockStrategy
  def idStrategy: CreateBlockID = buildSimpleHashStrategy
  def displayStrategy: HTMLConverter = anyToHtml
}

object DocumentType {
  val mappings = Map(
      (".md" -> MarkdownText),
      (".scala" -> ScalaCode)
  )
  
  def byFileExtension(filename: String) = mappings.getOrElse(filename, Default)
}

case object Default extends DocumentType {
  val name = "Default"
}

case object MarkdownText extends DocumentType {
  val name = "Markdown"
  override def displayStrategy: HTMLConverter = markdownToHtml
}

case object ScalaCode extends DocumentType {
  val name = "Scala"
  override def blockStrategy = explicitBlockStrategy
  override def idStrategy = extractExplicitIdStrategy
  override def displayStrategy: HTMLConverter = codeToHtml
}