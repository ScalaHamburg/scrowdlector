package model

import hashing.TextBlockHash._

sealed trait DocumentType { 
    def name: String
    def blockStrategy : FindBlockBorder
    def idStrategy : CreateBlockID
  }
  
  case object MarkdownText extends DocumentType { val name = "Markdown" 
    override def blockStrategy : FindBlockBorder = newlineBlockStrategy
    override def idStrategy : CreateBlockID = buildSimpleHashStrategy}
  
  case object ScalaCode extends DocumentType { 
    val name = "Scala"
    override def blockStrategy = explicitBlockStrategy
    override def idStrategy = extractExplicitIdStrategy
  }