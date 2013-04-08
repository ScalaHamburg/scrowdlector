package model

import hashing.TextBlockHash._
import play.api.templates.Html
import model.Document._

sealed trait DocumentType {
  def name: String
  def blockStrategy: FindBlockBorder
  def idStrategy: CreateBlockID
  def displayStrategy: HTMLConverter = anyToHtml
}

case object Default extends DocumentType {
  val name = "Default"
  override def blockStrategy: FindBlockBorder = newlineBlockStrategy
  override def idStrategy: CreateBlockID = buildSimpleHashStrategy
}

case object MarkdownText extends DocumentType {
  val name = "Markdown"
  override def blockStrategy: FindBlockBorder = newlineBlockStrategy
  override def idStrategy: CreateBlockID = buildSimpleHashStrategy
  override def displayStrategy: HTMLConverter = markdownToHtml
}

case object ScalaCode extends DocumentType {
  val name = "Scala"
  override def blockStrategy = explicitBlockStrategy
  override def idStrategy = extractExplicitIdStrategy
  override def displayStrategy: HTMLConverter = codeToHtml
}